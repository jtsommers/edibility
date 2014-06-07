package edu.ucsc.teambacon.edibility.deserialization;

import java.util.ArrayList;

public class MealFoodList {
	public String firstItem;
	public ArrayList<String> allFood;
	public String lastItem;
	
	public int length() {
		return allFood.size();
	}
	
	public String foodAtIndex(int i) {
		return allFood.get(i);
	}
}
