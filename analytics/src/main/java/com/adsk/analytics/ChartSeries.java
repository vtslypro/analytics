package com.adsk.analytics;

/**
 * Created by aenugav on 9/22/2014.
 */
import java.util.Vector;

public class ChartSeries
{
    private Vector<String> values;
    private String label;
    private String seriesStartDateInMillisString;

    public ChartSeries(Vector<String> values, String label)
    {
        this.setValues(values);
        this.setLabel(label);
    }

    public ChartSeries(Vector<String> values, String label, String seriesStartDateInMillisString) {
        this.setValues(values);
        this.setLabel(label);
        this.setSeriesStartDateInMillisString(seriesStartDateInMillisString);
    }

    public Vector<String> getValues()
    {
        return values;
    }

    public void setValues(Vector<String> values)
    {
        this.values = values;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getSeriesStartDateInMillisString() {
        return seriesStartDateInMillisString;
    }

    public void setSeriesStartDateInMillisString(String seriesStartDateInMillisString) {
        this.seriesStartDateInMillisString = seriesStartDateInMillisString;
    }
}