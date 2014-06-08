package edu.ucsc.teambacon.edibility;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;

public class SubscribedAlertsActivity extends ActionBarActivity {

	
    private ListView lv;
    private static final String LOG_TAG = "SubscribedAlerts";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subscribed_alerts);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.subscribed_alerts, menu);
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
	
	@Override
	public void onResume() {
		super.onResume();
        ListView lv = (ListView) findViewById(R.id.listView1);
        
        ArrayList<FoodItem>foodList = SavedPreferences.getInstance().getSavedFoodList();
        ArrayList<String> foodNameList = new ArrayList<String>();
        
        for(FoodItem food:foodList)
        {
        	foodNameList.add(food.displayName);
        }

        // This is the array adapter, it takes the context of the activity as a 
        // first parameter, the type of list view as a second parameter and your 
        // array as a third parameter.
        AlertsListAdapter adapter = new AlertsListAdapter(this, R.layout.alert_list_element, foodList);

        lv.setAdapter(adapter); 	
        
//        lv.setOnItemClickListener(new OnItemClickListener() {
//        	@Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { 
//        		
//        	}
//        }
//	);
	
	
	//end of onResume()
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
					R.layout.fragment_subscribed_alerts, container, false);
			return rootView;
		}
	}
	
	// Custom adapter
	private class AlertsListAdapter extends ArrayAdapter<FoodItem>{

		int resource;
		@SuppressWarnings("unused")
		Context context;
		
		public AlertsListAdapter(Context _context, int _resource, List<FoodItem> items) {
			super(_context, _resource, items);
			resource = _resource;
			context = _context;
			this.context = _context;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout newView;
			
			FoodItem item = getItem(position);
			
			// Inflate a new view if necessary.
			if (convertView == null) {
				newView = new LinearLayout(getContext());
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
				vi.inflate(resource,  newView, true);
			} else {
				newView = (LinearLayout) convertView;
			}
			
			// Fills in the view.
			TextView tv = (TextView) newView.findViewById(R.id.alertListItemText);
			tv.setText(item.displayName);
			TextView detail = (TextView) newView.findViewById(R.id.alertListDetailText);
			detail.setText(item.getLocationDisplay());

			return newView;
		}		
	}

}
