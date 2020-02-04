package com.adsk.analytics.geckoboard.widgets;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.json.simple.JSONValue;

public abstract class BaseWidget
{
	// for push connections 
	protected String url;
	
	public abstract String jsonToStringPoll();
	
	public abstract String jsonToStringPush();
	
	public String getUrl() 
	{
		return this.url;
	}
	
	public String mapToJSONString(@SuppressWarnings("rawtypes") Map map)
	{
		// converting the map to json 
		StringWriter out = new StringWriter();
		
		try
		{
			JSONValue.writeJSONString(map, out);
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		// converting the json expression to string
		String message = out.toString();
		
		return message;
	}
}
