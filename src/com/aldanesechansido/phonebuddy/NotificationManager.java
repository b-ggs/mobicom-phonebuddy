package com.aldanesechansido.phonebuddy;

import java.util.ArrayList;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Parcelable;
import android.view.accessibility.AccessibilityEvent;

public class NotificationManager extends AccessibilityService
{
	private ArrayList<String> ignore =  new ArrayList<String>();
	
	protected void onServiceConnected()
	{
		AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        info.notificationTimeout = 100;
        setServiceInfo(info);
        
        ignore.add("com.android.mms");
        ignore.add("com.android.dialer");
	}

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event)
	{
		// TODO Auto-generated method stub
		if(DataManager.getInstance().isActive())
		{
			if(event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED)
			{
				try
				{
					Parcelable data = event.getParcelableData();
					if(!ignore.contains(event.getPackageName()))
					{
						if(data instanceof Notification)
						{
							String appName = "";
							Notification n = (Notification) data;
//							DataManager.getInstance().getHomeActivity().addLog(event.getPackageName().toString() + ": " + n.tickerText.toString());
							
							PackageManager packageManager = getBaseContext().getPackageManager();
							ApplicationInfo applicationInfo;
							try
							{
							    applicationInfo = packageManager.getApplicationInfo(event.getPackageName().toString(), 0);
							    appName = (String) packageManager.getApplicationLabel(applicationInfo);
							}
							catch (final NameNotFoundException e)
							{
								appName = event.getPackageName().toString();
							}
							
							String response = "";
							response += "Relaying notification from " + appName + ": \"";
					    	response += n.tickerText.toString();
					    	response +=	"\" to buddy (" + DataManager.getInstance().getBuddyNumber() + ").";
					    	DataManager.getInstance().notify(response, DataManager.NOTIFY_LOG_ONLY, getBaseContext());
							
							String sms = BuddyCommunication.getInstance().constructSMS(BuddyCommunication.NOTIF_RELAY_TO_COMPANION, appName , "\"" + n.tickerText.toString() + "\"", getBaseContext());
							BuddyCommunication.getInstance().sendSMS(DataManager.getInstance().getBuddyNumber(), sms);
						}
					}
				}
				catch(Exception e)
				{
//					DataManager.getInstance().getHomeActivity().addLog(e.toString());
				}
			}
		}
	}

	@Override
	public void onInterrupt()
	{
		// TODO Auto-generated method stub
		
	}

}