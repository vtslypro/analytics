package com.adsk.acad.servlet.jersey.analyticsresources;

import com.adsk.analytics.utils.Base64;
import com.adsk.analytics.utils.FileUtils;
import com.adsk.analytics.adapter.DashboardFactory;
import com.adsk.analytics.exception.AnalyticsException;
import com.adsk.analytics.exception.NullWidgetException;
import com.adsk.analytics.geckoboard.GeckoboardHelper;
import com.google.gson.Gson;
import com.sun.jersey.spi.resource.Singleton;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by aenugav on 9/24/2014.
 */

@Component
@Singleton
@Path("/dashboard")
public class DashboardWidgetResource
{
    private static final Logger logger = LoggerFactory.getLogger(DashboardWidgetResource.class);



    HashMap<String,ArrayList<String>> credentialMap = new HashMap<String,ArrayList<String>>();
    @Context
    protected HttpServletRequest servletRequest;

    @Context
    protected HttpServletResponse servletResponse;

    @Context
    protected ServletContext context;

    public static GeckoboardHelper helper;

    @Autowired
    private DashboardFactory dashboardFactory;


    @GET
    @Path("/{dashboardName}/{widgetType}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object handleGetResponse(@PathParam("dashboardName") String dashboardName, @PathParam("widgetType")int widgetType) throws IOException
    {
        String responseMessage = "";
        String authHeader = servletRequest.getHeader("Authorization");

        try
        {
            if(widgetType==helper.SERVICE_TEST)
                return helper.SERVICE_RESULT;
            passwordAuthorization(authHeader);

            responseMessage = dashboardFactory.getDashboard(dashboardName).getWidgetData(widgetType);
            logger.debug("response : {}", responseMessage);
        }
        catch(NoSuchElementException nse)
        {
            logger.error("NoSuchElementException {}",nse);
            servletResponse.sendError(HttpStatus.SC_BAD_REQUEST, " The cache has failed to load in the scheduler.");
        }
        catch (NullWidgetException e) {
            logger.error("NullWidgetException {}", e);
            servletResponse.sendError(HttpStatus.SC_BAD_REQUEST, " Null Widget Exception ");
        }
        catch (AnalyticsException e) {
            logger.error("AnalyticsException {}",e);
            servletResponse.sendError(HttpStatus.SC_BAD_REQUEST, e.getMessage());
        }
        return responseMessage;
    }

    private void passwordAuthorization(String   header) throws AnalyticsException
    {
        String username = null;
        String password = null;

        if ((header == null) || (header.isEmpty()))
        {

            throw new AnalyticsException("Authorization header is missing");

        }

        logger.debug("before basic auth , header : {} " ,header);
        String unPw[] = getUsernamePasswordFromHeader(header);

        if (unPw != null)
        {
            username = "AnalyticsUser1";
            password = unPw[1];

            logger.debug("user :{} " , username + " pass : {} " , password);

            try
            {
                initializeConfiguration(System.getenv(GeckoboardHelper.BASIC_AUTH_USERS_CONFIG));
            } catch (AnalyticsException e) {
                logger.error("{} {} ",e.getMessage(), GeckoboardHelper.BASIC_AUTH_USERS_CONFIG);
            } catch (IOException e) {
                logger.error("{} {} ",e.getMessage(), GeckoboardHelper.BASIC_AUTH_USERS_CONFIG);
            }
            if(!credentialMap.isEmpty()) {

                logger.debug("Parsing Credential Map.... ");
                ArrayList<String> userList = credentialMap.get("userName");
                boolean userExists = false;
                for (String userNameFromMap : userList)
                {
                    if(username.equals(userNameFromMap))
                    {
                        userExists = true;
                        break;
                    }
                }
                if(!(userExists)||!(password.equals(GeckoboardHelper.GECKO_PASSWORD)))
                {
                    try {
                        servletResponse.sendError(HttpStatus.SC_FORBIDDEN, "username or password mismatch. Authentication Failed");
                    } catch (IOException e) {
                        logger.error(" IO exception {} ",e);
                    }
                }
                logger.debug("Credential Map parsing completed....");
            }
           /* else
            {
                logger.error(" ----------------------------The credential Map is empty ------------------------------------");
                logger.error(" ----------------------------The credential Map is empty ------------------------------------");
            }
*/
        }
        else
        {
            try
            {
                servletResponse.sendError(HttpStatus.SC_FORBIDDEN, "No username/password is submitted. Authentication Failed");
            }
            catch (IOException e)
            {
                logger.error(" IO exception {} ",e.getMessage());
            }
        }

    }

    public void initializeConfiguration(String configFilePath) throws AnalyticsException, IOException
    {
        if ((configFilePath == null) || (configFilePath.isEmpty())) {
            logger.error("Missing environment variable {} ", GeckoboardHelper.BASIC_AUTH_USERS_CONFIG);
            throw new AnalyticsException("Missing environment variable");
        }

        String fileContent = null;

        try {
            fileContent = FileUtils.getFileAsStream(configFilePath);
        } catch (FileNotFoundException e) {
            logger.error("Failed Loading the json file from path {}", configFilePath);
            throw new AnalyticsException("Failed Loading the json file from path");
        }

        logger.debug("analytics config file {}", fileContent);
        initConfigurationFromJson(fileContent);

    }

    public void initConfigurationFromJson(String configurationJson) throws AnalyticsException
    {
        credentialMap = new Gson().fromJson(configurationJson, HashMap.class);
    }
    public static String[] getUsernamePasswordFromHeader(String authorization)
    {
        StringTokenizer st = new StringTokenizer(authorization);

        if (st.hasMoreTokens())
        {
            String basic = st.nextToken();

            // We only handle HTTP Basic authentication
            if (basic.equalsIgnoreCase("Basic"))
            {
                String credentials = st.nextToken();

                String userPass = "";
                try
                {
                    try {
                        userPass = new String(Base64.decode(credentials));
                    } catch (IllegalArgumentException e) {
                        logger.error(" Illegal Argument Exception {} ",e.getMessage());
                    }
                }
                catch (IllegalArgumentException e)
                {
                    return null;
                }

                int p = userPass.indexOf(":");

                if (p != -1)
                {
                    String userId = userPass.substring(0, p);
                    String password = userPass.substring(p + 1);
                    return new String[] { userId, password };
                }
            }
        }

        return null;
    }

}
