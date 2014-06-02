package edu.ucsc.teambacon.edibility;

import java.util.HashMap;
import java.util.Locale;

public class Utilities {
	
	private static HashMap<String, String> diningHalls;
	
	static public String parseString(String dhall, String food){
		//make food lowercase and remove spaces
		food.toLowerCase(Locale.US);
		food.replaceAll(" ", "");
		
		//create output string that follows "dininghallcode_nameoffood"
		String out = "";
		out.concat(getLocationCode(dhall));
		out.concat("_");
		out.concat(food);
		
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


}
