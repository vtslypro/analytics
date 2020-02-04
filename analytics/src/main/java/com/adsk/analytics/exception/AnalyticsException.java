package com.adsk.analytics.exception;

/**
 * Created by aenugav on 10/20/2014.
 */
public class AnalyticsException extends Exception {
    public AnalyticsException(String message) {
        super(message);
    }

    public AnalyticsException(Throwable w){
        super(w);
    }
}
