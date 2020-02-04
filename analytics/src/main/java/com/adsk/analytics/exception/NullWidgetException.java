package com.adsk.analytics.exception;

public class NullWidgetException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NullWidgetException(int widgetType)
	{
		super("Null Widget Exception Type:" + widgetType);
	}

}
