package com.adsk.analytics.scheduler;

//import com.adsk.acad.core.subscriptions.controller.SubscriptionsUtils;
import com.adsk.analytics.geckoboard.GeckoboardHelper;
import com.adsk.analytics.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.util.Calendar;

/**
 * Created by aenugav on 10/2/2014.
 */

public class ScheduledDataLoader
{

    private static final Logger logger = LoggerFactory.getLogger(ScheduledDataLoader.class);
    public static GeckoboardHelper helper;

//    private SubscriptionsUtils subscriptionsUtils;


//    public ScheduledDataLoader(SubscriptionsUtils subscriptionsUtils)
//    {
//        this.subscriptionsUtils =subscriptionsUtils;
//    }

    public void setUpDBData()
    {
        logger.debug("Loading DB Data at {} ",Calendar.getInstance().getTime());

        helper = GeckoboardHelper.Instance();
        Date startDate = DateUtil.getDateDiffWithInterval(helper.LAST_30_DAYS);
        Date  endDate= DateUtil.getDateDiffWithInterval(helper.YESTERDAY);


        //Not in use Currently. Unnecessary data load. Commenting for now.
//        AnalyticsDataAccessUtil.setDailySubscribersbyAppIDChartVector(subscriptionsUtils, startDate, endDate);
//        AnalyticsDataAccessUtil.setDailySubscribersByProductIDChartVector(subscriptionsUtils, startDate, endDate);

    }

}
