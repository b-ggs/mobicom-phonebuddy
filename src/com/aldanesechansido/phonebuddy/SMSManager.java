package com.aldanesechansido.phonebuddy;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSManager extends BroadcastReceiver
{
	public void onReceive(Context context, Intent intent)
    {
    	if(DataManager.getInstance().isActive())
    	{
	        Bundle extras = intent.getExtras();

	        if (extras != null)
	        {
	            Object[] smsExtra = (Object[]) extras.get("pdus");
	            
	            String from = "";
	            String message = "";
	            
	            for (int i = 0; i < smsExtra.length; ++i)
	            {
	            	SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);
	            	
	            	from = sms.getOriginatingAddress();
	            	message = sms.getMessageBody().toString();
	                
	            	parseSMS(from, message, context);
	            }
	        }
    	}
        // this.abortBroadcast(); 
    }
    
    public void parseSMS(String from, String message, Context context)
    {
    	String response = "";
    	String buddyNumber = DataManager.getInstance().getBuddyNumber();
    	
    	if(!from.equals(buddyNumber)) //if from != buddy number
    	{
	    	response += "Relaying SMS from " + BuddyCommunication.getInstance().getContactName(from, context);
	    	response += " (" + from + ")" + ": \"";                    
	    	response += message;
	    	response +=	"\" to buddy (" + buddyNumber + ").";
	    	
	    	DataManager.getInstance().notify(response, DataManager.NOTIFY_LOG_ONLY, context);
	    	BuddyCommunication.getInstance().sendSMS(buddyNumber, BuddyCommunication.getInstance().constructSMS(BuddyCommunication.SMS_RELAY_TO_COMPANION, from, message, context));
    	}
    	else if(from.equals(buddyNumber)) //if from companion number
    	{
    		parseCommand(message, context);
    		this.abortBroadcast();
    	}
    	else
    	{
    		DataManager.getInstance().notify("Error: parseSMS()", DataManager.NOTIFY_TOAST_LOG, context);
    	}
    	
//    	this.abortBroadcast();
    }
    
    public void parseCommand(String message, Context context)
    {
    	String[] messageArray = message.split(" ");
    	String command = messageArray[0];
    	ArrayList<String> commandStatement = new ArrayList<String>();
    	
    	for(int i = 1; i < messageArray.length; i++)
    	{
    		commandStatement.add(messageArray[i]);
    	}
    	
    	if(command.equalsIgnoreCase("SENDSMS") && commandStatement.contains(new String("/")))
    	{
    		boolean flag = false;
    		String contact = "";
    		String smsmessage = "";
    		
    		for(int j = 0; j < commandStatement.size(); j++)
    		{
    			if(commandStatement.get(j).equals("/"))
    			{
    				if(flag)
    					smsmessage += "/ ";
    				else
	    				flag = true;
    			}
    			else if(!flag)
	    			contact += commandStatement.get(j) + " ";
    			else
    				smsmessage += commandStatement.get(j) + " ";
    		}
    		
    		contact = contact.trim();
			smsmessage = smsmessage.trim();
    		
    		ArrayList<String> sendTo = new ArrayList<String>();
    		sendTo = BuddyCommunication.getInstance().getContactNumber(contact, context);
    		
    		if(sendTo.size() == 1)
    		{
    			BuddyCommunication.getInstance().sendSMS(sendTo.get(0), BuddyCommunication.getInstance().constructSMS(BuddyCommunication.SMS_RELAY_TO_CONTACT, null, smsmessage, context));
    			DataManager.getInstance().notify("Sending SMS to " + BuddyCommunication.getInstance().getContactName(sendTo.get(0), context) + " (" + sendTo.get(0) + "): \"" + smsmessage + "\"", DataManager.NOTIFY_LOG_ONLY, context);
    		}
    		else if(sendTo.isEmpty())
    		{
    			DataManager.getInstance().notify("No result in contacts for \"" + contact.trim() + "\"", DataManager.NOTIFY_ERROR, context);
    		}
    		else
    		{
    			DataManager.getInstance().notify("Contact queries returning multiple results are not supported at this time.", DataManager.NOTIFY_ERROR, context);
    		}
    	}
    	else
    	{
    		DataManager.getInstance().notify("Invalid command received from buddy: \"" + message + "\"", DataManager.NOTIFY_ERROR, context);
    	}
    }
}