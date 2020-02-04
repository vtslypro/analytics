package com.adsk.analytics.utils.redis;

import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by singha on 11/19/2014.
 */
public class JedisPoolConfigWrapper {
    private static JedisPoolConfigWrapper instance;
    private JedisPoolConfig config;

    public JedisPoolConfigWrapper() {
        this.config = new JedisPoolConfig();
    }

    // this may be used any other module which do not have spring support
    public JedisPoolConfigWrapper(JedisPoolConfig jedisPoolConfig) {
        this.config = jedisPoolConfig;
    }

    public void init()
    {
        instance = this;
    }

    public static JedisPoolConfigWrapper getInstance()
    {
        return instance;
    }

    public JedisPoolConfig getConfig() {
        return config;
    }

    public int getMaxTotal() {
        return config.getMaxTotal();
    }

    public void setMaxTotal(int maxTotal) {
        config.setMaxTotal(maxTotal);
    }

    public int getMaxIdle() {
        return config.getMaxIdle();
    }

    public void setMaxIdle(int maxIdle) {
        config.setMaxIdle(maxIdle);
    }

    public int getMinIdle() {
        return config.getMinIdle();
    }

    public void setMinIdle(int minIdle) {
        this.config.setMinIdle(minIdle);
    }

    public long getMaxWaitMillis() {
        return config.getMaxWaitMillis();
    }

    public void setMaxWaitMillis(long maxWait) {
        config.setMaxWaitMillis(maxWait);
    }

    public boolean getTestOnBorrow() {
        return config.getTestOnBorrow();
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        config.setTestOnBorrow(testOnBorrow);
    }

    public boolean getTestOnReturn() {
        return config.getTestOnReturn();
    }

    public void setTestOnReturn(boolean testOnReturn) {
        config.setTestOnReturn(testOnReturn);
    }

    public boolean getTestWhileIdle() {
        return config.getTestWhileIdle();
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        config.setTestWhileIdle(testWhileIdle);
    }

    public boolean getBlockWhenExhausted() {
        return config.getBlockWhenExhausted();
    }

    public void setBlockWhenExhausted(boolean whenExhaustedAction) {
        config.setBlockWhenExhausted(whenExhaustedAction);
    }
}
