package com.aldanesechansido.phonebuddy;

import com.aldanesechansido.phonebuddy.R;

import android.content.Context;
import android.widget.Toast;

public class DataManager
{
	private static DataManager instance;
	private String app_name;
	private boolean active = false;
	private String buddyNumber = null; 
	private ServiceActivity serviceActivity = null;
	private boolean forwarding = false;
	
	public static final int NOTIFY_ERROR = 0;
	public static final int NOTIFY_LOG_ONLY = 1;
	public static final int NOTIFY_TOAST_ONLY = 2;
	public static final int NOTIFY_TOAST_LOG = 3;
	public static final int NOTIFY_ERROR_SMS = 4;
	
	private DataManager()
	{
		
	}
	
	public static synchronized DataManager getInstance()
	{
		if(instance == null)
		{
			instance = new DataManager();
		}
		
		return instance;
	}
	
	public boolean isActive()
	{
		return active;
	}
	
	public boolean hasHomeActivity()
	{
		if(serviceActivity != null)
			return true;
		return false;
	}
	
	public void setActive(boolean active)
	{
		this.active = active;
	}
	
	public void setServiceActivity(ServiceActivity serviceActivity)
	{
		this.serviceActivity = serviceActivity;
		app_name = serviceActivity.getResources().getString(R.string.app_name);
	}
	
	public ServiceActivity getServiceActivity()
	{
		return serviceActivity;
	}
	
	public boolean hasBuddyNumber()
	{
		if(buddyNumber != null)
			return true;
		return false;
	}
	
	public void setBuddyNumber(String buddyNumber)
	{
		this.buddyNumber = buddyNumber.replace("\\(", "").replace("\\)", "").replace(" ", "");
//		Toast.makeText(getServiceActivity().getBaseContext(), this.buddyNumber, Toast.LENGTH_LONG).show();
	}
	
	public String getBuddyNumber()
	{
		return buddyNumber;
	}
	
	public String getAppName()
	{
		return app_name;
	}
	
	public boolean isForwardingCall()
	{
		return forwarding;
	}
	
	public void setCallForwarding(boolean forwarding)
	{
		this.forwarding = forwarding;
	}
	
	public void reset()
	{
		instance = new DataManager();
	}
	
	public void notify(String message, int type, Context context)
    {
    	if(type == NOTIFY_LOG_ONLY)
    	{
    		DataManager.getInstance().getServiceActivity().addLog(message);
    	}
    	else if(type == NOTIFY_ERROR)
    	{
    		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    		DataManager.getInstance().getServiceActivity().addLog(message);
    	}
    	else if(type == NOTIFY_ERROR_SMS)
    	{
    		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    		DataManager.getInstance().getServiceActivity().addLog(message);
    		BuddyCommunication.getInstance().sendSMS(DataManager.getInstance().getBuddyNumber(), app_name + ": " + message);
    	}
    	else if(type == NOTIFY_TOAST_ONLY)
    	{
    		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    	}
    	else if(type == NOTIFY_TOAST_LOG)
    	{
    		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    		DataManager.getInstance().getServiceActivity().addLog(message);
    	}
    }
}
