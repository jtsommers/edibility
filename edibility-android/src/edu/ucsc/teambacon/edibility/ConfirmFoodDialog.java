package edu.ucsc.teambacon.edibility;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ConfirmFoodDialog extends DialogFragment {
	
	String foodName = "";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		// Where we track the selected items
		final ArrayList<Integer> selectedDHalls = new ArrayList<Integer>();
		
		//create a new food object
		final FoodItem food = new FoodItem(this.foodName);
		
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Set the dialog title
	    builder.setTitle(foodName)
	    // Specify the list array, the items to be selected by default (null for none),
	    // and the listener through which to receive callbacks when items are selected
	           .setMultiChoiceItems(R.array.names, null,
	                      new DialogInterface.OnMultiChoiceClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int which,
	                       boolean isChecked) {
	                   if (isChecked) {
	                       // If the user checked the item, add it to the selected items
	                       selectedDHalls.add(which);
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
	                       
	                   } else if (selectedDHalls.contains(which)) {
	                       // Else, if the item is already in the array, remove it 
	                       selectedDHalls.remove(Integer.valueOf(which));
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
	           .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   // User clicked OK, so save the mSelectedItems results somewhere
	                   // or return them to the component that opened the dialog
	            	   if(!food.getLocations().isEmpty())
	            		   SavedPreferences.getInstance().addFood(food);
	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	               
	               }
	           });

	    return builder.create();
	}

	public void setFoodName(String foodName) {
		this.foodName=foodName;
	}
}
