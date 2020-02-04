package com.adsk.analytics.geckoboard;

import com.adsk.analytics.model.AppAnnieDailyData;
import com.adsk.analytics.model.AppAnnieMonthlyData;
import com.adsk.analytics.model.DownloadSummary;
import com.adsk.analytics.model.MRRSummary;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

/**
 * Created by aenugav on 9/22/2014.
 */
public class GeckoboardHelper
{
    public final static String GECKOBOARD = "geckoboard";
    public final static int SERVICE_TEST = 0;
    public final static String SERVICE_RESULT = "SUCCESS";
    public final static int HIGHCHART_DAILY_NEW_SUBSCRIBERS_BY_APPID = 1;
    public final static int HIGHCHART_MRR = 2;
    public final static int HIGHCHART_DAILY_NEW_SUBSCRIBERS_BY_PRODID = 3;
    public final static int ACCOUNT_TYPE_HIGH_CHART_WIDGET = 4;
    public final static int DOWNLOADS_HIGH_CHART_WIDGET = 5;
    public final static int TOTAL_DOWNLOADS_TEXT_WIDGET = 6;
    public final static int ARPU_HIGH_CHART_WIDGET = 7;
    public final static int DAILY_SALES_MONTHY_HIGH_CHART_WIDGET = 8;
    public final static int DAILY_SALES_YEARLY_HIGH_CHART_WIDGET = 9;
    public final static int MONTHLY_REVENUE_PROPLUS_HIGH_CHART_WIDGET = 10;
    public final static int MONTHLY_REVENUE_PROMONTHLY_HIGH_CHART_WIDGET = 11;
    public final static int MONTHLY_REVENUE_PROYEARLY_HIGH_CHART_WIDGET = 12;
    public final static int TOTAL_REVENUE_TEXT_WIDGET = 13;
    public final static int MONTHLY_AVERAGE_RATING_IOS = 14;
    public final static int MONTHLY_AVERAGE_RATING_ANDROID = 15;
    public final static int BLINKY_DATA = 16;

    public final static double iOS_DOWNLOADS_PRIOR_TO_JAN_2014 = 8463000;
    public final static double GOOGLEPLAY_DOWNLOADS_PRIOR_TO_JAN_2014 = 5811000;
    public final static double TOTAL_DOWNLOADS_PRIOR_TO_JAN_2014 = 14274000;
    public final static String START_DATE_JAN_2014 = "2014-01-01";

    public static final String BASIC_AUTH_USERS_CONFIG = "basic_auth_users_config";
    private Vector<String> dailyNewSubscribersIOS;
    private Vector<String> dailyNewSubscribersAndroid;
    private Vector<String> dailyNewSubscribersTotal;
    private Vector<String> queryDates;
    private Vector<String> monthYear;

    private Vector<String> dailyNewSubscribersMonthly;
    private Vector<String> dailyNewSubscribersYearly;
    private Vector<String> dailyNewSubscribersYearlyPlus;
    private Vector<String> dailyNewSubscribersByProTotal;
    private Vector<String> blinkyClicks;

    private Vector<MRRSummary> mrrSummary;
    private Vector<DownloadSummary> downloadSummary;
    private Vector<AppAnnieDailyData> appAnnieDailyData;
    private Vector<AppAnnieMonthlyData> appAnnieMonthlyData;
    private LinkedHashMap<String, AppAnnieDailyData> appAnnieDailyDataHashMap;
    private String appAnnieDailySalesDataStartDate;

    private double iOSDownloads;
    private double androidDownloads;
    private double windowsDownloads;
    private double totalDownloads;
    private long totalAccumulatedRevenue;

    private Vector<String> iOSAverageRating;
    private Vector<String> androidAverageRating;

    private int proUsers;
    private int proPlusUsers;
    private int regularUsers;

    private int sumOfAllUsers;

    public final static int LAST_30_DAYS = -30;
    public final static int LAST_6_MONTHS = -180;
    public final static int LAST_12_MONTHS = -12;
    public final static int SIXTY_DAYS = 60;
    public final static int YESTERDAY = -1;
    public final static int SIX_MONTHS = 6;
    public final static int TWO_MONTHS_IN_DAYS = 60;

