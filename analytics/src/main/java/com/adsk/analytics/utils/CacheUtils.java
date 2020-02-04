package com.adsk.analytics.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CacheUtils {

    private static ICacheHandler cache = null;
    private static Logger logger = LoggerFactory.getLogger(CacheUtils.class);

    public static void setHandler(ICacheHandler handler)
    {
        cache = handler;
    }

    public static boolean exists(String key)
    {
        if (cache != null) {
            try {
                return cache.exists(key);
            }
            catch (Exception e) {
                logger.error("CacheUtil.exists: ", e);
            }
        }

        return false;
    }

    public static Long getExpirationTime(String key)
    {
        if (cache != null) {
            try {
                return cache.getExpirationTime(key);
            }
            catch (Exception e) {
                logger.error("CacheUtil.exists: ", e);
            }
        }

        return null;
    }

    public static boolean set(String key, String value)
    {
        if (cache != null) {
            try {
                cache.set(key, value);
                return true;
            }
            catch (Exception e) {
                logger.error("CacheUtil.set: ", e);
                return false;
            }
        }

        return false;
    }

    public static boolean set(String key, int expirationInSeconds, String value)
    {
        if (cache != null) {
            try {
                cache.set(key, expirationInSeconds, value);
                return true;
            }
            catch (Exception e) {
                logger.error("CacheUtils.set: ", e);
                return false;
            }
        }

        return false;
    }

    public static Object get(String key)
    {
        try {
            if (cache != null) {
                return cache.get(key);
            }
        }
        catch (Exception e) {
            logger.error("CacheUtil.get: ", e);
        }

        return null;
    }

    public static boolean set(String cacheId, String key, String value)
    {
        if (cache != null) {
            try {
                cache.set(getCacheKey(cacheId, key), value);
                return true;
            }
            catch (Exception e) {
                logger.error("CacheUtil.set: ", e);
                return false;
            }
        }

        return false;
    }

    public static boolean set(String cacheId, String key, int expirationInSeconds, String value)
    {
        if (cache != null) {
            try {
                cache.set(getCacheKey(cacheId, key), expirationInSeconds, value);
                return true;
            }
            catch (Exception e) {
                logger.error("CacheUtils.set: ", e);
                return false;
            }
        }

        return false;
    }

    public static Object get(String cacheId, String key)
    {
        try {
            if (cache != null) {
                return cache.get(getCacheKey(cacheId, key));
            }
        }
        catch (Exception e) {
            logger.error("CacheUtil.get: ", e);
        }

        return null;
    }

    public static boolean addList(String key, List<String> value)
    {
        if (cache != null) {
            try {
                cache.addList(key, value);
                return true;
            }
            catch (Exception e) {
                logger.error("CacheUtil.addList: ", e);
            }
        }

        return false;
    }

    public static boolean addList(String key, int expirationInSeconds, List<String> value)
    {
        if (cache != null) {
            try {
                cache.addList(key, expirationInSeconds, value);
                return true;
            }
            catch (Exception e) {
                logger.error("CacheUtil.addList: ", e);
            }
        }

        return false;
    }

    public static List<String> getList(String key)
    {
        if (cache != null) {
            try {
                return cache.getList(key);
            }
            catch (Exception e) {
                logger.error("CacheUtils.getList: ", e);
            }
        }

        return null;
    }

    public static List<String> getList(String key, int range)
    {
        if (cache != null) {
            try {
                return cache.getList(key, range);
            }
            catch (Exception e) {
                logger.error("CacheUtils.getList: ", e);
            }
        }

        return null;
    }

    public static boolean addSet(String key, String... values)
    {
        if (cache != null) {
            try {
                cache.addSet(key, values);
                return true;
            }
            catch (Exception e) {
                logger.error("CacheUtil.addSet: ", e);
            }
        }

        return false;
    }

    public static boolean addSet(String key, int expirationInSeconds, String... values)
    {
        if (cache != null) {
            try {
                cache.addSet(key, expirationInSeconds, values);
                return true;
            }
            catch (Exception e) {
                logger.error("CacheUtil.addSet: ", e);
            }
        }

        return false;
    }

    public static Set<String> getSet(String key)
    {
        if (cache != null) {
            try {
                return cache.getSet(key);
            }
            catch (Exception e) {
                logger.error("CacheUtils.getSet: ", e);
            }
        }

        return null;
    }

    public static boolean addHash(String key, String field, String value)
    {
        if (cache != null) {
            try {
                cache.addHash(key, field, value);

                return true;
            }
            catch (Exception e) {
                logger.error("CacheUtil.addHash: ", e);
            }
        }

        return false;
    }

    public static boolean addHash(String key, int expirationInSeconds, String field, String value)
    {
        if (cache != null) {
            try {
                cache.addHash(key, expirationInSeconds, field, value);
                return true;
            }
            catch (Exception e) {
                logger.error("CacheUtil.addHash: ", e);
            }
        }

        return false;
    }

    public static boolean addHashMap(String key, Map<String, String> value)
    {
        if (cache != null) {
            try {
                cache.addHashMap(key, value);
                return true;
            }
            catch (Exception e) {
                logger.error("CacheUtil.addHashMap: ", e);
            }
        }

        return false;
    }

    public static boolean addHashMap(String key, int expirationInSeconds, Map<String, String> value)
    {
        if (cache != null) {
            try {
                cache.addHashMap(key, expirationInSeconds, value);
                return true;
            }
            catch (Exception e) {
                logger.error("CacheUtil.addHashMap: ", e);
            }
        }

        return false;
    }

    public static boolean updateHashMap(String oldKey,String newKey, Map<String, String> value)
    {
        if (cache != null) {
            try {
                cache.updateHashMap(oldKey, newKey, value);
                return true;
            }
            catch (Exception e) {
                logger.error("CacheUtil.updateHashMap: ", e);
            }
        }

        return false;
    }

    public static List<String> getHash(String key, String... fields)
    {
        if (cache != null) {
            try {
                return cache.getHash(key, fields);
            }
            catch (Exception e) {
                logger.error("CacheUtils.getHash: ", e);
            }
        }

        return null;
    }

    public static Map<String, String> getHashAll(String key)
    {
        if (cache != null) {
            try {
                return cache.getHashAll(key);
            }
            catch (Exception e) {
                logger.error("CacheUtils.getHashAll: ", e);
            }
        }

        return null;
    }

    public static boolean delete(String key)
    {
        try {
            if (cache != null) {
                cache.delete(key);
                return true;
            }
        }
        catch (Exception e) {
            logger.error("CacheUtils.delete: ", e);
        }

        return false;
    }

    public static boolean delete(String cacheId, String key)
    {
        try {
            if (cache != null) {
                cache.delete(getCacheKey(cacheId, key));
                return true;
            }
        }
        catch (Exception e) {
            logger.error("CacheUtils.delete: ", e);
        }

        return false;
    }

    private static String getCacheKey(String cacheId, String key)
    {
        return cacheId + key;
    }

}
