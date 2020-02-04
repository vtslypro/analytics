package com.adsk.analytics.appannie;

import com.adsk.analytics.APIConnection;
import com.adsk.analytics.geckoboard.GeckoboardHelper;
import com.adsk.analytics.model.AppAnnieMonthlyData;
import com.adsk.analytics.utils.DateUtil;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;


public class AppAnnieAPIConnection extends APIConnection
{
	public final static int THREAD_SLEEP_TIME = 60000;
	public final static int RETRY_COUNT = 2;
//"rating","iap_sales_list","sales_list","ios","google-play"

	public final static String PRODUCT_REVIEW_KEY_NAME = "reviews";
	public final static String RATING_ITEM_KEY_NAME = "rating";
	public final static String PRODUCT_SALES_IAP_KEY_NAME = "iap_sales_list";
	public final static String PRODUCT_SALES_KEY_NAME = "sales_list";
	public final static String PRODUCT_REVIEW_MARKET_IOS = "ios";
	public final static String PRODUCT_REVIEW_MARKET_GOOGLE_PLAY = "google-play";

	private HttpsURLConnection conn;
	private static final Logger logger = LoggerFactory.getLogger(AppAnnieAPIConnection.class);
	private static GeckoboardHelper helper = GeckoboardHelper.Instance();

	private static AppAnnieHelper appAnnieHelper;

	public AppAnnieHelper getAppAnnieHelper() {
		return appAnnieHelper;
	}

	public void setAppAnnieHelper(AppAnnieHelper appAnnieHelper) {
		this.appAnnieHelper = appAnnieHelper;
	}

	@Override
	public void connect()
	{
		conn = buildHttpsConnection(conn);
		buildAutencticationHeader(conn);
	}

	@Override
	public void setServerOutput()
	{
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		}
		catch (IOException e)
		{
			logger.error("setServerOutput() : Exception while reading Stream  {} :", e);
		}

		String output = null;
		String response = null;

		try
		{
			while ((output = br.readLine()) != null) 
			{
				response += output;
			}
		}
		catch (IOException e)
		{
			logger.error("setServerOutput() : Exception while reading line  {} :", e);
		}
		
