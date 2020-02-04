package com.adsk.analytics.adapter;

/**
 * Created by aenugav on 9/23/2014.
 */
public interface DashboardFactory
{
    public IDashboardAdapter getDashboard(String dashboardName);
}
