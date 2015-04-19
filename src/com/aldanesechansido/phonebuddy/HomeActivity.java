package com.aldanesechansido.phonebuddy;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.app.NotificationManager;

public class HomeActivity extends Activity
{
	private ToggleButton serviceButton;
	private Button settingsBtn;
	private TextView phoneBuddyNumber;
	private EditText logField;
	private String buddyNumber;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		
		buddyNumber = getIntent().getExtras().getString("buddynumber");
		serviceButton = (ToggleButton) findViewById(R.id.service_active);
		serviceButton.setOnClickListener(toggleService);
	}
		
	
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		if(DataManager.getInstance().isActive())
			Toast.makeText(this, getResources().getString(R.string.app_name) + " is still running.", Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
	OnClickListener toggleService = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public void addLog(String logNew){
		String log = "";
		EditText logEditText = (EditText) findViewById(R.id.log_field);
		
		Time logTime = new Time(Time.getCurrentTimezone());
		logTime.setToNow();
		logEditText.getText();
		
		log += "- " + logTime.format("%k:%M:%S") + ": " + logNew;
		log += "\n" + logEditText.getText();
		
		logEditText.setText(log);
	}
	
}