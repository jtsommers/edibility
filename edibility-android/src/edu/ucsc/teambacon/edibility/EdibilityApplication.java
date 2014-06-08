package edu.ucsc.teambacon.edibility;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

import android.app.Application;

public class EdibilityApplication extends Application {
	
	@Override
	public void onCreate() {
		// Application level onCreate method, called before any activity initialization
		super.onCreate();
		// Initialize Parse (Push Notifications)
		Parse.initialize(this, "ltuDyC6qDfdL7QLia9LIBF78VFKUPFmgluC6sftx", "VVG8Xk6L3j6kZ7jsuaWSJL1GYIk69tdSrmPZwSnv");
		// Set default activity to handle push notifications
		PushService.setDefaultPushCallback(this, LocationSelectionActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
		
		// Initialize shared prefs with application context
		SavedPreferences.getInstance(getApplicationContext());
	}
}
