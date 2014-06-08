package edu.ucsc.teambacon.edibility;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class SavedPreferences {

	private static final String SAVED_PREFS = "EdibilityPreferences";
	private static final String STORED_GSON = "GsonData";

	private static SavedPreferences instance = null;
	public SavedFood savedFood;
	private SharedPreferences settings = null;
	private static Context applicationContext;
	
	public class SavedFood {
		public ArrayList<FoodItem> foodPrefs;
		
		public SavedFood() {
			this.foodPrefs = new ArrayList<FoodItem>();
		}
	}

	protected SavedPreferences() {
		if (applicationContext != null) {
			settings = applicationContext.getSharedPreferences(SAVED_PREFS, Context.MODE_PRIVATE);
			String json = settings.getString(STORED_GSON, "");
			Gson g = new Gson();
			try {
				savedFood = g.fromJson(json, SavedFood.class);
			} catch (JsonParseException e) {
				// Presumably this was due to an updated data class
				// Right now just tosses old saved data
				Log.e(SAVED_PREFS, "JSON Exception in saved preferences");
			}
			if (savedFood == null) {
				savedFood = new SavedFood();
				Log.i(SAVED_PREFS, "Unable to load preferences");
			}
		} else {
			Log.e(SAVED_PREFS, "Settings not initialized, call getInstance(context) first");
		}
	}

	public static SavedPreferences getInstance() {
		if (instance == null) {
			instance = new SavedPreferences();
		}

		return instance;
	}
	
	// This method should always be called first to set the application context
	// Really poor code, but quick and dirty
	public static SavedPreferences getInstance(Context c) {
		applicationContext = c;
		if (instance == null) {
			instance = new SavedPreferences();
		}
		
		return instance;
	}
	
	private void commitToPrefs() {
		if (settings != null) {
			SharedPreferences.Editor e = settings.edit();
			Gson g = new Gson();
			String json = g.toJson(savedFood);
			e.putString(STORED_GSON, json);
			e.commit();
		} else {
			Log.i(SAVED_PREFS, "Attempted to save to null settings");
		}
	}

	public void addFood(FoodItem food) {
		instance.savedFood.foodPrefs.add(food);
		instance.commitToPrefs();
	}

	public void removeFood(FoodItem food) {
		instance.savedFood.foodPrefs.remove(food);
		instance.commitToPrefs();
	}

	public ArrayList<FoodItem> getSavedFoodList() {
		return instance.savedFood.foodPrefs;
	}

	public FoodItem getFood(FoodItem f) {
		int i = instance.savedFood.foodPrefs.lastIndexOf(f);
		return instance.savedFood.foodPrefs.get(i);
	}
	
}
