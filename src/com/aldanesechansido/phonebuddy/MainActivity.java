package com.aldanesechansido.phonebuddy;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		getActionBar().hide();
		Thread logoTimer = new Thread()
		{
			public void run()
			{
				try
				{
					int logoTimer = 0;
					while(logoTimer < 5000)
					{
						sleep(100);
						logoTimer = logoTimer + 100;
					};
					Intent i = new Intent();
					i.setClass(getBaseContext(), ConfirmationActivity.class);
					startActivity(i);
				
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
				finally
				{
					finish();
				}
			}
		};
		
		logoTimer.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
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
