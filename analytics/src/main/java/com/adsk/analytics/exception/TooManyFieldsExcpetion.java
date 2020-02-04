package com.adsk.analytics.exception;

public class TooManyFieldsExcpetion extends Exception
{
	private static final long serialVersionUID = 1L;

	private short _fieldsNum;
	
	public TooManyFieldsExcpetion(short fieldsNum)
	{
		_fieldsNum = fieldsNum;
	}
	
	@Override
	public String toString()
	{
		return "This widget can't show more than " + _fieldsNum + " fields.";
	}
}
