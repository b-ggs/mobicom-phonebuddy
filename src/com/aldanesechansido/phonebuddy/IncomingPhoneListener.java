package com.aldanesechansido.phonebuddy;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class IncomingPhoneListener extends PhoneStateListener
{
	private boolean callFlag = false;
	private Context context;
	
	public IncomingPhoneListener(Context context)
	{
		super();
		this.context = context;
	}
	
	@Override
	public void onCallStateChanged(int state, String incomingNumber)
	{
		String message = "";
		
		super.onCallStateChanged(state, incomingNumber);
		if(state == TelephonyManager.CALL_STATE_RINGING)
		{
			callFlag = true;
			
			message = BuddyCommunication.getInstance().constructSMS(BuddyCommunication.CALL_ACTIVE_RELAY_TO_COMPANION, incomingNumber, null, context);
			BuddyCommunication.getInstance().sendSMS(DataManager.getInstance().getBuddyNumber(), message);
			String response = "";
			response += "Relaying incoming call notification from " + BuddyCommunication.getInstance().getContactName(incomingNumber, context);
	    	response += " (" + incomingNumber + ") ";
	    	response +=	"to buddy (" + DataManager.getInstance().getBuddyNumber() + ").";
			DataManager.getInstance().getServiceActivity().addLog(response);
		}
//		if(callFlag)
//		{
//			callFlag = false;
//			message = BuddyCommunication.getInstance().constructSMS(BuddyCommunication.CALL_MISSED_RELAY_TO_COMPANION, incomingNumber, null, context);
//			BuddyCommunication.getInstance().sendSMS(DataManager.getInstance().getBuddyNumber(), message);
//			
////			Intent i = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
////           // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            context.startActivity(i);
//            
//		}
	}
}