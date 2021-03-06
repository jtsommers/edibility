package edu.ucsc.teambacon.edibility;

import java.util.HashMap;
import java.util.Locale;

import android.content.Context;

public class Utilities {
	
	private static HashMap<String, String> diningHalls;
	
	static public String parseString(String dhall, String food){
		//make food lowercase and remove spaces
		food = food.toLowerCase(Locale.US);
		food = food.replaceAll("[\\s,.'\";:?!/\\&@#$%^*()]", "");
		
		//create output string that follows "foodalert_dininghallcode_nameOfFood"
		String out = "foodalert_";
		out = out + getLocationCode(dhall) + "_" + food;
		
		return out;
	}
	
	// Populate the hashmap with dining hall codes used by FoodPro
	private static void createLocationCodeMap() {
		if (diningHalls == null) {
			diningHalls = new HashMap<String, String>();
			diningHalls.put("cowell", "05");
			diningHalls.put("crown", "20");
			diningHalls.put("eight", "30");
			diningHalls.put("nine", "40");
			diningHalls.put("porter", "25");
		}
	}
	
	public static String getLocationCode(String dhall) {
		createLocationCodeMap();
		return diningHalls.get(dhall);
	}

	// Function to get a string resource R.id."aString" by passing "aString"
	// http://stackoverflow.com/a/11595723
	public static String getStringResourceByName(String aString) {
		Context c = EdibilityApplication.getContext();
		String packageName = c.getPackageName();
		int resId = c.getResources()
				.getIdentifier(aString, "string", packageName);
		return c.getString(resId);
	}
}
