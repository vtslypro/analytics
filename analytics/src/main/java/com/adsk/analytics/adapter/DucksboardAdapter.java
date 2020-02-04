package com.adsk.analytics.adapter;

/**
 * Created by aenugav on 9/23/2014.
 */
import org.springframework.stereotype.Component;

@Component("ducksboard")
public class DucksboardAdapter implements IDashboardAdapter{
    @Override
    public String getWidgetData(int widgetType)
    {
        //test message. Logic to be updated.
        System.out.println("ducks");
        return "DUCKS";
    }
}
