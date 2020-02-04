package com.adsk.analytics.cache;

import com.adsk.analytics.utils.CacheUtils;
import com.adsk.analytics.utils.ICacheHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aenugav on 7/8/2015.
 */
public class RedisCacheDataAccessUtil {

    private static final String MONTHLY_SUBSCRIBERS_DATA_CACHE_KEY = "subscribers";
    private static final Logger logger = LoggerFactory.getLogger(RedisCacheDataAccessUtil.class);
    private final int EXPIRY_IN_SECONDS = 13 * 60 * 60 * 24 * 30;

    public RedisCacheDataAccessUtil(ICacheHandler cacheHandler) {
        CacheUtils.setHandler(cacheHandler);
    }

    public void saveSubscribersByMonthYear(String monthYear, int subscribersCount) {
        String key = "";

        try {
            key = generateMonthlySubscribersDataCacheKey(monthYear);
            String subscribersCountStr = String.valueOf(subscribersCount);
            boolean isCacheSet = CacheUtils.set(key, EXPIRY_IN_SECONDS, subscribersCountStr);

            if(isCacheSet){
                logger.info("saveSubscribersByMonthYear() : saved subscribersCountStr {} to  key {}", subscribersCountStr, key);
            }
        } catch (Exception e) {
            logger.error("saveSubscribersByMonthYear failed for key: {}", key, e);
        }
    }

    public String getSubscribersCountByMonthYear(String monthYear) {
        String key = generateMonthlySubscribersDataCacheKey(monthYear);
        String subscribersCount = "";

        if(CacheUtils.get(key)!=null){
            subscribersCount  = String.valueOf(CacheUtils.get(key));
            logger.info("getSubscribersCountByMonthYear() : got subscribersCountStr {} from  key {}", subscribersCount, key);
        }else{
            return null;
        }

        return subscribersCount;
    }

    private String generateMonthlySubscribersDataCacheKey(String monthYear) {
        return (new StringBuilder(MONTHLY_SUBSCRIBERS_DATA_CACHE_KEY).append(String.valueOf(":monthYear:")).append(monthYear).toString());
    }
}