    private static GeckoboardHelper instance = null;
    public final static String GECKO_PASSWORD = "X";

    public static int getBlinkyData() {
        return BLINKY_DATA;
    }

    protected GeckoboardHelper()
    {
        dailyNewSubscribersIOS = new Vector<String>();
        dailyNewSubscribersAndroid = new Vector<String>();
        dailyNewSubscribersTotal =  new Vector<String>();
        dailyNewSubscribersMonthly = new Vector<String>();
        dailyNewSubscribersYearly = new Vector<String>();
        dailyNewSubscribersYearlyPlus =  new Vector<String>();
        dailyNewSubscribersByProTotal =  new Vector<String>();

        blinkyClicks = new Vector<String>();
        queryDates = new Vector<String>();



        iOSDownloads = iOS_DOWNLOADS_PRIOR_TO_JAN_2014;
        androidDownloads = GOOGLEPLAY_DOWNLOADS_PRIOR_TO_JAN_2014;
        totalDownloads = TOTAL_DOWNLOADS_PRIOR_TO_JAN_2014;
        mrrSummary = new Vector<>();
        downloadSummary =  new Vector<>();
        appAnnieMonthlyData = new Vector();

        iOSAverageRating = new Vector<>();
        androidAverageRating = new Vector<>();
        appAnnieDailyData = new Vector();
        appAnnieDailyDataHashMap  = new LinkedHashMap();
        monthYear = new Vector<>();
    }

    public static GeckoboardHelper Instance()
    {
        if(instance == null)
        {
            instance = new GeckoboardHelper();
        }

        return instance;
    }

    public void clearAllSubsByAppIDResultsVectors()
    {
        getDailyNewSubscribersIOS().clear();
        getDailyNewSubscribersAndroid().clear();
        getDailyNewSubscribersTotal().clear();
        getQueryDates().clear();
    }

    public void clearAllSubsByProdIDResultsVectors()
    {

        getDailyNewSubscribersByProTotal().clear();
        getDailyNewSubscribersMonthly().clear();
        getDailyNewSubscribersYearly().clear();
        getDailyNewSubscribersYearlyPlus().clear();
        getQueryDates().clear();
    }

    public Vector<String> getDailyNewSubscribersIOS() {
        return dailyNewSubscribersIOS;
    }

    public void setDailyNewSubscribersIOS(Vector<String> dailyNewSubscribersIOS) {
        this.dailyNewSubscribersIOS = dailyNewSubscribersIOS;
    }

    public Vector<String> getDailyNewSubscribersAndroid() {
        return dailyNewSubscribersAndroid;
    }

    public void setDailyNewSubscribersAndroid(Vector<String> dailyNewSubscribersAndroid) {
        this.dailyNewSubscribersAndroid = dailyNewSubscribersAndroid;
    }

    public Vector<String> getDailyNewSubscribersTotal() {
        return dailyNewSubscribersTotal;
    }

    public void setDailyNewSubscribersTotal(Vector<String> dailyNewSubscribersTotal) {

        this.dailyNewSubscribersTotal = dailyNewSubscribersTotal;
    }

    public Vector<String> getQueryDates() {
        return queryDates;
    }

    public void setQueryDates(Vector<String> queryDates) {
        this.queryDates = queryDates;
    }

    public Vector<String> getDailyNewSubscribersYearlyPlus() {
        return dailyNewSubscribersYearlyPlus;
    }

    public void setDailyNewSubscribersYearlyPlus(Vector<String> dailyNewSubscribersYearlyPlus) {
        this.dailyNewSubscribersYearlyPlus = dailyNewSubscribersYearlyPlus;
    }

    public Vector<String> getDailyNewSubscribersYearly() {
        return dailyNewSubscribersYearly;
    }

    public void setDailyNewSubscribersYearly(Vector<String> dailyNewSubscribersYearly) {
        this.dailyNewSubscribersYearly = dailyNewSubscribersYearly;
    }

    public Vector<String> getDailyNewSubscribersMonthly() {
        return dailyNewSubscribersMonthly;
    }

    public void setDailyNewSubscribersMonthly(Vector<String> dailyNewSubscribersMonthly) {
        this.dailyNewSubscribersMonthly = dailyNewSubscribersMonthly;
    }