		this.outputString = response;
	}
	
	@Override
	public void disconnect()
	{
		conn.disconnect();
	}

	private HttpsURLConnection buildHttpsConnection(HttpsURLConnection conn)
	{
		URL url = null;
		
		try {
			url = new URL (this.url);
		}
		catch (MalformedURLException e)
		{
			logger.error("buildHttpsConnection() : Exception while instantiating URL object  {} :", e);
		}
		
		try	{
			conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			try
			{
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Content-Type", "application/json");
			}
			catch (ProtocolException e)
			{
				logger.error("buildHttpsConnection() : Exception in request method  {} :", e);
			}
		}
		catch (IOException e)
		{
			logger.error("buildHttpsConnection() : Exception when opening connection {} :", e);
		}
		return conn;		
	}

	private static void buildAutencticationHeader(HttpsURLConnection conn)
	{
		conn.setRequestProperty("Authorization", "bearer " + appAnnieHelper.getAppAnnieAPIKey());
	}

	public static ArrayList<LinkedTreeMap> getSalesList(APIConnection send, String accountID, String productID, String breakdown, String startDate, String endDate, String keyName)
	{
		String url= appAnnieHelper.getAppAnnieURL() +"/accounts/" + accountID + "/products/"
				+  productID + "/sales";

        if (breakdown != null && !breakdown.isEmpty()) {
            url = url + "?break_down=" + breakdown + "&start_date=" + startDate + "&end_date=" + endDate;
        }

		String responseFromAPI = "";

		for(int retryCount=1; retryCount <= RETRY_COUNT ; retryCount++)
		{
			try{
				responseFromAPI = send.getAPIOutput(url).replaceFirst("null", "");

				if(responseFromAPI == null){
					Thread.sleep(THREAD_SLEEP_TIME);
				}
				else{
					break;
				}
			}
			catch (InterruptedException e1) {
				logger.error("getSalesList() : Exception while thread sleep {} " , e1);
			}
			catch(Exception e)
			{
				logger.error("getSalesList() : Failed to get the response from AppAnnie {}  {} ", url , e);

				logger.error("getSalesList() : The Retry attempt will be made if  Retry Count {} is less than Max Retries {}  ", retryCount, RETRY_COUNT);
			}
		}

		HashMap<String,ArrayList<LinkedTreeMap>> salesMap = new Gson().fromJson(responseFromAPI, HashMap.class);

		ArrayList<LinkedTreeMap> salesList = new ArrayList<>();

		if(!salesMap.isEmpty()){
			salesList = salesMap.get(keyName);
		}

		return salesList;
	}

	public static ArrayList<LinkedTreeMap> getProductRatings(APIConnection send, String market, String productID, String startDate, String endDate, String keyName) {

		String url = appAnnieHelper.getAppAnnieURL() + "/apps/" + market + "/app/" + productID + "/reviews";

		if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
			url = url + "?start_date=" + startDate + "&end_date=" + endDate;
		}

		String responseFromAPI = "";

		for (int retryCount = 1; retryCount <= RETRY_COUNT; retryCount++) {
			try {
				responseFromAPI = send.getAPIOutput(url).replaceFirst("null", "");

				if (responseFromAPI == null) {
					Thread.sleep(THREAD_SLEEP_TIME);
				} else {
					break;
				}
			} catch (InterruptedException e1) {
				logger.error("getProductRatings() : Exception while thread sleep {} ", e1);
			} catch (Exception e) {
				logger.error("getProductRatings() : Failed to get the response from AppAnnie {}  {}. The Retry attempt will be made if  Retry Count {} is less than Max Retries {}  ");
			}
		}

		HashMap<String, ArrayList<LinkedTreeMap>> productReviewsMap = new Gson().fromJson(responseFromAPI, HashMap.class);

		ArrayList<LinkedTreeMap> productReviews = new ArrayList<>();

		if (!productReviewsMap.isEmpty()) {
			productReviews = productReviewsMap.get(keyName);
		}

		return productReviews;
	}

	//Get Average Product Rating  of the entire month for specific product from AppAnnie
	public static float getMonthlyAverageRatingByProduct(APIConnection send, String market, String productID, String startDate, String endDate) {

		ArrayList<LinkedTreeMap> reviewsList = getProductRatings(send, market, productID, startDate, endDate, PRODUCT_REVIEW_KEY_NAME);

		if (reviewsList == null) {
			logger.error(" getMonthlyAverageRatingByProduct() : Product Reviews List is null! Product Reviews is not set for the month - year {} !", DateUtil.getMonthYearFromDate(startDate));
			return 0;
		}

		float ratingSum = 0;

		for (LinkedTreeMap reviewItem : reviewsList) {

			ratingSum += Float.parseFloat(reviewItem.get(RATING_ITEM_KEY_NAME).toString());

		}

		float averageRating = ratingSum / reviewsList.size();

		return averageRating;
	}


	//get monthly revenue for past 12 months
	public static Float getAnnualRevenueByProductFor12Months(APIConnection send, String accountID, String productID, String startDate)
	{
		AppAnnieMonthlyData appAnnieMonthlyData;
		float revenue = 0;
		String date = startDate;

		String monthYearFromDate = DateUtil.getMonthYearFromDate(startDate);
//		logger.debug(" getAnnualRevenueByProductFor12Months(): ------------------Calculating Net MRR for the month {} ", monthYearFromDate);

		//iterate through past 12 months
		for(int i=1; i <= 12 ; i++)
		{
			appAnnieMonthlyData = getMonthlyRevenueByProduct(send, accountID, productID, DateUtil.getMonthStartDate(date), DateUtil.getMonthEndDate(date));

//			logger.debug("getAnnualRevenueByProductFor12Months(): Month {}  ", monthYearFromDate );

			if(appAnnieMonthlyData != null){
				if(i==1){
					revenue = appAnnieMonthlyData.getMonthlyRev();
//					logger.debug("getAnnualRevenueByProductFor12Months(): monthly Rev  {}  ", appAnnieMonthlyData.getMonthlyRev() );
				}

				revenue += appAnnieMonthlyData.getYearlyRevDivBy12();
//				logger.debug("getAnnualRevenueByProductFor12Months(): monthly Rev  {}  ", appAnnieMonthlyData.getYearlyRevDivBy12() );
			}
			else
			{
				logger.error(" getAnnualRevenueByProductFor12Months() :  Monthly data is null for the month-year {} Invalid Revenue..", monthYearFromDate);
			}
			date = DateUtil.addMonth(startDate, -i);
		}

		//Avoid 429 error from http call.
		try {
			Thread.sleep(THREAD_SLEEP_TIME);
		} catch (InterruptedException e) {
			logger.error(" getAnnualRevenueByProductFor12Months() : Exception while thread sleep");
		}

		logger.debug(" getAnnualRevenueByProductFor12Months()------------------End of Calculating Net MRR for the month {} ", monthYearFromDate);
		return revenue;
	}


	//Get revenue  of the entire month for specific product from AppAnnie
    public static AppAnnieMonthlyData getMonthlyRevenueByProduct(APIConnection send, String accountID, String productID, String startDate, String endDate) {
        // get data from appAnnie : iap+date
        String breakdown = "iap+date";

        ArrayList<LinkedTreeMap> salesList = getSalesList(send, accountID, productID, breakdown, startDate, endDate, PRODUCT_SALES_IAP_KEY_NAME);

        AppAnnieMonthlyData productMonthlyData = new AppAnnieMonthlyData();

        float salesRev = 0;
        float refundRev = 0;

        float revenue;
        double netSales;

        if (salesList == null) {
            logger.error(" getMonthlyRevenueByProduct() : Sales List is null! Monthly Revenue is not set for the month - year {} !", DateUtil.getMonthYearFromDate(startDate));
            return null;
        }

        for (LinkedTreeMap iapSalesItem : salesList) {

            if (((LinkedTreeMap) iapSalesItem.get("revenue")).get("sales") != null) {
                salesRev = Float.parseFloat((String) ((LinkedTreeMap) iapSalesItem.get("revenue")).get("sales"));
            }

            if (((LinkedTreeMap) iapSalesItem.get("revenue")).get("refunds") != null) {
                refundRev = Float.parseFloat((String) ((LinkedTreeMap) iapSalesItem.get("revenue")).get("refunds"));
            }

            revenue = salesRev + refundRev; // refund value is negative

            if (iapSalesItem.get("iap").equals("Pro1Mo") || iapSalesItem.get("iap").equals("pro1month")) //TODO:vatsalya Add windows specific plan "key" for subscription
            {
                productMonthlyData.setMonthlyRev(productMonthlyData.getMonthlyRev() + revenue);
            }
            if (iapSalesItem.get("iap").equals("Pro1Y") || iapSalesItem.get("iap").equals("pro1year")) {
                productMonthlyData.setYearlyRev(productMonthlyData.getYearlyRev() + revenue);
            }
            if (iapSalesItem.get("iap").equals("ProPlus1Y") || iapSalesItem.get("iap").equals("proplus1year")) {
                productMonthlyData.setProYearlyRev(productMonthlyData.getProYearlyRev() + revenue);
            }
        }

        float yearlyDivBy12 = (productMonthlyData.getYearlyRev() + productMonthlyData.getProYearlyRev()) / 12;

        productMonthlyData.setYearlyRevDivBy12(yearlyDivBy12);
        productMonthlyData.setMonthYear(endDate);

        return productMonthlyData;
    }

    /**
     * Description : Set the total Monthly Revenue.
     *
     * @param appAnnieDataForTheMonth
     */
    public static void setMonthlyRevenue(AppAnnieMonthlyData appAnnieDataForTheMonth) {
        Vector<AppAnnieMonthlyData> monthlyDataVector = helper.getAppAnnieMonthlyData();
        boolean isDataForMonthExist = false;

        for (AppAnnieMonthlyData appAnnieMonthlyData : monthlyDataVector) {
            if (appAnnieMonthlyData.getMonthYear().equalsIgnoreCase(appAnnieDataForTheMonth.getMonthYear())) {
                appAnnieMonthlyData.setProYearlyRev(appAnnieDataForTheMonth.getProYearlyRev() + appAnnieMonthlyData.getProYearlyRev());
                appAnnieMonthlyData.setMonthlyRev(appAnnieDataForTheMonth.getMonthlyRev() + appAnnieMonthlyData.getMonthlyRev());
                appAnnieMonthlyData.setYearlyRev(appAnnieDataForTheMonth.getYearlyRev() + appAnnieMonthlyData.getYearlyRev());
                isDataForMonthExist = true;
                break;
            }
        }

        if (!isDataForMonthExist) {
            monthlyDataVector.add(appAnnieDataForTheMonth);
        }
    }
}
