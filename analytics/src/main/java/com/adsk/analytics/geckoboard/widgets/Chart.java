package com.adsk.analytics.geckoboard.widgets;

import com.adsk.analytics.ChartSeries;

import java.util.Vector;

public class Chart {
    private Vector<ChartSeries> values;
    private Vector<String> xAxesLabels;
    private String yAxesLable;
    private String seriesStartDateInMillisString;
    private String chartTitle;

    public Chart() {
        this.values = new Vector<ChartSeries>();
        this.xAxesLabels = new Vector<String>();
        this.yAxesLable = "";
    }

    public Chart(Vector<ChartSeries> values, Vector<String> xLabels, String yLabel) {
        this.setValues(values);
        this.setxAxesLabels(xLabels);
        this.setyAxesLable(yLabel);
    }

    public Vector<ChartSeries> getValues() {
        return values;
    }

    public void setValues(Vector<ChartSeries> values) {
        this.values = values;
    }

    public Vector<String> getxAxesLabels() {
        return xAxesLabels;
    }

    public void setxAxesLabels(Vector<String> xAxesLabels) {
        this.xAxesLabels = xAxesLabels;
    }

    public String getyAxesLable() {
        return yAxesLable;
    }

    public void setyAxesLable(String yAxesLabl) {
        this.yAxesLable = yAxesLabl;
    }

    public String getSeriesStartDateInMillisString() {
        return seriesStartDateInMillisString;
    }

    public void setSeriesStartDateInMillisString(String seriesStartDateInMillisString) {
        this.seriesStartDateInMillisString = seriesStartDateInMillisString;
    }

    public String getChartTitle() {
        return chartTitle;
    }

    public void setChartTitle(String chartTitle) {
        this.chartTitle = chartTitle;
    }
}
