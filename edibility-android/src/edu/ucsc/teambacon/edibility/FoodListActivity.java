package edu.ucsc.teambacon.edibility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import edu.ucsc.teambacon.edibility.deserialization.*;

public class FoodListActivity extends ActionBarActivity {

	private static final String 
	DOWNLOAD_URL = 
	"http://www.kimonolabs.com/api/6guup5y4?apikey=e9c97d5dd3b6d537d322c030e00fa7a6";
		
	public static final String LOG_TAG = "FOODLIST";

	private ChoiceAdapter adapter;
	public ArrayList<String> list; // as global
	private BackgroundDownloader downloader;
	
	private String[] header = new String[]{"Breakfast", "Dinner", "Lunch"};
	
	
	// dHall will get the dHall code, and be appended to DOWNLOAD_URL,  
	// without dHall code, the sample data will be fetched.
	public String dHall = "&locationNum=";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_list);

		Bundle extras = getIntent().getExtras();
		if ( extras != null){
			dHall += Utilities.getLocationCode(extras.getString("name"));
		}//*/
		
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		list = new ArrayList<String>();
	
	}

	protected void onResume() {
		super.onResume();

		// Sanity check
		if (list == null) {
			list = new ArrayList<String>();
		}// */

		// Set up adapter if it hasn't already been created
		if (adapter == null) {
			adapter = new ChoiceAdapter(this, R.layout.element_food_list, list);
			((ListView) findViewById(R.id.food_listView)).setAdapter(adapter);
		}
		
		// Download and popuplate list data here, don't reset data unless list
		// is empty
		if (list.size() < 1) {
			reloadSources();
		}
	}

	// Clear list if it exists and redownload new sources
	public void reloadSources() {
		// Only start a new download if previous download is not running
		if (downloader == null
				|| downloader.getStatus() == BackgroundDownloader.Status.FINISHED) {
			downloader = new BackgroundDownloader(new ChoiceCompletion());
			
			downloader.execute(FoodListActivity.DOWNLOAD_URL+dHall);
		}
	}// mark

	private class ChoiceCompletion extends BDCompletionTask {

		@Override
		void execute(String s) {
			// Sanitize string to remove empty data
		
			s = s.replaceAll(",\\n.*\\{\\n.*\\n.*\"allFood\": \"\",\\n.*\\n.*\\}", "");
			Gson gson = new Gson();

			KimonoData data = null;
			try {
				data = gson.fromJson(s, KimonoData.class);
			} catch (JsonSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TODO: This is a temporary measure to list only the first meal
			if (data != null) {
				ArrayList<MealFoodList> foodList = data.results.food;	
				
				// stores the mis-ordered dinner items.
				ArrayList<String> dinnerItem = new ArrayList<String>(); 
				
				if (list == null) {
					list = new ArrayList<String>();
				} else {
					list.clear();
				}

				for (int i = 0; i < foodList.size(); i++) {

					ArrayList <String> item = foodList.get(i).allFood;
					
					if ( i == 1 ){	
						dinnerItem.add(header[i]);
						
						for (int k = 0; k < item.size(); k++ ){
							dinnerItem.add(item.get(k));
						}
					}
					else{
						list.add(header[i]); // add the title: Breakfast, Lunch
						for (int j = 0; j < item.size(); j++){			
							list.add(item.get(j));
						}
					}

				}// */
			
				for ( int i = 0; i < dinnerItem.size(); i++ ){
					list.add(dinnerItem.get(i));
				}
				adapter.notifyDataSetChanged();
			} else {
				Log.d(LOG_TAG, "Data not set");
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.food_list, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_food_list,
					container, false);
			return rootView;
		}
	}

	private class ChoiceAdapter extends ArrayAdapter<String> {

		int resource;
		@SuppressWarnings("unused")
		Context context;

		public ChoiceAdapter(Context _context, int _resource, List<String> items) {

			super(_context, _resource, items);
			resource = _resource;
			context = _context;
			this.context = _context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
	
			LinearLayout newView;
	
			String item = getItem(position);

			// Inflate a new view if necessary.
			if (convertView == null) {
				newView = new LinearLayout(getContext());
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater vi = (LayoutInflater) getContext()
						.getSystemService(inflater);
				vi.inflate(resource, newView, true);
			} else {
				newView = (LinearLayout) convertView;
			}

			Button b = (Button) newView.findViewById(R.id.listButton);
		
			b.setText(item.toString());
       
			// Sets a listener for the button, and a tag for the button as well.
			b.setTag(Integer.toString(position));
			b.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// React to button according to tag/index
					String s = (String) v.getTag();		
					int pos = Integer.parseInt(s);		
					
					String item = getItem(pos); // item is the name of food

					// if the name matches the header, do nothing
					for ( int i = 0; i< header.length; i++)
						if ( item.compareTo(header[i]) == 0)
							return;

					Intent intent = new Intent(getApplicationContext(),
							SubscribedAlertsActivity.class);
					startActivity(intent);
				}
			}); // */

			return newView;
		}
	}

}
