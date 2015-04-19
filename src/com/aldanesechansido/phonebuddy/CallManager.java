package com.aldanesechansido.phonebuddy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class CallManager extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if(DataManager.getInstance().isActive())
		{
			try
			{
				TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				IncomingPhoneListener incomingPhoneListener = new IncomingPhoneListener(context);
				telephonyManager.listen(incomingPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
			}
			catch(Exception e)
			{
				
			}
		}
	}
}
