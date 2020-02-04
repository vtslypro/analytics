package com.adsk.analytics.geckoboard.widgets;

import com.adsk.analytics.ChartSeries;

import java.util.Vector;

public class HighchartLineWidget extends BaseWidget {
    private Vector<String> xAxes;
    private Chart chart;
    private String yLabel;
    private Vector<ChartSeries> chartSeries;
    private String axesString;
    private String seriesString;

    public HighchartLineWidget(Chart data) {
        this.chart = data;
        this.xAxes = this.chart.getxAxesLabels();
        this.yLabel = this.chart.getyAxesLable();
        this.chartSeries = this.chart.getValues();
        this.yLabel = chart.getyAxesLable();

        this.axesString = buildChartXAxes();
        this.seriesString = buildChartSeries();
    }


    @Override
    public String jsonToStringPoll() {
        // building long string
        String settings =
                "{" +
                        "chart: {" +
                        "renderTo: 'container'," +
                        "type: 'line'," +
                        "plotBackgroundColor: null," + // black
                        "plotBorderWidth: null," +
                        "defaultSeriesType: 'spline'" +
                        "}," +
                        "legend: {" +
                        "margin: 20" +
                        "}," +
                        "credits: {" +
                        "enabled: true" + // false
                        "}," +
                        "tooltip: {" +
                        "enabled: true," +
                        "formatter: function() {" +
                        "return '<b>'+ this.series.name +'</b><br/>'+" +
                        "this.x +': '+ this.y;" +
                        "}" +
                        "}," +
                        "xAxis: { " +
                        "categories: [" + this.axesString + "], " +
                        "title: {" +
                        "text: 'Date'" +
                        "}, " +
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
                        "return this.value;}" +
                        "}" +
                        "}," +
                        "plotOptions: {" +
                        "line: {" +
//			"dataLabels: {" +
//			"enabled: true," +
//			"y: -5," +
//			"style: {" +
//			"fontSize: '13px'," +
//			"fontFamily: 'Verdana, sans-serif'" +
//			"}" +
//			"}," +
                        "enableMouseTracking: true" +
                        "}" +
                        "}," +
                        "title: {" +
                        "text: null" +
                        "}," +
                        //				"series: [{" +
                        //					 "type: 'line'," +
                        //					 "name: " + "'" + seriesLabel + "'," +
                        //					 "data: [" + valuesStr + "]" +
                        //				"}]" +
                        this.seriesString +

                        "}";
        return settings;
    }

    private String buildChartSeries() {
        String series = "series : [";

        series += "{type: 'line',";
        series += "name: " + "'" + chartSeries.get(0).getLabel() + "',";
        series += "data: [" + buildValues(chartSeries.get(0).getValues()) + "]}";

        for (int index = 1; index < chartSeries.size(); index++) {
            series += ",{type: 'line',";
            series += "name: " + "'" + chartSeries.get(index).getLabel() + "',";
            series += "data: [" + buildValues(chartSeries.get(index).getValues()) + "]}";
        }

        series += "]";

        return series;
    }


    private String buildChartXAxes() {
        String firstCategory = this.xAxes.firstElement();
        int sizeOfValues = this.xAxes.size();
        String xAxis = "'" + firstCategory + "'";

        for (int i = 1; i < sizeOfValues; i++) {
            xAxis += ",";
            xAxis += "'" + this.xAxes.get(i) + "'";
        }
        return xAxis;
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

    @Override
    public String jsonToStringPush() {
return null;
    }

}
