package edu.ucsc.teambacon.edibility;

import java.util.ArrayList;

public class SavedPreferences {

	private static SavedPreferences instance = null;
	public ArrayList<FoodItem> savedFood;
	
	protected SavedPreferences() {
		
	}
	
	public static SavedPreferences getInstance() {
		if(instance == null) {
			instance = new SavedPreferences();
			}
	
		return instance;   
	}
	
	public void addFood(FoodItem food) {
		instance.savedFood.add(food);
	}

	public void removeFood(FoodItem food) {
		instance.savedFood.remove(food);
	}
	
	public ArrayList<FoodItem> getSavedFoodList() {
		return instance.savedFood;
	}

	public FoodItem getFood(FoodItem f){
		int i = instance.savedFood.lastIndexOf(f);
		return instance.savedFood.get(i);
	}
}
