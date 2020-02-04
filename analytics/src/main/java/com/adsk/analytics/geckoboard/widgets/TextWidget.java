package com.adsk.analytics.geckoboard.widgets;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;


public class TextWidget extends BaseWidget 
{
	// widget will contain only one page. More can be included
	private final static Integer type = 0;
	private String message;
	
	public TextWidget(String message) 
	{
		this.message = message;
	}
	
	
	public String jsonToStringPoll()
	{
		@SuppressWarnings("rawtypes")
		Map map = new LinkedHashMap();
		JSONObject obj = new JSONObject();
		JSONArray list = new JSONArray();

		obj.put("text", this.message);
		obj.put("type", TextWidget.type);
		list.add(obj);
		
		map.put("item", list);
				
		return this.mapToJSONString(map);
	}

	@Override
	public String jsonToStringPush() {
		return null;
	}
}
