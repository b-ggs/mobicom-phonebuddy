package com.aldanesechansido.phonebuddy;

import android.app.Activity;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Time;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ServiceActivity extends Activity {

	private String buddyNumber;
	private TextView tvBuddyNum;
	private Button settingBtn;
	private Button serviceBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service);
		getActionBar().hide();
		
		buddyNumber = getIntent().getExtras().getString("buddynumber");
		System.out.print(buddyNumber);
		tvBuddyNum = (TextView) findViewById(R.id.buddy_number);
		tvBuddyNum.setText(buddyNumber);

		DataManager.getInstance().setServiceActivity(this);
		DataManager.getInstance().setBuddyNumber(buddyNumber);
		
		settingBtn = (Button) findViewById(R.id.setting_btn);
		settingBtn.setOnClickListener(toSettings);
		
		serviceBtn = (Button) findViewById(R.id.service_active);
		serviceBtn.setOnClickListener(serviceToggle);
		
		serviceBtn.setTextColor(Color.WHITE);
		
		EditText logEditText = (EditText) findViewById(R.id.log_text);
		logEditText.setText("");
		
		addLog("Hit the blue button to start using PhoneBuddy!");
	}
	
	OnClickListener serviceToggle = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Button button = (Button) v.findViewById(R.id.service_active);
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			if(!DataManager.getInstance().isActive())
			{
				Bitmap icon = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.notif_big);
				DataManager.getInstance().setActive(true);
				addLog("PhoneBuddy service is active.");
				Notification.Builder mBuilder =
						new Notification.Builder(ServiceActivity.this)
						.setSmallIcon(R.drawable.notif_small)
						.setContentTitle("PhoneBuddy")
						.setContentText("PhoneBuddy is active.")
						.setLargeIcon(icon)
						.setOngoing(true);
				mNotificationManager.notify(0, mBuilder.build());
			}
			else if(DataManager.getInstance().isActive())
			{
				DataManager.getInstance().setActive(false);
				addLog("PhoneBuddy service is inactive.");
				mNotificationManager.cancel(0);
			}
		}
	};
	
	OnClickListener toSettings = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent();
			i.setClass(getBaseContext(), SettingsActivity.class);
			startActivity(i);
		}
	};

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
	
	public void addLog(String logNew)
	{
		String log = "";
		EditText logEditText = (EditText) findViewById(R.id.log_text);
		
		Time logTime = new Time(Time.getCurrentTimezone());
		logTime.setToNow();
		logEditText.getText();
		
		log += "- " + logTime.format("%k:%M:%S") + ": " + logNew;
		log += "\n" + logEditText.getText();
		
		logEditText.setText(log);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		return;
	}
}
