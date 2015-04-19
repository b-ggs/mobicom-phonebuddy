package com.aldanesechansido.phonebuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {
	
	boolean callForward;
	CustomItemAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		if(!DataManager.getInstance().isActive())
			callForward = DataManager.getInstance().isForwardingCall();
		else
			callForward = false;
		Setting[] settings = {
				new Setting(callForward, null, "Your calls will be forwarded to your buddy.", true),
				new Setting(true, "Log Out", "Don't want to use this buddy anymore? Your buddy's number will be removed.", false)
		};
		adapter = new CustomItemAdapter(getBaseContext(), R.layout.list_item, settings)
		{
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = getLayoutInflater();
				View rowView = inflater.inflate(R.layout.list_item, null, true);
				TextView header = (TextView) rowView.findViewById(R.id.tv_header);
				TextView description = (TextView) rowView.findViewById(R.id.tv_description);
				
				if(position == 0)
				{
					header.setText("Call Forwarding");
					description.setText("Your calls will be forwarded to your buddy.");
				}
				else if(position == 1)
				{
					header.setText("Log Out");
					description.setText("Don't want to use this buddy anymore? Your buddy's number will be removed.");
				}
				
				if(position == 0)
				{
					Switch sb = (Switch) rowView.findViewById(R.id.switch_btn);
					sb.setChecked(callForward);
					sb.setOnCheckedChangeListener(toggleCheck);
				}
				else
				{
					Switch sb = (Switch) rowView.findViewById(R.id.switch_btn);
					sb.setVisibility(View.GONE);
				}
				
				return rowView;
			}
		};
		ListView lv = (ListView) findViewById(R.id.lv_settings);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(settingsSelect);
	}
	
	OnCheckedChangeListener toggleCheck = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			if(DataManager.getInstance().isActive())
			{
				if(!isChecked) //disable service
				{
					DataManager.getInstance().setCallForwarding(false);
					callforward("#21#");
					//Toast.makeText(getBaseContext(), "DISABLE SERVICE", Toast.LENGTH_LONG).show();
					DataManager.getInstance().getServiceActivity().addLog("Call forwarding disabled.");
				}
				else //enable service
				{
					DataManager.getInstance().setCallForwarding(true);
					callforward("*21*" + DataManager.getInstance().getBuddyNumber() +"#");
					//Toast.makeText(getBaseContext(), "ENABLE SERVICE: " + DataManager.getInstance().getBuddyNumber(), Toast.LENGTH_LONG).show();
					DataManager.getInstance().getServiceActivity().addLog("Call forwarding enabled.");
				}
			}
			else
			{
				Toast.makeText(getBaseContext(), "Please enable service before changing call forwarding settings.", Toast.LENGTH_LONG).show();
				buttonView.setChecked(false);
			}
		}
	};
	
	OnItemClickListener settingsSelect = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			// TODO Auto-generated method stub
			if(position == 0)
			{
				
			}
			else if (position == 1)
			{
				Toast.makeText(getBaseContext(), "You have been logged out.", Toast.LENGTH_LONG).show();
				DataManager.getInstance().reset();
				Intent i = new Intent();
				i.setClass(getBaseContext(), ConfirmationActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(i);
			}
		}
	};
	
	OnClickListener switchForwarding = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Switch switchButton = (Switch) v.findViewById(R.id.switch_btn);
			if(DataManager.getInstance().isActive())
			{
				if(switchButton.isChecked()) //disable service
				{
					DataManager.getInstance().setCallForwarding(false);
					switchButton.setChecked(false);
					callforward("#21#");
					Toast.makeText(getBaseContext(), "DISABLE SERVICE", Toast.LENGTH_LONG).show();
				}
				else if(!switchButton.isChecked()) //enable service
				{
					DataManager.getInstance().setCallForwarding(true);
					switchButton.setChecked(true);
					callforward("*21*" + DataManager.getInstance().getBuddyNumber() +"#");
					Toast.makeText(getBaseContext(), "ENABLE SERVICE: " + DataManager.getInstance().getBuddyNumber(), Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				Toast.makeText(getBaseContext(), "Please enable service before changing call forwarding settings.", Toast.LENGTH_LONG).show();
			}
		}
	};
	
	public void callforward(String callForwardString)
    {
        IncomingPhoneListener phoneListener = new IncomingPhoneListener(getBaseContext());
        TelephonyManager telephonyManager = (TelephonyManager)
         this.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
               
        Intent intentCallForward = new Intent(Intent.ACTION_CALL);
        Uri mmiCode = Uri.fromParts("tel", callForwardString, "#");
        intentCallForward.setData(mmiCode);
        startActivity(intentCallForward);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}
}
