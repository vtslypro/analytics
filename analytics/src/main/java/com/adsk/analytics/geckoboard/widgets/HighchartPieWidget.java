package com.adsk.analytics.geckoboard.widgets;


import java.util.Vector;

public class HighchartPieWidget extends BaseWidget
{
	private Vector<String> labels;
	private Vector<String> values;
	private int total;
	private String seriesName;
	private boolean isLegendWithNumbers;

	public HighchartPieWidget(Vector<String> values, Vector<String> labels, String seriesName, int total, boolean isLegendWithNumbers)
	{
//		this.url = "https://push.geckoboard.com/v1/send/28547-deb88b33-e087-468f-9a15-a32580ff9c24";
		this.labels = labels;
		this.values = values;
		this.seriesName = seriesName;
		this.total = total;
		this.isLegendWithNumbers = isLegendWithNumbers;
	}

	@Override
	public String jsonToStringPush()
	{
		return null;
	}

	@Override
	public String jsonToStringPoll()
	{
		
		String dataStr = buildData();

		// building long string
		String settings =
				"{" +
						"chart: {" +
						"renderTo: 'container'," +
						"plotBackgroundColor: null," + // black
						"plotBorderWidth: null," +
						"events: {" +
						" load: function(event) { " +
						" var total = " + this.total + "; " +
						"console.log(\"chart.plotLeft \"+this.plotLeft+\" chart.plotRight \"+this.plotRight+\" chart.plotbottom \"+this.plotBottom+\" chart.plotTop \"+this.plotTop+\" xaxis \" + this.xAxis[0].width +\" yaxis \" + this.yAxis[0].height +\" spacingLeft \"+this.spacingLeft);" +
						" this.renderer.text( " +
						" 'Total: ' + total, " +
						" this.xAxis[0].width/6, " +
						" this.yAxis[0].height-30 " +
						" ).attr({" +
						" zIndex : 5 " +
						" }) .css({" +
						" fontWeight: 'bold'," +
						" color: '#000000'," +
						" fontSize: '20px'" +
						" }).add() " +
						"}" +
						"} " +
						"}," +
						"credits: {" +
						"enabled: true" + // false
						"}," +
						"tooltip: {" +
						"pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'," +  // showing number on hover
						"percentageDecimals: 1" +
						"},";
		if (this.isLegendWithNumbers == true) {
			settings +=
					"legend: {" +
							"itemStyle: {" +
							"fontSize: '25px'," +
							"color: '#000000'" +
							"}," +
							"width: 450," +
							"useHTML: true," +
							"labelFormatter: function() {" +
							"return this.name + ' : ' + this.y;" +
							"}" +
							"},";
		}

		settings +=
				"plotOptions: {" +
						"pie: {" +
						"allowPointSelect: true," +
						"cursor: 'pointer'," +
						"dataLabels: {" +
						"enabled: true," +
						"color: '#000000'," +
						"connectorColor: '#000000'," +
						"format: '<b>{point.name}</b>: {y}'," + // showing numbers on the chart
						"style: {fontSize: '20px'}" +
						"}," +
						"showInLegend: true" +
						"}" +
						"}," +
						"title: {" +
						"text: null" +
						"}," +
						"series: [{" +
						"type: 'pie'," +
						"name: '" + this.seriesName + "'," +
						"data: [" +
						dataStr +
						"]" +
						"}]" +

						"}";

		return settings;
	}

	private String buildData()
	{
		String data = "['" + this.labels.get(0) + "', " + this.values.get(0) + "]";
		
		for (int index = 1; index < this.labels.size(); index ++)
		{
			data += ",['" + this.labels.get(index) + "', " + this.values.get(index) + "]";
		}
		return data;
	}

}
