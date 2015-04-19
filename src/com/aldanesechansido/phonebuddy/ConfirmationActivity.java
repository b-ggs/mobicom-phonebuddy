package com.aldanesechansido.phonebuddy;

import java.util.Random;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.telephony.SmsManager;

public class ConfirmationActivity extends Activity {

	private Button confirm;
	private EditText phoneNumber;
	private Spinner countrySpinner;
	private String buddyNumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirmation);
		getActionBar().hide();
		
		confirm = (Button) findViewById(R.id.confirm_button);
		phoneNumber = (EditText) findViewById(R.id.phone_number);
		countrySpinner = (Spinner) findViewById(R.id.country_spinner);
		
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNotificationManager.cancel(0);
		
		DataManager.getInstance().reset();
		
		confirm.setOnClickListener(confirmNumber);
	}
	
	OnClickListener confirmNumber = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			String[] buddyPrefixTemp;
			
			buddyPrefixTemp = countrySpinner.getSelectedItem().toString().split("\\(");
			buddyPrefixTemp = buddyPrefixTemp[1].split("\\)");
			//String countryCode = countrySpinner.getSelectedItem().toString();
			String phoneNum = phoneNumber.getText().toString();
			buddyNumber = "";
			
			buddyNumber += buddyPrefixTemp[0];
			buddyNumber += phoneNum;
			buddyNumber = buddyNumber.replace(" ", "");
			int verKey = getVerKey();
			String message = "PhoneBuddy:\nYour verification code is " + verKey + ". Enter the code to start using this number as your buddy!";
			
			
			SmsManager manager = SmsManager.getDefault();
			manager.sendTextMessage(buddyNumber, null, message, null, null);
			Toast.makeText(getApplicationContext(), "Verification sent to " + buddyNumber + "!",Toast.LENGTH_SHORT).show();
			
			Intent i = new Intent();
			i.putExtra("verkey", verKey);
			i.putExtra("buddynumber", buddyNumber);
			i.setClass(getBaseContext(), VerificationActivity.class);
			startActivity(i);
			
		}

		private int getVerKey() {
			// TODO Auto-generated method stub
			Random r = new Random();
			return r.nextInt(9999-1000)+1000;
			
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.confirmation, menu);
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
