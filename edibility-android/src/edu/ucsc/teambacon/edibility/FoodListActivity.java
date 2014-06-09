package edu.ucsc.teambacon.edibility;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import edu.ucsc.teambacon.edibility.deserialization.KimonoData;
import edu.ucsc.teambacon.edibility.deserialization.MealFoodList;
import edu.ucsc.teambacon.edibility.deserialization.MealType;
import edu.ucsc.teambacon.edibility.headerlistview_source.HeaderListView;
import edu.ucsc.teambacon.edibility.headerlistview_source.SectionAdapter;

public class FoodListActivity extends ActionBarActivity {

	private static final String DOWNLOAD_URL 
	                     = "http://www.kimonolabs.com/api/6guup5y4?apikey=e9c97d5dd3b6d537d322c030e00fa7a6";

	public static final String LOG_TAG = "FOODLIST";

	private HeaderListView list;
	private BackgroundDownloader downloader;

	private KimonoData data = null;


	private ArrayList<MealFoodList> foods;
	private ArrayList<String> meals;

	public String dHall = "&locationNum=";
	//public String dHallCode = null;
	
	private ProgressDialog progressDialog;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String dHallName = extras.getString("name");
			dHall += Utilities.getLocationCode(dHallName);
			
			// Set the action bar title
			setTitle(Utilities.getStringResourceByName(dHallName));
		} else {
			Log.d(LOG_TAG, "Not reading extras");
		}

		if (downloader == null
				|| downloader.getStatus() == BackgroundDownloader.Status.FINISHED) {
            // Show the progress dialog
            progressDialog = ProgressDialog.show(FoodListActivity.this,"Loading...",  
            	    "Loading application View, please wait...", false, false);
            
            // Start the background downloader
			downloader = new BackgroundDownloader(new ChoiceCompletion());
			downloader.execute(FoodListActivity.DOWNLOAD_URL + dHall);

			// downloader.execute("http://www.kimonolabs.com/api/6guup5y4?apikey=e9c97d5dd3b6d537d322c030e00fa7a6");
		}

	}

	// it's called inside of the execute function
	public void createHeaderListView() {

		list = new HeaderListView(this);
	//	list.getListView().setBackgroundColor(getResources().getColor(R.color.list_blue));
	//	list.getListView().setBackgroundDrawable(getWallpaper());
		
		list.setAdapter(new SectionAdapter() {
		
			
			@Override
			public int numberOfSections() {
				
				if (data != null) return  foods.size();
				
				return 1;
			}

			@Override
			public int numberOfRows(int section) {
				
				if ( data != null)
					return foods.get(section).allFood.size();

				return 0;
			}

			@Override
			public Object getRowItem(int section, int row) { 
				return foods.get(section).foodAtIndex(row);
			}

			@Override
			public boolean hasSectionHeaderView(int section) { return true; }

			@Override
			public View getRowView(int section, int row, View convertView, ViewGroup parent) {

				if (convertView == null) {
					convertView = (TextView) getLayoutInflater().inflate( getResources().getLayout(
									android.R.layout.simple_list_item_1), null);
				}				
			
				Typeface typeface2 = Typeface.createFromAsset(getAssets(), "AlteHaasGroteskRegular.ttf");
				
				
				//convertView.setBackgroundColor(R.color.blue);
				//convertView.setBackgroundColor(getResources().getColor(R.color.silver));

				
				((TextView) convertView).setText(foods.get(section).allFood.get(row));
				((TextView) convertView).setBackgroundColor(getResources().getColor(R.color.list_blue));
				((TextView) convertView).setTypeface(typeface2);

				//((TextView) convertView).setTextColor(getResources().getColor(R.color.selector_white_gray));

				((TextView) convertView).setTextColor(getResources().getColor(R.drawable.selector_white_gray));

				

				
				
				return convertView;
			}

			@Override
			public int getSectionHeaderViewTypeCount() { return 2; }

			@Override
			public int getSectionHeaderItemViewType(int section) { return section % 2; }

			@Override
			public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {

				if (convertView == null) {
					convertView = (TextView) getLayoutInflater().inflate(
							getResources().getLayout(
									android.R.layout.simple_list_item_1), null);
				}
				
				Typeface typeface1 = Typeface.createFromAsset(getAssets(), "AlteHaasGroteskBold.ttf");				
				
				String mealName = data.results.meals.get(section).mealName;
				((TextView) convertView).setText(mealName);
				switch (mealName.substring(0, mealName.indexOf(" ")).toLowerCase()){
					case "breakfast":
						convertView.setBackgroundColor(getResources().getColor(R.color.header_orange));
					
						break;
					case "lunch":
						convertView.setBackgroundColor(getResources().getColor(R.color.header_green));
						//((TextView) convertView).setText(mealName);
						break;
					case "dinner":
						convertView.setBackgroundColor(getResources().getColor(R.color.header_red));
						//((TextView) convertView).setText(mealName);
						break;
						
				}
					
				
				return convertView;
			}

			@Override
			public void onRowItemClick(AdapterView<?> parent, View view,
					int section, int row, long id) {
				super.onRowItemClick(parent, view, section, row, id);

		
				
				//display  dialog
				ConfirmFoodDialog confirmFood = new ConfirmFoodDialog();
			    confirmFood.setFoodName((String)getRowItem(section, row));
			    confirmFood.show(getSupportFragmentManager(), "confirm food");
			    
				// Toast.makeText(FoodListActivity.this, "Section " + section +
				// " Row " + row, Toast.LENGTH_SHORT).show();
			}
		});

		setContentView(list);

		list.getListView().setDivider(new ColorDrawable(0x000404));
		list.getListView().setDividerHeight(2);
	}

	
	
	
	/*
	 * protected void onResume() { super.onResume();
	 * 
	 * 
	 * 
	 * }
	 */
	
	

	private class ChoiceCompletion extends BDCompletionTask {

		@Override
		void execute(String s) {
			progressDialog.dismiss();
			
			// Sanitize string to remove empty data
			
			if ( s != null){
				s = s.replaceAll(
						",\\n.*\\{\\n.*\\n.*\"allFood\": \"\",\\n.*\\n.*\\}",
						"");
				Gson gson = new Gson();

				data = null;
				try {
					data = gson.fromJson(s, KimonoData.class);
				} catch (JsonSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (data.lastrunstatus.equalsIgnoreCase("failure")) {
					data = null;
				} 
				
				
				sanitizeData();

				createHeaderListView();
			}
			else{
				setContentView(R.layout.activity_food_list);
				TextView text = (TextView) findViewById(R.id.textView1);
				text.setText("  No internet connection.");	
			}		
		}
		
		private void sanitizeData() {
			// Sanity checks
			if (data != null && data.results.meals != null && data.results.meals.size() > 0) {
				meals = new ArrayList<String>();
				foods = new ArrayList<MealFoodList>();
				ArrayList<MealType> m = data.results.meals;
				// Push meal name onto list
				for (int i = 0; i < m.size(); i++) {
					meals.add(m.get(i).mealName);
				}
				// Save food lists into local array "foods"
				// Some crazy logic here to handle dinner always coming before lunch
				int numMeals = meals.size();
				
				
				MealFoodList breakfast = null, lunch = null, dinner = null;
				if (numMeals > 1) {
					
					int mLength_1 = data.results.food.get(numMeals - 1).allFood.size();
					int mLength_2 = data.results.food.get(numMeals - 2).allFood.size();
					
					if ( mLength_1 > mLength_2){
						
						// This should cover all cases, even weekends have lunch(brunch) and dinner
						// Last food list references lunch
						lunch = data.results.food.get(numMeals - 1);
						// Second to last food list references dinner
						dinner = data.results.food.get(numMeals - 2);
					}
					else {
						lunch = data.results.food.get(numMeals - 2);
						dinner = data.results.food.get(numMeals - 1);
					}
					
					// Check for breakfast
					if (numMeals > 2) {
						breakfast = data.results.food.get(0);
					}
				} else {
					// Only one meal, this shouldn't happen, I think, but handle it anyway
					breakfast = data.results.food.get(0);
				}
				// Push meals onto food list
				if (breakfast != null) {
					foods.add(breakfast);
				}
				if (lunch != null) {
					foods.add(lunch);
				}
				if (dinner != null) {
					foods.add(dinner);
				}
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

}
