package com.adsk.analytics.geckoboard.widgets;

import com.adsk.analytics.ChartSeries;

import java.util.Vector;

/**
 * Created by deepa on 6/22/2015.
 */
public class HighchartTimeDateWidget extends BaseWidget {

    private Vector<String> xAxes;
    private Chart chart;
    private String yLabel;
    private Vector<ChartSeries> chartSeries;
    private String axesString;
    private String seriesString;
    private String chartTitle;

    public HighchartTimeDateWidget(Chart data) {
        this.chart = data;
        this.yLabel = this.chart.getyAxesLable();
        this.chartSeries = this.chart.getValues();
        this.yLabel = chart.getyAxesLable();
        this.chartTitle = data.getChartTitle();
        this.seriesString = buildChartSeries();
    }

    @Override
    public String jsonToStringPoll() {

        // building long string
        String settings =
                "{" +
                        "chart: {" +
                        "zoomType: 'x'," +
                        "renderTo: 'container'," +
                        "type: 'spline'" +
                        "}," +
                        "legend: {" +
                        "margin: 20" +
                        "}," +
                        "credits: {" +
                        "enabled: true" + // false
                        "}," +
                        "tooltip: {" +
                        "dateTimeLabelFormats: {" +
                        "millisecond: '%Y-%m-%d'," +
                        "second: '%Y-%m-%d'," +
                        "minute: '%Y-%m-%d'," +
                        "hour: '%Y-%m-%d'," +
                        "day: '%Y-%m-%d'," +
                        "week: '%Y-%m-%d'," +
                        "month: '%Y-%m-%d'," +
                        "year: '%Y-%m-%d'" +
                        "}" +
                        "}," +
                        "xAxis: { " +
                        "type : 'datetime'," +
                        "minTickInterval: 24 * 3600 * 1000," +
                        "dateTimeLabelFormats: {" +
                        "day: '%Y-%m-%d'," +
                        "week: '%Y-%m-%d'," +
                        "month: '%Y-%m-%d'," +
                        "year: '%Y-%m-%d'" +
                        "}," +
                        "labels: {" +
                        "y: 20," +
                        "rotation: -45," +
                        "align: 'right'" +
                        "}" +
                        "}," +
                        "yAxis: {" +
                        "gridLineColor: 'rgba(255,255,255,0.05)'," +
                        "title: {" +
                        "text: " + "'" + yLabel + "'" +
                        "}," +
                        "labels: {" +
                        "formatter: function() {" +
                        "return this.value;" +
                        "}" +
                        "}" +
                        "}," +
                        "plotOptions: {" +
                        "spline: {" +
                        "lineWidth: 4," +
                        "states: {" +
                        "hover: {" +
                        "lineWidth: 5" +
                        "}" +
                        "}," +
                        "marker: {" +
                        "enabled: false" +
                        "}" +
                        "}" +
                        "}," +
                        "title: {" +
                        "text: '" + this.chartTitle + "'" +
                        "}," +
                        this.seriesString +
                        "}";

        return settings;
    }

    private String buildValues(Vector<String> seriesArray) {
        String firstValue = seriesArray.get(0);
        String values = firstValue;

        for (int index = 1; index < seriesArray.size(); index++) {
            values += ",";
            values += seriesArray.get(index);
        }
        return values;
    }

    private String buildChartSeries() {
        String series = "series : [";
        series += "{type: 'line',";
        series += "name: " + "'" + chartSeries.get(0).getLabel() + "',";
        series += "pointInterval: 24 * 3600000,"; // one day
        series += "pointStart: " + chartSeries.get(0).getSeriesStartDateInMillisString() + ",";
        series += "pointRange: 24 * 3600 * 1000,";
        series += "pointPlacement: -0.5,";
        series += "data: [" + buildValues(chartSeries.get(0).getValues()) + "]}";

        for (int index = 1; index < chartSeries.size(); index++) {
            series += ",{type: 'line',";
            series += "name: " + "'" + chartSeries.get(index).getLabel() + "',";
            series += "pointInterval: 24 * 3600000,"; // one day
            series += "pointStart: " + chartSeries.get(index).getSeriesStartDateInMillisString() + ",";
            series += "pointRange: 24 * 3600 * 1000,";
            series += "pointPlacement: -0.5,";
            series += "data: [" + buildValues(chartSeries.get(index).getValues()) + "]}";
        }

        series += "]";

        return series;
    }

    @Override
    public String jsonToStringPush() {
        return null;
    }
}
