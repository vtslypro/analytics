package com.adsk.analytics.utils;


import com.adsk.analytics.utils.redis.JedisPoolConfigWrapper;
import com.adsk.analytics.utils.redis.RedisConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.SafeEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisCacheHandler implements ICacheHandler {
    public static final int DEFAULT_CACHED_EXPIRATION = 60 * 60 * 24 * 30; // seconds in a month
    private static volatile RedisCacheHandler instance;
    private volatile JedisPool jedisPool;
    private static Logger logger = LoggerFactory.getLogger(RedisCacheHandler.class);

    private RedisCacheHandler() {
    }

    public static void setInstance(RedisCacheHandler redisCacheHandler) {
        instance = redisCacheHandler;
    }

    public void init() {
        instance = this;
    }

    public static RedisCacheHandler getInstance() {
        RedisCacheHandler result = instance; // http://en.wikipedia.org/wiki/Double-checked_locking#Usage_in_Java. Using result var improves performance by 25%

        if (result == null) {
            synchronized (RedisCacheHandler.class) {
                result = instance;

                if (result == null) {
                    result = instance = new RedisCacheHandler();
                }
            }
        }

        return result;
    }

    @Override
    public void set(String key, String value) throws CacheException {
        set(key, DEFAULT_CACHED_EXPIRATION, value);
    }

    @Override
    public void set(String key, int expiration, String value) throws CacheException {
        if (value == null) {
            return;
        }

        try (Jedis jedis = getResource()) {
            jedis.setex(key, expiration, value);
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:set Exception: ", e);
            throw new CacheException(e);
        }
    }

    @Override
    public Object get(String key) throws CacheException {
        if (key == null) {
            return null;
        }

        try (Jedis jedis = getResource()) {
            return jedis.get(key);
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:get Exception: ", e);
            throw new CacheException(e);
        }
    }

    @Override
    public void delete(String key) throws CacheException {
        if (key == null) {
            return;
        }

        try (Jedis jedis = getResource()) {
            jedis.del(key);
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:delete Exception: ", e);
            throw new CacheException(e);
        }
    }

    @Override
    public void addList(String key, List<String> value) throws CacheException {
        addList(key, DEFAULT_CACHED_EXPIRATION, value);
    }

    @Override
    public void addList(String key, int expirationInSeconds, List<String> value) throws CacheException {
        if (value == null) {
            return;
        }

        try (Jedis jedis = getResource()) {
            Transaction transaction = jedis.multi();

            for (int i = 0; i < value.size(); i++) {
                transaction.rpush(key, value.get(i));
            }

            transaction.expire(key, expirationInSeconds);
            List<Object> returnedObject = transaction.exec();

            if (returnedObject == null || (returnedObject.size() != value.size() + 1)) {
                throw new CacheException("RedisCacheHandler:addList Error in string values for key: " + key);
            }
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:addList Exception: ", e);
            throw new CacheException(e);
        }
    }

    @Override
    public List<String> getList(String key) throws CacheException {
        if (key == null) {
            return null;
        }

        try (Jedis jedis = getResource()) {
            List<String> value = jedis.lrange(key, 0, -1);
            return value;
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:getList Exception: ", e);
            throw new CacheException(e);
        }
    }

    public List<byte[]> getListAsBytes(String key) throws CacheException {
        if (key == null) {
            return null;
        }

        try (Jedis jedis = getResource()) {
            List<byte[]> value = jedis.lrange(SafeEncoder.encode(key), 0, -1);
            return value;
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:getListBinary Exception: ", e);
            throw new CacheException(e);
        }
    }

    public List<byte[]> getListAsBytes(String[] keys) throws CacheException {
        List<byte[]> result = new ArrayList<>();

        for (int i = 0; i < keys.length; i++) {  // TODO: change to multi request
            result.addAll(getListAsBytes(keys[i]));
        }

        return result;
    }

    @Override
    public List<String> getList(String key, int range) throws CacheException {
        if (key == null) {
            return null;
        }

        try (Jedis jedis = getResource()) {
            List<String> value = jedis.lrange(key, 0, range);
            return value;
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:getList Exception: ", e);
            throw new CacheException(e);
        }
    }

    @Override
    public void addSet(String key, String... values) throws CacheException {
        addSet(key, DEFAULT_CACHED_EXPIRATION, values);
    }

    @Override
    public void addSet(String key, int expirationInSeconds, String... values) throws CacheException {
        if (key == null) {
            return;
        }

        try (Jedis jedis = getResource()) {
            Transaction transaction = jedis.multi();
            transaction.sadd(key, values);
            transaction.expire(key, expirationInSeconds);
            List<Object> returnedObject = transaction.exec();

            if ((returnedObject == null) || (returnedObject.size() != 2) || !(returnedObject.get(0).equals((long) values.length))) {
                throw new CacheException("RedisCacheHandler:addSet() Error in storing values or expiry for key: " + key);
            }
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:addSet Exception: ", e);
            throw new CacheException(e);
        }
    }

    @Override
    public Set<String> getSet(String key) throws CacheException {
        if (key == null) {
            return null;
        }

        try (Jedis jedis = getResource()) {
            return jedis.smembers(key);
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:getSet Exception: ", e);
            throw new CacheException(e);
        }
    }

    @Override
    /**
     * Only use this method when you want to add / update fields for an EXISTING hash
     */
    public void addHash(String key, String field, String value) throws CacheException {
        if (key == null) {
            return;
        }

        try (Jedis jedis = getResource()) {

            // Checks if the hash exists
            if (jedis.exists(key)) {
                jedis.hset(key, field, value);
            } else {
                throw new CacheException(String.format("RedisCacheHandler:addHash() Error - No hash exist for the key %s", key));
            }
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:addHash Exception: ", e);
            throw new CacheException(e);
        }
    }

    @Override
    public void addHash(String key, int expirationInSeconds, String field, String value) throws CacheException {
        if (value == null) {
            return;
        }

        try (Jedis jedis = getResource()) {
            Transaction transaction = jedis.multi();
            transaction.hset(key, field, value);
            transaction.expire(key, expirationInSeconds);
            List<Object> returnedObject = transaction.exec();

            if (returnedObject == null || returnedObject.size() != 2) {
                throw new CacheException("RedisCacheHandler:addHash() Error in storing value or expiry for key: " + key);
            }
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:addHash Exception: ", e);
            throw new CacheException(e);
        }
    }

    @Override
    public void addHashMap(String key, Map<String, String> value) throws CacheException {
        addHashMap(key, DEFAULT_CACHED_EXPIRATION, value);
    }

    @Override
    public void addHashMap(String key, int expirationInSeconds, Map<String, String> value) throws CacheException {
        final String SUCCESS_STATUS = "OK";
        if (value == null) {
            return;
        }

        try (Jedis jedis = getResource()) {
            Transaction transaction = jedis.multi();
            transaction.hmset(key, value);
            transaction.expire(key, expirationInSeconds);
            List<Object> returnedObject = transaction.exec();

            if (returnedObject == null || returnedObject.size() != 2) {
                throw new CacheException("RedisCacheHandler:addHashMap() Error in storing value or expiry for key: " + key);
            }
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:addHashMap Exception: ", e);
            throw new CacheException(e);
        }
    }

    @Override
    public void updateHashMap(String oldKey, String newKey, Map<String, String> value) throws CacheException {
        if (value == null) {
            return;
        }

        try (Jedis jedis = getResource()) {

            Transaction transaction = jedis.multi();
            transaction.hmset(oldKey, value);
            transaction.rename(oldKey, newKey);
            List<Object> returnedObject = transaction.exec();

            if (returnedObject == null || returnedObject.size() != 2) {
                throw new CacheException("RedisCacheHandler:updateHashMap() Error in storing value or expiry for key: " + newKey);
            }
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:addHashMap Exception: ", e);
            throw new CacheException(e);
        }
    }

    @Override
    public List<String> getHash(String key, String... fields) throws CacheException {
        if (key == null) {
            return null;
        }

        try (Jedis jedis = getResource()) {
            return jedis.hmget(key, fields);
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:getHash Exception: ", e);
            throw new CacheException(e);
        }
    }

    @Override
    public Map<String, String> getHashAll(String key) throws CacheException {
        if (key == null) {
            return null;
        }

        try (Jedis jedis = getResource()) {
            return jedis.hgetAll(key);
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:getHashAll Exception: ", e);
            throw new CacheException(e);
        }
    }

    public void rpush(final String key, final String... values) throws CacheException {
        if (key == null) {
            return;
        }

        try (Jedis jedis = getResource()) {
            jedis.rpush(key, values);
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:rpush Exception: ", e);
            throw new CacheException(e);
        }
    }

    public void rpush(final String key, final byte[]... values) throws CacheException {
        if (key == null) {
            return;
        }

        try (Jedis jedis = getResource()) {
            jedis.rpush(SafeEncoder.encode(key), values);
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:rpush Exception: ", e);
            throw new CacheException(e);
        }
    }

    /**
     * This method adds values to the end of the list. Thew expiration of the list will be updated on every push.
     *
     * @param key
     * @param expirationInSeconds
     * @param values - Type: String
     * @throws CacheException
     */
    public void rpush(final String key, final int expirationInSeconds, final String... values) throws CacheException {
        if (key == null) {
            return;
        }

        try (Jedis jedis = getResource()) {
            Transaction transaction = jedis.multi();
            transaction.rpush(key, values);
            transaction.expire(key, expirationInSeconds);
            List<Object> returnedObject = transaction.exec();

            if (returnedObject == null || (returnedObject.size() != 2)) {
                throw new CacheException("RedisCacheHandler:rpush() Error in storing values or setting expiry for key: " + key);
            }
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:rpush() Exception: ", e);
            throw new CacheException(e);
        }
    }

    /**
     * This method adds values to the end of the list. Thew expiration of the list will be updated on every push.
     *
     * @param key
     * @param expirationInSeconds
     * @param values - Type: byte array.
     * @throws CacheException
     */
    public void rpush(final String key, final int expirationInSeconds, final byte[]... values) throws CacheException {
        if (key == null) {
            return;
        }

        byte[] encodedKey = SafeEncoder.encode(key);

        try (Jedis jedis = getResource()) {
            Transaction transaction = jedis.multi();
            transaction.rpush(encodedKey, values);
            transaction.expire(encodedKey, expirationInSeconds);
            List<Object> returnedObject = transaction.exec();

            if (returnedObject == null || (returnedObject.size() != 2)) {
                throw new CacheException("RedisCacheHandler:rpush() Error in storing values or setting expiry for key: " + key);
            }
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:rpush() Exception: ", e);
            throw new CacheException(e);
        }
    }

    @Override
    public boolean exists(final String key) throws CacheException {
        if (key == null) {
            return false;
        }

        try (Jedis jedis = getResource()) {
            return jedis.exists(key);
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:exists Exception: ", e);
            throw new CacheException(e);
        }
    }

    @Override
    public long getExpirationTime(String key) throws CacheException {
        return ttl(key);
    }

    public long ttl(final String key) throws CacheException {
        if (key == null) {
            return -2;
        }

        try (Jedis jedis = getResource()) {
            return jedis.ttl(key);
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:ttl Exception: ", e);
            throw new CacheException(e);
        }
    }

    private Jedis getResource() {
        JedisPool result = jedisPool;

        if (result == null) {
            synchronized (RedisCacheHandler.class) {
                result = jedisPool;

                if (result == null) {
                    result = jedisPool = new JedisPool(JedisPoolConfigWrapper.getInstance().getConfig(), RedisConfiguration.getInstance().getPrimaryEndPoint(),
                            RedisConfiguration.getInstance().getPort());
                }
            }
        }

        try {
            return result.getResource();
        } catch (JedisConnectionException e) {
            throw e;
        }
    }

    public long incr(String key) throws CacheException {
        if (key == null) {
            return -1;
        }

        Long jedisRetVal;

        try (Jedis jedis = getResource()) {
            jedisRetVal = jedis.incr(key);
        } catch (JedisException e) {
            logger.error("RedisCacheHandler:incr Exception: ", e);
            throw new CacheException(e);
        }

        return jedisRetVal;
    }
}
