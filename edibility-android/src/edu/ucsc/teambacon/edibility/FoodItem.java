package edu.ucsc.teambacon.edibility;

import java.util.ArrayList;
import java.util.Locale;

public class FoodItem {

	String displayName = "";
	String channelName = "";
	ArrayList<String> locations;
	
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
	}

	public void removeLocation(String dhall){
		this.locations.remove(dhall);
	}
}
