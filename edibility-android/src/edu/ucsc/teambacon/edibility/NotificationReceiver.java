package edu.ucsc.teambacon.edibility;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.util.Log;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {
	
	
	public static final String LOG_TAG = "NotificationReceiver";
	public static final String PARSE_DATA_KEY         =   "com.parse.Data";
	public static final String PARSE_JSON_CHANNEL_KEY       =   "com.parse.Channel";
	
	 @Override
	 public void onReceive(Context context, Intent intent) {
		 

	     ArrayList<FoodItem> foodlist = SavedPreferences.getInstance().getSavedFoodList();
		 FoodItem foodItem;
		 String saved = "";
		 
		//Toast.makeText(context, "Push received!",Toast.LENGTH_SHORT).show();
		 try {
	            String action = intent.getAction();
	            String channel = intent.getExtras().getString( PARSE_JSON_CHANNEL_KEY );
	            
	            for (int i = 0; i < foodlist.size(); i ++){
	            	foodItem = foodlist.get(i);
	            	
	            	if (channel.indexOf(foodItem.channelName) != -1)	{
	            		saved = foodItem.displayName;
	            	}
	            }
	           
	            JSONObject json = new JSONObject(intent.getExtras().getString(PARSE_DATA_KEY ));
	        
	           // String var = json.getString("header"); 
	            String college =  Utilities.getStringResourceByName(json.getString("college"));
	          
	            //TODO : a better message maybe
	            String message = college + " now has " + saved;
	            
	            // Calls a function to define the notification ( rington,content);
	            defineNotification(context,  message);
	        } catch (Exception e) {
	            Log.d(LOG_TAG, "JSONException: " + e.getMessage());
	        }

	}
	 
	 public static void defineNotification(Context context,  String message) {
	        // Show the notification
	        long when = System.currentTimeMillis();
	        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	        
	        //TODO - change square-shape launching icon
	        Notification notification = new Notification(R.drawable.ic_launcher, message,when);
	       
	        String title = context.getString(R.string.app_name);
	        
	        Intent notificationIntent = new Intent(context, NotificationReceiver.class);

	        // set intent so it does not start a new activity
	        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
	        notification.setLatestEventInfo(context, title, message, intent);
	        notification.vibrate = new long[] { 500, 500 };
	        notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

	        notification.flags = 
	            Notification.FLAG_AUTO_CANCEL | 
	            Notification.FLAG_SHOW_LIGHTS;

	        notificationManager.notify(0, notification);
	    }
}
