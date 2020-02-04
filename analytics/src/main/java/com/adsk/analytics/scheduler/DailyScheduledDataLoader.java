package com.adsk.analytics.scheduler;

import com.adsk.analytics.appannie.AppAnnieAPIConnection;
import com.adsk.analytics.geckoboard.GeckoboardHelper;
import com.adsk.analytics.geckoboard.utils.AnalyticsDataAccessUtil;
import com.adsk.analytics.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * Created by aenugav on 10/2/2014.
 */

public class DailyScheduledDataLoader {

    private static final Logger logger = LoggerFactory.getLogger(DailyScheduledDataLoader.class);
    private static GeckoboardHelper helper = GeckoboardHelper.Instance();
//    private SubscriptionsUtils subscriptionsUtils;
    private AppAnnieAPIConnection appAnnieAPIConnection;

//    public DailyScheduledDataLoader(SubscriptionsUtils subscriptionsUtils, AppAnnieAPIConnection appAnnieAPIConnection) {
//        this.subscriptionsUtils = subscriptionsUtils;
//        this.appAnnieAPIConnection = appAnnieAPIConnection;
//    }

    public void loadData() {
        logger.debug("Loading Data at {} ", Calendar.getInstance().getTime());

//        AnalyticsDataAccessUtil.setSumOfAllUsers(subscriptionsUtils);
//        AnalyticsDataAccessUtil.setupAllUserTypesPayingAccounts(subscriptionsUtils);

        String yesterday = DateUtil.getDateRangeByInterval(helper.YESTERDAY);
        String startDateForDownloads = DateUtil.getDateRangeByInterval(helper.LAST_6_MONTHS);

        clearDownloadsData();

        AnalyticsDataAccessUtil.setDownloadsFromAppAnnie(appAnnieAPIConnection, startDateForDownloads, yesterday, false);

        AnalyticsDataAccessUtil.setDownloadsFromAppAnnie(appAnnieAPIConnection, helper.START_DATE_JAN_2014, yesterday, true);

        helper.getAppAnnieDailyData().clear();
        helper.getAppAnnieDailyDataHashMap().clear();

        //get last six months daily sales from App Annie
        AnalyticsDataAccessUtil.setDailyProSalesFromAppAnnie(appAnnieAPIConnection, DateUtil.getDateRangeByInterval(Calendar.DATE, (helper.LAST_6_MONTHS - 1)), yesterday, helper.SIX_MONTHS);

        //set accumulated revenue from App Annie
        AnalyticsDataAccessUtil.setAccumulatedRevenueFromAppAnnie(appAnnieAPIConnection);
    }

    private void clearDownloadsData() {
        helper.getDownloadSummary().clear();
        helper.setiOSDownloads(helper.iOS_DOWNLOADS_PRIOR_TO_JAN_2014);
        helper.setAndroidDownloads(helper.GOOGLEPLAY_DOWNLOADS_PRIOR_TO_JAN_2014);
        helper.setWindowsDownloads(0);
        helper.setTotalDownloads(helper.TOTAL_DOWNLOADS_PRIOR_TO_JAN_2014);
    }

}
