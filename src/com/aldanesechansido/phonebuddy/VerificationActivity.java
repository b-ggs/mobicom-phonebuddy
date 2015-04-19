package com.aldanesechansido.phonebuddy;

import java.util.Random;

import android.accounts.Account;
import android.accounts.OnAccountsUpdateListener;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VerificationActivity extends Activity {
	
	private Button confirmButton;
	private Button resendButton;
	private EditText inputVerKey;
	private String buddyNumber;
	private int genVerKey;
	private int inVerKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verification);
		getActionBar().hide();
		
		genVerKey = getIntent().getExtras().getInt("verkey");
		confirmButton = (Button) findViewById(R.id.confirmVerKey);
		confirmButton.setOnClickListener(confirmVerkey);
		resendButton = (Button) findViewById(R.id.resend);
		resendButton.setOnClickListener(resendVerKey);
		
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNotificationManager.cancel(0);
		
		DataManager.getInstance().reset();
		
// 		Toast.makeText(getBaseContext(), "Key: " + genVerKey, Toast.LENGTH_LONG).show();
	}
	
	OnClickListener confirmVerkey = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			inputVerKey = (EditText) findViewById(R.id.verKeyInput);
			inVerKey = Integer.parseInt(inputVerKey.getText().toString());
			
			System.out.println("ONCLICK BEFORE CHECHING");
			System.out.println("genKey:"+genVerKey);
			System.out.println("inputKey:"+inVerKey);
			if(inVerKey == genVerKey)
			{

				buddyNumber = getIntent().getExtras().getString("buddynumber");
				Intent i = new Intent();
				i.putExtra("buddynumber", buddyNumber);
				i.setClass(getBaseContext(), ServiceActivity.class);
//				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(i);
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Wrong Verification Code", Toast.LENGTH_LONG).show();
				System.out.println("WRONG VERI");
				System.out.println("genKey:"+genVerKey);
				System.out.println("inputKey:"+inVerKey);
			}
			
			
		}
	};

	OnClickListener resendVerKey = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			genVerKey = getVerKey();
			buddyNumber = getIntent().getExtras().getString("buddynumber");
			
			String message = "You verification code is " + genVerKey + ". Enter the code to start using this number as your buddy!";
			
			
			SmsManager manager = SmsManager.getDefault();
			manager.sendTextMessage(buddyNumber, null, message, null, null);
			Toast.makeText(getApplicationContext(), "Verification sent!",Toast.LENGTH_LONG).show();
		}
	};
	
	public int getVerKey() {
		// TODO Auto-generated method stub
		Random r = new Random();
		return r.nextInt(9999-1000)+1000;
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.verification, menu);
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
