package com.adsk.analytics.mixpanel;

import com.adsk.analytics.APIConnection;
import com.adsk.analytics.utils.DateUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by aenugav on 9/10/2014.
 */
public class MixpanelAPIConnection extends APIConnection {
    private HttpURLConnection conn;

    public final static String LOGIN_EVENT = "Login";
    public final static String CREATE_AN_ACCOUNT_EVENT = "Create an acount";
    public final static String LAST_SEVEN_DAYS = "7";


    @Override
    protected void connect() {
        conn = generalBuildHTTPConnection(this.url);
//      buildAuthenticationHeader(conn, FlurryHelper.USERNAME, FlurryHelper.PASSWORD);
    }

    @Override
    protected void setServerOutput() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String output = "";
        String response = "";
        System.out.println("Output from Server .... \n");

        try {
            while ((output = br.readLine()) != null) {
                System.out.print(output);
                response += output;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.outputString = response;


    }

    @Override
    protected void disconnect() {
        conn.disconnect();
    }

    private HttpURLConnection generalBuildHTTPConnection(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            System.out.println("request:" + url.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            try {
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void getMixPanelEventActiveUsageData(APIConnection sendMetric, String eventType) {
        String url = buildMixpanelUrl(eventType);
        sendMetric.getAPIOutput(url);
    }

    public static String buildMixpanelUrl(String eventType) {

//        long mils = DateUtil.getCurrentUTCTimeinSeconds();
        String expire = "123312";
        TreeMap<String, String> map = new TreeMap<>();

        map.put("api_key", MixpanelHelper.API_KEY);
        map.put("expire", expire);
        map.put("event", "[\""+eventType+"\"]");
        map.put("type", "unique");
        map.put("unit", "day");
        map.put("interval", LAST_SEVEN_DAYS);
        map.put("name","Operating System");
        //map.put("values","")

        map.put("sig", md5(getJoinedParams(map)));

        String eventTypeEncoded = eventType.replace(" ","%20");
        String url = MixpanelHelper.EVENTS_URL+ "properties" + "?name=Operating%20System&interval=" + map.get("interval") + "&expire=" + map.get("expire") + "&sig=" + map.get("sig") + "&api_key=" + map.get("api_key")
                + "&type=" + map.get("type") + "&event=[\""+eventTypeEncoded+"\"]" + "&unit=" + map.get("unit");
        return url;
    }


    public static String getJoinedParams(TreeMap argsMap) {
        StringBuilder argsString = new StringBuilder();

        Set<Map.Entry<String, String>> entry = argsMap.entrySet();
        for (Iterator iterator = entry.iterator(); iterator.hasNext(); ) {
            argsString.append(iterator.next());

        }
        String joinedString = argsString + MixpanelHelper.API_SECRET;
        System.out.println(joinedString);
        return joinedString;
    }

    public static String md5(String input) {

        String md5 = null;

        if (null == input) return null;

        try {

            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            //Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());

            //Converts message digest value in base 16 (hex)
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
        return md5;
    }


    public static void main(String args[]) {
        APIConnection mixpanelAPIConnection = new MixpanelAPIConnection();
        getMixPanelEventActiveUsageData(mixpanelAPIConnection, CREATE_AN_ACCOUNT_EVENT);
    }
}
