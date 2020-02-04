package com.adsk.analytics;

public abstract class APIConnection
{
	protected String url;
	protected String outputString;
	
	public final String getAPIOutput(String url)
	{
		setConnectionUrl(url);
		connect();
		setServerOutput();
		disconnect();
		
		return this.outputString;
	}
	
	protected abstract void connect();
	
	protected abstract void setServerOutput();
	
	protected abstract void disconnect();
	
	public void setConnectionUrl(String url)
	{
		this.url = url;
	}
}
