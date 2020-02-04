package com.adsk.analytics.adapter;

import com.adsk.analytics.exception.NullWidgetException;

/**
 * Created by aenugav on 9/23/2014.
 */
public interface IDashboardAdapter
{
    public String getWidgetData(int widgetType) throws NullWidgetException;
}
