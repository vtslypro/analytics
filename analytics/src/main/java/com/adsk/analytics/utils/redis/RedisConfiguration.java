package com.adsk.analytics.utils.redis;

/**
 * Created by singha on 11/20/2014.
 */
public class RedisConfiguration {
    private static RedisConfiguration instance;

    private String primaryEndPoint;
    private int port;
    private int timeoutInMillis;

    public RedisConfiguration(String primaryEndPoint, int port, int timeoutInMillis){
        this.primaryEndPoint = primaryEndPoint;
        this.port = port;
        this.timeoutInMillis = timeoutInMillis;
        instance = this;
    }

    public static RedisConfiguration getInstance()
    {
        return instance;
    }

    public String getPrimaryEndPoint() {
        return primaryEndPoint;
    }

    public int getPort() {
        return port;
    }

    public int getTimeoutInMillis() {
        return timeoutInMillis;
    }

}