    public Vector<String> getDailyNewSubscribersByProTotal() {
        return dailyNewSubscribersByProTotal;
    }

    public void setDailyNewSubscribersByProTotal(Vector<String> dailyNewSubscribersByProTotal) {
        this.dailyNewSubscribersByProTotal = dailyNewSubscribersByProTotal;
    }

    public int getSumOfAllUsers() {
        return sumOfAllUsers;
    }

    public void setSumOfAllUsers(int sumOfAllUsers) {
        this.sumOfAllUsers = sumOfAllUsers;
    }

    public int getProUsers() {
        return proUsers;
    }

    public void setProUsers(int proUsers) {
        this.proUsers = proUsers;
    }

    public int getProPlusUsers() {
        return proPlusUsers;
    }

    public void setProPlusUsers(int proPlusUsers) {
        this.proPlusUsers = proPlusUsers;
    }

    public int getRegularUsers() {
        return regularUsers;
    }

    public void setRegularUsers(int regularUsers) {
        this.regularUsers = regularUsers;
    }

    public Vector<MRRSummary> getMrrSummary() {
        return mrrSummary;
    }

    public void setMrrSummary(Vector<MRRSummary> mrrSummary) {
        this.mrrSummary = mrrSummary;
    }

    public Vector<DownloadSummary> getDownloadSummary() {
        return downloadSummary;
    }

    public void setDownloadSummary(Vector<DownloadSummary> downloadSummary) {
        this.downloadSummary = downloadSummary;
    }

    public double getiOSDownloads() {
        return iOSDownloads;
    }

    public void setiOSDownloads(double iOSDownloads) {
        this.iOSDownloads = iOSDownloads;
    }

    public double getAndroidDownloads() {
        return androidDownloads;
    }

    public void setAndroidDownloads(double androidDownloads) {
        this.androidDownloads = androidDownloads;
    }

    public double getWindowsDownloads() {
        return windowsDownloads;
    }

    public void setWindowsDownloads(double windowsDownloads) {
        this.windowsDownloads = windowsDownloads;
    }

    public double getTotalDownloads() {
        return totalDownloads;
    }

    public void setTotalDownloads(double totalDownloads) {
        this.totalDownloads = totalDownloads;
    }

    public Vector<AppAnnieDailyData> getAppAnnieDailyData() {
        return appAnnieDailyData;
    }

    public void setAppAnnieDailyData(Vector<AppAnnieDailyData> appAnnieDailyData) {
        this.appAnnieDailyData = appAnnieDailyData;
    }

    public HashMap<String, AppAnnieDailyData> getAppAnnieDailyDataHashMap() {
        return appAnnieDailyDataHashMap;
    }

    public String getAppAnnieDailySalesDataStartDate() {
        return appAnnieDailySalesDataStartDate;
    }

    public void setAppAnnieDailySalesDataStartDate(String appAnnieDailySalesDataStartDate) {
        this.appAnnieDailySalesDataStartDate = appAnnieDailySalesDataStartDate;
    }

    public Vector<AppAnnieMonthlyData> getAppAnnieMonthlyData() {
        return appAnnieMonthlyData;
    }

    public void setAppAnnieMonthlyData(Vector<AppAnnieMonthlyData> appAnnieMonthlyData) {
        this.appAnnieMonthlyData = appAnnieMonthlyData;
    }

    public void setTotalAccumulatedRevenue(long totalAccumulatedRevenue) {
        this.totalAccumulatedRevenue = totalAccumulatedRevenue;
    }

    public long getTotalAccumulatedRevenue() {
        return totalAccumulatedRevenue;
    }

    public Vector<String> getiOSAverageRating() {
        return iOSAverageRating;
    }

    public void setiOSAverageRating(Vector<String> iOSAverageRating) {
        this.iOSAverageRating = iOSAverageRating;
    }

    public Vector<String> getAndroidAverageRating() {
        return androidAverageRating;
    }

    public void setAndroidAverageRating(Vector<String> androidAverageRating) {
        this.androidAverageRating = androidAverageRating;
    }

    public Vector<String> getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(Vector<String> monthYear) {
        this.monthYear = monthYear;
    }
}
