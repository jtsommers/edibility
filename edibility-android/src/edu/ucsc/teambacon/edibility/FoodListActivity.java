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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import edu.ucsc.teambacon.edibility.deserialization.*;
import edu.ucsc.teambacon.edibility.headerlistview_source.*;

public class FoodListActivity extends ActionBarActivity {

	private static final String DOWNLOAD_URL = "http://www.kimonolabs.com/api/6guup5y4?apikey=e9c97d5dd3b6d537d322c030e00fa7a6";

	public static final String LOG_TAG = "FOODLIST";

	private HeaderListView list;
	private BackgroundDownloader downloader;
	private KimonoData data;
	public String dHall = "&locationNum=";
	public String dHallName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			dHallName = (extras.getString("name"));
			dHall += Utilities.getLocationCode(dHallName);

		}

		if (downloader == null
				|| downloader.getStatus() == BackgroundDownloader.Status.FINISHED) {
			downloader = new BackgroundDownloader(new ChoiceCompletion());
			downloader.execute(FoodListActivity.DOWNLOAD_URL + dHall);

			// downloader.execute("http://www.kimonolabs.com/api/6guup5y4?apikey=e9c97d5dd3b6d537d322c030e00fa7a6");
		}

	}

	public void createHeaderListView() {

		list = new HeaderListView(this);
		list.setAdapter(new SectionAdapter() {
			@Override
			public int numberOfSections() {
				int numMeal = data.results.food.size() + 1;
				return numMeal;
			}

			@Override
			public int numberOfRows(int section) {
				int numFood = 1;

				if (section > 0) {
					String mealName = data.results.meals.get(section - 1).mealName;
					if (mealName.contains("Lunch"))
						numFood = data.results.food.get(section).allFood.size();
					else if (mealName.contains("Dinner"))
						numFood = data.results.food.get(section - 2).allFood
								.size();
					else // breakfast
						numFood = data.results.food.get(section - 1).allFood
								.size();
				}

				return numFood;
			}

			@Override
			public Object getRowItem(int section, int row) {
				return null;
			}

			@Override
			public boolean hasSectionHeaderView(int section) {
				return true;
			}

			@Override
			public View getRowView(int section, int row, View convertView,
					ViewGroup parent) {

				if (convertView == null) {
					convertView = (TextView) getLayoutInflater().inflate(
							getResources().getLayout(
									android.R.layout.simple_list_item_1), null);
				}

				if (section > 0) {
					String mealName = data.results.meals.get(section - 1).mealName;

					if (mealName.contains("Lunch"))
						((TextView) convertView).setText(data.results.food
								.get(section).allFood.get(row));

					else if (mealName.contains("Dinner"))
						((TextView) convertView).setText(data.results.food
								.get(section - 2).allFood.get(row));

					else
						// if (mealName.contains("Breakfast") )
						((TextView) convertView).setText(data.results.food
								.get(section - 1).allFood.get(row));
				} else {
					((TextView) convertView).setText("Food Item");

				}

				return convertView;
			}

			@Override
			public int getSectionHeaderViewTypeCount() {

				return 2;
			}

			@Override
			public int getSectionHeaderItemViewType(int section) {
				return section % 2;
			}

			@Override
			public View getSectionHeaderView(int section, View convertView,
					ViewGroup parent) {

				if (convertView == null) {
					convertView = (TextView) getLayoutInflater().inflate(
							getResources().getLayout(
									android.R.layout.simple_list_item_1), null);
				}

				switch (section) {
				case 0:
					convertView.setBackgroundColor(getResources().getColor(
							R.color.holo_red_light));
					((TextView) convertView).setText(dHallName);
					break;
				case 1:
					convertView.setBackgroundColor(getResources().getColor(
							R.color.holo_orange_light));
					((TextView) convertView).setText(data.results.meals.get(0).mealName);
					break;
				case 2:
					((TextView) convertView).setText(data.results.meals.get(1).mealName);
					convertView.setBackgroundColor(getResources().getColor(
							R.color.holo_green_light));
					break;
				case 3:
					convertView.setBackgroundColor(getResources().getColor(
							R.color.holo_blue_light));
					((TextView) convertView).setText(data.results.meals.get(2).mealName);
					break;
				}
				return convertView;
			}

			@Override
			public void onRowItemClick(AdapterView<?> parent, View view,
					int section, int row, long id) {
				super.onRowItemClick(parent, view, section, row, id);

				if (section == 0)
					return;

				// ///// ==============
				Intent intent = new Intent(getApplicationContext(),
						SubscribedAlertsActivity.class);
				startActivity(intent);

				// ///// ===============

				// Toast.makeText(FoodListActivity.this, "Section " + section +
				// " Row " + row, Toast.LENGTH_SHORT).show();
			}
		});

		setContentView(list);

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
			// Sanitize string to remove empty data

			s = s.replaceAll(
					",\\n.*\\{\\n.*\\n.*\"allFood\": \"\",\\n.*\\n.*\\}", "");
			Gson gson = new Gson();

			// KimonoData data = null;
			data = null;
			try {
				data = gson.fromJson(s, KimonoData.class);
			} catch (JsonSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			createHeaderListView();
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
