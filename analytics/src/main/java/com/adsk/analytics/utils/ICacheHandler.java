package com.adsk.analytics.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by singha on 11/25/2014.
 */
public interface ICacheHandler {

    public void set(String key, String value) throws CacheException;
    public void set(String key, int expirationInSeconds, String value) throws CacheException;
    public Object get(String key) throws CacheException;

    public void addList(String key, List<String> value) throws CacheException;
    public void addList(String key, int expirationInSeconds, List<String> value) throws CacheException;
    public List<String> getList(String key) throws CacheException;
    public List<String> getList(String key, int range) throws CacheException;

    public void addSet(String key, String... values) throws CacheException;
    public void addSet(String key, int expirationInSeconds, String... values) throws CacheException;
    public Set<String> getSet(String key) throws CacheException;

    public void addHash(String key, String Field, String value) throws CacheException;
    public void addHash(String key, int expirationInSeconds, String Field, String value) throws CacheException;

    public void addHashMap(String key, Map<String, String> value) throws CacheException;
    public void addHashMap(String key, int expirationInSeconds, Map<String, String> value) throws CacheException;

    void updateHashMap(String oldKey, String newKey, Map<String, String> value) throws CacheException;

    public List<String> getHash(String key, String... fields) throws CacheException;
    public Map<String, String> getHashAll(String key) throws CacheException;

    public void delete(String key) throws CacheException;

    boolean exists(String key)throws CacheException;

    long getExpirationTime(String key)throws CacheException;
}
