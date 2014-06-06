package edu.ucsc.teambacon.edibility;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class LocationSelectionActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_selection);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		// Initialize Parse (Push Notifications)
		Parse.initialize(this, "ltuDyC6qDfdL7QLia9LIBF78VFKUPFmgluC6sftx", "VVG8Xk6L3j6kZ7jsuaWSJL1GYIk69tdSrmPZwSnv");
		// Set default activity to handle push notifications
		PushService.setDefaultPushCallback(this, LocationSelectionActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
		// TODO: Remove this test subscription
		PushService.unsubscribe(getApplicationContext(), "testchannel");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location_selection, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_location_selection, container, false);
			return rootView;
		}
	}
	
	public void porter(View v)
	{
		Intent intent = new Intent(getBaseContext(), FoodListActivity.class);
		intent.putExtra("name", "porter");
		startActivity(intent);
	}

	public void eight(View v)
	{
		Intent intent = new Intent(getBaseContext(), FoodListActivity.class);
		intent.putExtra("name", "eight");
		startActivity(intent);
	}

	public void nine(View v)
	{
		Intent intent = new Intent(getBaseContext(), FoodListActivity.class);
		intent.putExtra("name", "nine");
		startActivity(intent);
	}
	
	public void crown(View v)
	{
		Intent intent = new Intent(getBaseContext(), FoodListActivity.class);
		intent.putExtra("name", "crown");
		startActivity(intent);
	}
	
	public void cowell(View v)
	{
		Intent intent = new Intent(getBaseContext(), FoodListActivity.class);
		intent.putExtra("name", "cowell");
		startActivity(intent);
	}
	
	public void alerts(View v)
	{
		Intent intent = new Intent(getBaseContext(), SubscribedAlertsActivity.class);
		startActivity(intent);
	}
}
