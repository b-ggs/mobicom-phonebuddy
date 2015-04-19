package com.aldanesechansido.phonebuddy;

import java.util.ArrayList;

import com.aldanesechansido.phonebuddy.R;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.telephony.SmsManager;

public class BuddyCommunication
{
	private String app_name = "";
	private static BuddyCommunication instance;
	public static final int SMS_RELAY_TO_COMPANION = 0;
	public static final int SMS_RELAY_TO_CONTACT = 1;
	public static final int CALL_ACTIVE_RELAY_TO_COMPANION = 2;
	public static final int CALL_MISSED_RELAY_TO_COMPANION = 2;
	public static final int NOTIF_RELAY_TO_COMPANION = 3;
	
	private BuddyCommunication()
	{
		if(DataManager.getInstance().isActive())
			app_name = DataManager.getInstance().getServiceActivity().getResources().getString(R.string.app_name);
	}
	
	public static synchronized BuddyCommunication getInstance()
	{
		if(instance == null)
		{
			instance = new BuddyCommunication();
		}
		
		return instance;
	}
	
	public String constructSMS(int type, String from, String message, Context context)
    {
    	String response = "";
    	
    	if(type == SMS_RELAY_TO_COMPANION)
    	{
    		response += app_name + ":\n";
    		response += "SMS from " + getContactName(from, context) + " (" + from + "):\n";
    		response += "\"" + message + "\"";
    	}
    	else if(type == SMS_RELAY_TO_CONTACT)
    	{
    		response += message + "\n";
    		response += "Sent from " + app_name;
    	}
    	else if(type == CALL_ACTIVE_RELAY_TO_COMPANION)
    	{
    		response += app_name + ":\n";
    		response += "Incoming call from " + getContactName(from, context) + " (" + from + ").\n";
    	}
    	else if(type == CALL_MISSED_RELAY_TO_COMPANION)
    	{
    		response += app_name + ":\n";
    		response += "Missed call from " + getContactName(from, context) + " (" + from + ").\n";
    	}
    	else if(type == NOTIF_RELAY_TO_COMPANION)
    	{
    		response += app_name + ":\n";
    		response += "Notification from application " + from + ": ";
    		response += message;
    	}
    	
    	return response;
    }
    
    public void sendSMS(String phoneNumber, String message)
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }
    
    public String getContactName(String number, Context context)
    {
        String name = null;

        String[] projection = new String[] {
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup._ID };

        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        Cursor cursor = context.getContentResolver().query(contactUri, projection, null, null, null);

        if(cursor != null)
        {
            if (cursor.moveToFirst())
            	name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            else
                name = number;
            cursor.close();
        }
        
        return name;
    }
    
    public ArrayList<String> getContactNumber(String name, Context context)
    {
    	ArrayList<String> numbers = new ArrayList<String>();
    	
    	Cursor cursor = null;
    	try
    	{
    	    cursor = context.getContentResolver().query(Data.CONTENT_URI,
    	            new String [] { Data.RAW_CONTACT_ID },
    	            StructuredName.DISPLAY_NAME + "=? AND "
    	                + Data.MIMETYPE + "='" + StructuredName.CONTENT_ITEM_TYPE + "'",
    	            new String[] { name },  null);
    	    if (cursor != null && cursor.moveToFirst())
    	    {
    	        do
    	        {
    	            String rawContactId = cursor.getString(0);
    	            Cursor phoneCursor = null;
    	            try
    	            {
    	                phoneCursor = context.getContentResolver().query(Data.CONTENT_URI,
    	                        new String[] {Data._ID, Phone.NUMBER},
    	                        Data.RAW_CONTACT_ID + "=?" + " AND "
    	                                + Phone.TYPE + "=" + Phone.TYPE_MOBILE + " AND "
    	                                + Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'",
    	                                new String[] {rawContactId}, null);

    	                if (phoneCursor != null && phoneCursor.moveToFirst())
    	                {
    	                    String number = phoneCursor.getString(phoneCursor.getColumnIndex(Phone.NUMBER));
    	                    numbers.add(number.trim().replace("-", "").replace(" ", ""));
    	                }
    	            } 
    	            finally
    	            {
    	                if (phoneCursor != null)
    	                    phoneCursor.close();
    	            }
    	        }
    	        while (cursor.moveToNext());  
    	    }
    	}
    	finally
    	{
    	    if (cursor != null) 
    	    {
    	        cursor.close();
    	    }
    	}
    	
    	//better number handling
    	if (numbers.isEmpty())
    	{
    		if(name.charAt(0) == '+')
    	    {
        		numbers.add(name.trim().replace("-", "").replace(" ", ""));
    	    }
    	}
    	
    	return numbers;
    }
}
