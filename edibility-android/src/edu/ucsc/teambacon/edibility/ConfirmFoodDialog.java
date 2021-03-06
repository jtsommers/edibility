package edu.ucsc.teambacon.edibility;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class ConfirmFoodDialog extends DialogFragment {
	
	public interface ConfirmFoodDialogListener {
		void onFinishFoodDialog();
	}
	
	String foodName = "";
	boolean alreadySaved;
	private static final String LOG_TAG = "ConfirmFoodDialog";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		alreadySaved = false;
	    final FoodItem food;
	    FoodItem temp = null;
				
		//create a new food object
	    ArrayList<FoodItem> saved = SavedPreferences.getInstance().getSavedFoodList();
		for(FoodItem check: saved)
		{
			if(check.getName().equalsIgnoreCase(this.foodName))
			{
				alreadySaved = true;
				temp = check;
				break;
			}
		}
		
		if(!alreadySaved)
		{
			food = new FoodItem(this.foodName);
		}
		else
		{
			food = temp;
		}		

	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Set the dialog title
	    builder.setTitle(foodName)
	    // Specify the list array, the items to be selected by default (null for none),
	    // and the listener through which to receive callbacks when items are selected
	           .setMultiChoiceItems(R.array.names, setCheckMarks(food),
	               new DialogInterface.OnMultiChoiceClickListener() {
	        	   
	        	   
	               @Override
	               public void onClick(DialogInterface dialog, int which,
	                       boolean isChecked) {

	            	   if (isChecked) {
	                       // If the user checked the item, add it to the selected items
	                       	switch(which){
	                    	   case 0: food.addLocation("porter");
	                    		   break;
	                    	   case 1: food.addLocation("eight");
	                    		   break;
	                    	   case 2: food.addLocation("nine");
	                    		   break;
	                    	   case 3: food.addLocation("crown");
	                    		   break;
	                    	   case 4: food.addLocation("cowell");
	                    		   break;
	                       }
	                       
	                   } else {
	                       // Else, if the item is already in the array, remove it 
	                       
	                	switch(which){
                    	   case 0: food.removeLocation("porter");
                    		   break;
                    	   case 1: food.removeLocation("eight");
                    		   break;
                    	   case 2: food.removeLocation("nine");
                    		   break;
                    	   case 3: food.removeLocation("crown");
                    		   break;
                    	   case 4: food.removeLocation("cowell");
                    		   break;
	                       }
	                   }
	               }
	           })
	    // Set the action buttons
	           .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   // User clicked OK, so save the mSelectedItems results somewhere
	                   // or return them to the component that opened the dialog
	            	   
	            	   if(!food.getLocations().isEmpty() && !alreadySaved)
	            		   SavedPreferences.getInstance().addFood(food);
	            	   if(food.getLocations().isEmpty() && alreadySaved)
	            		   SavedPreferences.getInstance().removeFood(food);
	            	   try {
		            	   ConfirmFoodDialogListener activity = (ConfirmFoodDialogListener) getActivity();
		                   activity.onFinishFoodDialog();
	            	   } catch (ClassCastException e) {
	            		   Log.i(LOG_TAG, "Class cast failed, this is only an error if in SubscribedAlertsActivity");
	            	   }
	               }
	           })
	           .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	               
	               }
	           });

	    return builder.create();
	}

	public void setFoodName(String foodName) {
		this.foodName=foodName;
	}
	
	public boolean[] setCheckMarks(FoodItem foodToBe){
		
		boolean[] checks = new boolean[5];
		
		if(alreadySaved){
			
			for(String s: foodToBe.getLocations()){
				
				switch(s){
				case "porter": checks[0]=true;
					break;
				case "eight": checks[1]=true;
					break;
				case "nine": checks[2]=true;
					break;
				case "crown": checks[3]=true;
					break;
				case "cowell": checks[4]=true;
					break;
				}
			}
		}
		else
		{
			for(int i=0; i<5; ++i)
			{
				checks[i]=false;
			}
			
		}
			
		return checks;
		
	}
}
