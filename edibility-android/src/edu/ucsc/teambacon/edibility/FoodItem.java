package edu.ucsc.teambacon.edibility;

import java.util.ArrayList;

public class FoodItem {

	String name = "";
	ArrayList<String> locations;
	
	public FoodItem(String name){
		this.name = name;
		this.locations = new ArrayList<String>();
	}

	public FoodItem(String name, ArrayList<String> locations){
		this.name = name;
		this.locations = locations;
	}
	
	public String getName(){
		return this.name;
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
