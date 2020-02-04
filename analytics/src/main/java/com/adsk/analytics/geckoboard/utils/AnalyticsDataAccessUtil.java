package com.adsk.analytics.geckoboard.utils;

import com.adsk.analytics.appannie.AppAnnieAPIConnection;
import com.adsk.analytics.appannie.AppAnnieHelper;
import com.adsk.analytics.cache.RedisCacheDataAccessUtil;
import com.adsk.analytics.geckoboard.GeckoboardHelper;
import com.adsk.analytics.model.*;
import com.adsk.analytics.utils.DateUtil;
import com.google.gson.internal.LinkedTreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by aenugav on 9/22/2014.
 */
public abstract class AnalyticsDataAccessUtil {

    private static GeckoboardHelper helper = GeckoboardHelper.Instance();
    public final static int QUERY_SUBSCRIPTION_APP_TYPE_IOS = 1;
    public final static int QUERY_SUBSCRIPTION_APP_TYPE_ANDROID = 2;

    public final static int SUBSCRIBERS_PRIOR_TO_JUNE2015 = 20000;
    public final static String IAP_PRO_MONTHLY_IOS = "Pro1Mo";
    public final static String IAP_PRO_MONTHLY_ANDROID = "pro1month";
    public final static String IAP_PRO_YEARLY_IOS = "Pro1Y";
    public final static String IAP_PRO_YEARLY_ANDROID = "pro1year";
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsDataAccessUtil.class);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");

    private static AppAnnieHelper appAnnieHelper;


    /**
     * Description : Set the daily sales for the period falling between start and end date.
     *
     * @param send
     * @param startDate
     * @param endDate
     */
    public static void setDailyProSalesFromAppAnnie(AppAnnieAPIConnection send, String startDate,
                                                    String endDate, int intervalInMonths) {
        // get data from appAnnie : date
        String breakdown = "date+iap";
        appAnnieHelper = send.getAppAnnieHelper();
        String twoMonthsStartDate = startDate;
        String twoMonthsEndDate = DateUtil.getDateRangeByInterval(startDate, helper.SIXTY_DAYS);
        int loopCount = (int) (intervalInMonths / 2); //The Appannie supports only 60 days as date range for date+iap queries

        helper.setAppAnnieDailySalesDataStartDate(startDate);

        for (int i = 0; i < loopCount; i++) {

            //ios
            ArrayList<LinkedTreeMap> iosSalesList = send.getSalesList(send, appAnnieHelper.getiOSAccountID(), appAnnieHelper.getiOSProductID(), breakdown, twoMonthsStartDate, twoMonthsEndDate, send.PRODUCT_SALES_IAP_KEY_NAME);

            //GooglePlay
            ArrayList<LinkedTreeMap> googlePlaySalesList = send.getSalesList(send, appAnnieHelper.getGooglePlayAccountID(), appAnnieHelper.getGooglePlayProductID(), breakdown, twoMonthsStartDate, twoMonthsEndDate, send.PRODUCT_SALES_IAP_KEY_NAME);

            if (iosSalesList == null || googlePlaySalesList == null) {
                logger.error("setDailyProMonthlySalesFromAppAnnie() : Sales List is null. Download Data load failed . ");
                return;
            }

            String date = twoMonthsStartDate;

            Iterator<LinkedTreeMap> iosSalesListIter = iosSalesList.iterator();
            Iterator<LinkedTreeMap> googlePlaySalesListIter = googlePlaySalesList.iterator();
            
            AppAnnieDailySalesListPair appAnnieDailySalesListPair = new AppAnnieDailySalesListPair();
            appAnnieDailySalesListPair.setAndroidSalesItem(googlePlaySalesListIter.next());
            appAnnieDailySalesListPair.setIosSalesItem(iosSalesListIter.next());

            while (iosSalesListIter.hasNext() || googlePlaySalesListIter.hasNext()) {
                setTwoMonthDailySalesData(date, twoMonthsEndDate, iosSalesListIter,
                        googlePlaySalesListIter, appAnnieDailySalesListPair);
            }

            twoMonthsStartDate = DateUtil.getDateRangeByInterval(twoMonthsEndDate, 1);
            twoMonthsEndDate = DateUtil.getDateRangeByInterval(twoMonthsStartDate, helper.TWO_MONTHS_IN_DAYS);

            if (DateUtil.getDateDifferenceInDays(endDate, twoMonthsEndDate) > 0) {
                twoMonthsEndDate = endDate;
            }

        }

    }

    /**
     * Description : Set the daily downloads for past 6 months.
     * @param send
     * @param startDate
     * @param endDate
     */
    public static void setDownloadsFromAppAnnie(AppAnnieAPIConnection send, String startDate, String endDate, boolean isTotalDownloads) {
        // get data from appAnnie : date
        String breakdown = "date";
        DecimalFormat df = new DecimalFormat("#.##");
        appAnnieHelper = send.getAppAnnieHelper();
        //ios
        ArrayList<LinkedTreeMap> iosSalesList = send.getSalesList(send, appAnnieHelper.getiOSAccountID(), appAnnieHelper.getiOSProductID(), breakdown, startDate, endDate, send.PRODUCT_SALES_KEY_NAME);

        //GooglePlay
        ArrayList<LinkedTreeMap> googlePlaySalesList = send.getSalesList(send, appAnnieHelper.getGooglePlayAccountID(), appAnnieHelper.getGooglePlayProductID(), breakdown, startDate, endDate, send.PRODUCT_SALES_KEY_NAME);

        //windows
        ArrayList<LinkedTreeMap> windowsSalesList = send.getSalesList(send, appAnnieHelper.getWindowsAccountID(), appAnnieHelper.getWindowsProductID(), breakdown, startDate, endDate, send.PRODUCT_SALES_KEY_NAME);


        if (googlePlaySalesList == null || iosSalesList == null || windowsSalesList == null) {
            logger.error(" setDownloadsFromAppAnnie() : Sales List is null. Download Data load failed . ");
            return;
        }

        String date = startDate;
        int iosListCount = 0;
        int androidListCount = 0;
        int windowsListCount = 0;

        for (int dateCount = 1; !date.equals(endDate); dateCount++) {
            DownloadSummary downloadSummary = new DownloadSummary();
            double iosDownloads = 0;
            double gplayDownloads = 0;
            double windowDownloads = 0;
            double total = 0;

            downloadSummary.setDate(date);

            if (iosListCount < iosSalesList.size()) {
                LinkedTreeMap iosSalesItem = iosSalesList.get(iosListCount);

                if (iosSalesItem.get("date").equals(date)) {
                    iosDownloads = getNumberOfDownloads(iosSalesItem);
                    total += iosDownloads;
                    iosListCount++;
                }
            }
            if (androidListCount < googlePlaySalesList.size()) {
                LinkedTreeMap gplaySalesItem = googlePlaySalesList.get(androidListCount);

                if (gplaySalesItem.get("date").equals(date)) {
                    gplayDownloads = getNumberOfDownloads(gplaySalesItem);
                    total += gplayDownloads;
                    androidListCount++;
                }
            }
            if (windowsListCount < windowsSalesList.size()) {
                LinkedTreeMap windowsSalesItem = windowsSalesList.get(windowsListCount);

                if (windowsSalesItem.get("date").equals(date)) {
                    windowDownloads = getNumberOfDownloads(windowsSalesItem);
                    total += windowDownloads;
                    windowsListCount++;
                }
            }

            downloadSummary.setiOSDownload(df.format(iosDownloads));
            downloadSummary.setAndroidDownload(df.format(gplayDownloads));
            downloadSummary.setWindowsDownload(df.format(windowDownloads));
            downloadSummary.setTotalDownloads(df.format(total));

            date = DateUtil.getDateRangeByInterval(startDate, dateCount);

            if (isTotalDownloads) {
                helper.setiOSDownloads(Double.parseDouble(df.format(helper.getiOSDownloads() + iosDownloads)));
                helper.setAndroidDownloads(Double.parseDouble(df.format(helper.getAndroidDownloads() + gplayDownloads)));
                helper.setWindowsDownloads(Double.parseDouble(df.format(helper.getWindowsDownloads() + windowDownloads)));
                helper.setTotalDownloads(Double.parseDouble(df.format(helper.getTotalDownloads() + total)));
            } else {
                helper.getDownloadSummary().add(downloadSummary);
            }
        }

        logger.debug("setDownloadsFromAppAnnie(): Completed Setting the Downloads by productID ");
    }

    private static double getNumberOfDownloads(LinkedTreeMap salesItem) {
        return (double) ((LinkedTreeMap) ((LinkedTreeMap) salesItem.get("units")).get("product")).get("downloads");
    }

    private static double getNumberOfSales(LinkedTreeMap salesItem) {
        return (double) ((LinkedTreeMap) salesItem.get("units")).get("sales");
    }

    private static double getAccumulatedRevenue(LinkedTreeMap salesItem) {
        return Double.parseDouble(((LinkedTreeMap) ((LinkedTreeMap) salesItem.get("revenue")).get("iap")).get("sales").toString());
    }

    private static double getAccumulatedRefund(LinkedTreeMap salesItem) {
        return Double.parseDouble(((LinkedTreeMap) ((LinkedTreeMap) salesItem.get("revenue")).get("iap")).get("refunds").toString());
    }

    private static double getDailySalesFromSalesList(LinkedTreeMap salesItem, String iapString) {

        if (salesItem.get("iap").equals(iapString)) {
            return getNumberOfSales(salesItem);
        }

        return 0;
    }

    private static void setTwoMonthDailySalesData(String twoMonthsStartDate,
        String twoMonthsEndDate, Iterator<LinkedTreeMap> iosSalesListIter,
        Iterator<LinkedTreeMap> androidSalesListIter,
        AppAnnieDailySalesListPair appAnnieDailySalesListPair) {

        String date = twoMonthsStartDate;

        for (int dateCount = 1; !date.equals(DateUtil.getDateRangeByInterval(twoMonthsEndDate, 1)); dateCount++) {
            AppAnnieDailyData appAnnieDailyData = new AppAnnieDailyData();
            appAnnieDailyData.setDate(date);
            LinkedTreeMap iosSalesItem = appAnnieDailySalesListPair.getIosSalesItem();
            String iosSalesItemDate = iosSalesItem.get("date").toString();

            if (iosSalesItemDate.equals(date)) {
                appAnnieDailyData.setProMonthlyIOSSubscribers(
                    getDailySalesFromSalesList(iosSalesItem, IAP_PRO_MONTHLY_IOS));
                appAnnieDailyData.setProYearlyIOSSubscribers(
                    getDailySalesFromSalesList(iosSalesItem, IAP_PRO_YEARLY_IOS));

                if (iosSalesListIter.hasNext()) {
                    appAnnieDailySalesListPair.setIosSalesItem(iosSalesListIter.next());
                }
            }

            LinkedTreeMap googlePlaySalesItem = appAnnieDailySalesListPair.getAndroidSalesItem();
            String googlePlaySalesItemDate = googlePlaySalesItem.get("date").toString();

            if (googlePlaySalesItemDate.equals(date)) {
                appAnnieDailyData.setProMonthlyAndroidSubscribers(
                    getDailySalesFromSalesList(googlePlaySalesItem, IAP_PRO_MONTHLY_ANDROID));
                appAnnieDailyData.setProYearlyAndroidSubscribers(
                    getDailySalesFromSalesList(googlePlaySalesItem, IAP_PRO_YEARLY_ANDROID));

                if (androidSalesListIter.hasNext()) {
                    appAnnieDailySalesListPair.setAndroidSalesItem(androidSalesListIter.next());
                }
            }

            AppAnnieDailyData existingAppAnnieData = helper.getAppAnnieDailyDataHashMap().get(date);

            if (existingAppAnnieData != null) {
                appAnnieDailyData.updateAppAnnieDailyData(existingAppAnnieData);
            }

            helper.getAppAnnieDailyDataHashMap().put(date, appAnnieDailyData);
            date = DateUtil.getDateRangeByInterval(twoMonthsStartDate, dateCount);
        }

    }

    /**
     * Description : Set the Monthly Revenue using AppANnie API for the past 12 months.
     *
     * @param send
     * @param startDate
     */
    public static void setMonthlyRevenueFromAppAnnie(AppAnnieAPIConnection send, String startDate) {

        String date = startDate;
        appAnnieHelper = send.getAppAnnieHelper();
        helper.getAppAnnieMonthlyData().clear();

        for(int i=1; i <= 12 ; i++) {
            send.setMonthlyRevenue(send.getMonthlyRevenueByProduct(send, appAnnieHelper.getiOSAccountID(), appAnnieHelper.getiOSProductID(), DateUtil.getMonthStartDate(date), DateUtil.getMonthEndDate(date)));
            send.setMonthlyRevenue(send.getMonthlyRevenueByProduct(send, appAnnieHelper.getGooglePlayAccountID(), appAnnieHelper.getGooglePlayProductID(), DateUtil.getMonthStartDate(date), DateUtil.getMonthEndDate(date)));
            date = DateUtil.addMonth(date, 1);
        }
    }

    /**
     * Description : Set the total accumulated revenue.
     *
     * @param send
     */
    public static void setAccumulatedRevenueFromAppAnnie(AppAnnieAPIConnection send) {
        appAnnieHelper = send.getAppAnnieHelper();

        //ios
        ArrayList<LinkedTreeMap> iosSalesList = send.getSalesList(send, appAnnieHelper.getiOSAccountID(), appAnnieHelper.getiOSProductID(), null, null, null, send.PRODUCT_SALES_KEY_NAME);

        //GooglePlay
        ArrayList<LinkedTreeMap> googlePlaySalesList = send.getSalesList(send, appAnnieHelper.getGooglePlayAccountID(), appAnnieHelper.getGooglePlayProductID(), null, null, null, send.PRODUCT_SALES_KEY_NAME);

        //windows
        ArrayList<LinkedTreeMap> windowsSalesList = send.getSalesList(send, appAnnieHelper.getWindowsAccountID(), appAnnieHelper.getWindowsProductID(), null, null, null, send.PRODUCT_SALES_KEY_NAME);

        if (googlePlaySalesList == null || iosSalesList == null || windowsSalesList == null) {
            logger.error(" setAccumulatedRevenueFromAppAnnie() : Sales List is null. Accumulated Revenue Data load failed . ");
            return;
        }

        if (iosSalesList.size() != 1 || googlePlaySalesList.size() != 1 || windowsSalesList.size() != 1) {
            logger.error(" setAccumulatedRevenueFromAppAnnie() : Sales List size should be 1. Accumulated Revenue Data load failed . ");
            return;
        }

        LinkedTreeMap iosSalesListItem = iosSalesList.get(0);
        LinkedTreeMap googlePlaySalesListItem = googlePlaySalesList.get(0);
        LinkedTreeMap windowsSalesListItem = windowsSalesList.get(0);

        try {
            helper.setTotalAccumulatedRevenue(Math.round((getAccumulatedRevenue(iosSalesListItem) - getAccumulatedRefund(iosSalesListItem)) +
                    (getAccumulatedRevenue(googlePlaySalesListItem) - getAccumulatedRefund(googlePlaySalesListItem)) +
                    (getAccumulatedRevenue(windowsSalesListItem) - getAccumulatedRevenue(windowsSalesListItem))));
        } catch (Exception e) {
            logger.error("setAccumulatedRevenueFromAppAnnie(): failed to set accumulated revenue: {}", e);
            return;
        }

        logger.info("setAccumulatedRevenueFromAppAnnie(): Accumulated revenue set successfully: {}", helper.getTotalAccumulatedRevenue());
    }

    public static void setMonthlyProductAverageRating(AppAnnieAPIConnection send, String startDate) {
        clearProductAverageRatingData();
        String date = startDate;

        DecimalFormat df = new DecimalFormat("#.##");

        appAnnieHelper = send.getAppAnnieHelper();
        for (int i = 1; i <= 12; i++) {

            float avgProductRatingsMapiOS = send.getMonthlyAverageRatingByProduct(send, send.PRODUCT_REVIEW_MARKET_IOS, send.getAppAnnieHelper().getiOSProductID(), DateUtil.getMonthStartDate(date), DateUtil.getMonthEndDate(date));

            float avgProductAvgRatingsMapAndroid = send.getMonthlyAverageRatingByProduct(send, send.PRODUCT_REVIEW_MARKET_GOOGLE_PLAY, send.getAppAnnieHelper().getGooglePlayProductID(), DateUtil.getMonthStartDate(date), DateUtil.getMonthEndDate(date));

            helper.getAndroidAverageRating().add(df.format(avgProductAvgRatingsMapAndroid));
            helper.getiOSAverageRating().add(df.format(avgProductRatingsMapiOS));
            helper.getMonthYear().add(DateUtil.getMonthYearFromDate(date));

            date = DateUtil.addMonth(startDate, i);

        }
    }

    public static void clearProductAverageRatingData() {

        helper.getAndroidAverageRating().clear();
        helper.getiOSAverageRating().clear();
        helper.getMonthYear().clear();

    }

}
