package com.adsk.analytics.utils;

/**
 * Created by singha on 12/22/2014.
 */
public class CacheException extends Exception {
    public CacheException(String message) {
        super(message);
    }

    public CacheException(Throwable w){
        super(w);
    }
}
