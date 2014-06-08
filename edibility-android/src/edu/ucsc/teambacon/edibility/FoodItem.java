package edu.ucsc.teambacon.edibility;

import java.util.ArrayList;
import java.util.Locale;

import android.util.Log;

import com.parse.PushService;

public class FoodItem {

	String displayName = "";
	String channelName = ""; // last part-- no wierd fuhao
	ArrayList<String> locations;
	
	private static final String LOG_TAG = "FoodItem";
	
	public FoodItem(String name){
		this.displayName = name;

		this.channelName = name;
		this.channelName.toLowerCase(Locale.US);
		this.channelName.replaceAll(" ", "");
		this.channelName.replaceAll("[,.'\";:?!/&@#$%^*()]", "");
		
		this.locations = new ArrayList<String>();
	}

	public FoodItem(String name, ArrayList<String> locations){
		this.displayName = name;
		this.locations = locations;
	}
	
	public String getName(){
		return this.displayName;
	}
	
	public String getChannelName(){
		return this.channelName;
	}
	
	public ArrayList<String> getLocations(){
		return this.locations;
	}
	
	public void addLocation(String dhall){
		this.locations.add(dhall);
		// Add to parse subscriptions
		String channel = Utilities.parseString(dhall, this.displayName);
	//	Log.i(LOG_TAG, "Unsubscribing from " + channel);
		PushService.subscribe(
				EdibilityApplication.getContext(), 
				channel, 
				LocationSelectionActivity.class);
	}

	public void removeLocation(String dhall){
		this.locations.remove(dhall);
		// Unsubscribe from channel
		String channel = Utilities.parseString(dhall, this.displayName);
	//	Log.i(LOG_TAG, "Unsubscribing from " + channel);
		PushService.unsubscribe(
				EdibilityApplication.getContext(), 
				channel);
	}
	
	public String getLocationDisplay() {
		String ret = "";
		for (String s: locations) {
			ret += (Utilities.getStringResourceByName(s) + ", ");
		}
		return ret;
	}
}
