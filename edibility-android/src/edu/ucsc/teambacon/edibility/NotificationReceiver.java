package edu.ucsc.teambacon.edibility;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {
	
	
	public static final String PARSE_DATA_KEY         =   "com.parse.Data";
	public static final String PARSE_JSON_CHANNEL_KEY       =   "com.parse.Channel";

	
	 // Receive notification. It does not think but gives out a toast at this point
	 //TODO need to add codes to push notification for users.
	 @Override
	 public void onReceive(Context context, Intent intent) {
		 
		 Toast.makeText(context, "Push received!",Toast.LENGTH_SHORT).show();
	      String action = intent.getAction();
	         String channel = intent.getExtras().getString(PARSE_JSON_CHANNEL_KEY);
	         try {
				JSONObject json = new JSONObject(intent.getExtras().getString(PARSE_DATA_KEY));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	     }

	 	
	

	}
