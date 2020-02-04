package com.adsk.analytics.scheduler;

import com.adsk.analytics.utils.RedisCacheHandler;
import com.adsk.analytics.appannie.AppAnnieAPIConnection;
import com.adsk.analytics.cache.RedisCacheDataAccessUtil;
import com.adsk.analytics.geckoboard.GeckoboardHelper;
import com.adsk.analytics.geckoboard.utils.AnalyticsDataAccessUtil;
import com.adsk.analytics.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * Created by aenugav on 6/11/2015.
 */
public class MonthlyScheduledDataLoader {

    private static final Logger logger = LoggerFactory.getLogger(DailyScheduledDataLoader.class);
    private static GeckoboardHelper helper = GeckoboardHelper.Instance();
//    private AppAnnieAPIConnection appAnnieAPIConnection;
//    private SubscriptionsUtils subscriptionsUtils;

//    public MonthlyScheduledDataLoader(AppAnnieAPIConnection appAnnieAPIConnection)
//    {
////        this.appAnnieAPIConnection = appAnnieAPIConnection;
//    }

//    public SubscriptionsUtils getSubscriptionsUtils() {
//        return subscriptionsUtils;
//    }
//
//    public void setSubscriptionsUtils(SubscriptionsUtils subscriptionsUtils) {
//        this.subscriptionsUtils = subscriptionsUtils;
//    }

    public void loadData()
    {
        logger.debug("Loading Data at {} ", Calendar.getInstance().getTime());

        String currentDate = DateUtil.getCurrentDate();

        String startDate = DateUtil.addMonth(currentDate, helper.LAST_12_MONTHS);

        loadSubscribersByMonYearOnCache(currentDate);

//        AnalyticsDataAccessUtil.setMRRandARPUFromAppAnnie(appAnnieAPIConnection, startDate);
//        AnalyticsDataAccessUtil.setMonthlyRevenueFromAppAnnie(appAnnieAPIConnection, startDate);
//        AnalyticsDataAccessUtil.setMonthlyProductAverageRating(appAnnieAPIConnection, startDate);
    }


    public void loadSubscribersByMonYearOnCache(String currentDate){

//        AnalyticsDataAccessUtil.setupAllUserTypesPayingAccounts(subscriptionsUtils);

        RedisCacheHandler redisCacheHandler = RedisCacheHandler.getInstance();

        RedisCacheDataAccessUtil redisCacheDataAccessUtil = new RedisCacheDataAccessUtil(redisCacheHandler);

        String monthYear = DateUtil.getMonthYearFromDate(currentDate);

        int subscribersTotalCount = helper.getProPlusUsers()+helper.getProUsers();

        if (redisCacheDataAccessUtil.getSubscribersCountByMonthYear(monthYear) == null) {
            redisCacheDataAccessUtil.saveSubscribersByMonthYear(monthYear, subscribersTotalCount);
        }
    }
}
