package edu.ucsc.teambacon.edibility;

import java.util.ArrayList;
import java.util.List;

import edu.ucsc.teambacon.edibility.ConfirmFoodDialog.ConfirmFoodDialogListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SubscribedAlertsActivity extends ActionBarActivity implements ConfirmFoodDialogListener {

    @SuppressWarnings("unused")
	private static final String LOG_TAG = "SubscribedAlerts";
    AlertsListAdapter adapter;
    private ArrayList<FoodItem> foodList;
	
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
        
        foodList = SavedPreferences.getInstance().getSavedFoodList();
        
        if ( !foodList.isEmpty()){
			// This is the array adapter, it takes the context of the activity as a
			// first parameter, the type of list view as a second parameter and your
			// array as a third parameter.
			adapter = new AlertsListAdapter(this, R.layout.alert_list_element,
					foodList);

			lv.setAdapter(adapter);

			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					ConfirmFoodDialog confirmFood = new ConfirmFoodDialog();
					confirmFood.setFoodName(adapter.getItem(arg2).getName());
					confirmFood.show(getSupportFragmentManager(),
							"confirm food");
				}
			}

			);
		} else {
        	showEmptyListDialog();
        }
	
	//end of onResume()
	}
	
	public void goBack() {
		NavUtils.navigateUpFromSameTask(this);
	}
	
	// Show a dialog when list is empty
	public void showEmptyListDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.empty_list_alert_title)
			.setMessage(R.string.empty_list_alert_text)
			.setCancelable(false)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					goBack();
				}
			})
			.setIcon(R.drawable.ic_launcher);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
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

	@Override
	public void onFinishFoodDialog() {
		foodList = SavedPreferences.getInstance().getSavedFoodList();
		adapter.notifyDataSetChanged();
		if (foodList.size() == 0) {
			showEmptyListDialog();
		}
	}

}
