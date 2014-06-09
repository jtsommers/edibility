package edu.ucsc.teambacon.edibility;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

	public static final String LOG_TAG = "NotificationReceiver";
	public static final String PARSE_DATA_KEY = "com.parse.Data";
	public static final String PARSE_JSON_CHANNEL_KEY = "com.parse.Channel";
	public static String college = "";

	@Override
	public void onReceive(Context context, Intent intent) {

		ArrayList<FoodItem> foodList = SavedPreferences.getInstance()
				.getSavedFoodList();
		FoodItem foodItem;
		String food = null;
		String message = null;

		// if there is food subscribed
		if (!foodList.isEmpty()) {
			try {
				String action = intent.getAction();
				String channel = intent.getExtras().getString(
						PARSE_JSON_CHANNEL_KEY);

				for (int i = 0; i < foodList.size(); i++) {
					foodItem = foodList.get(i);

					if (channel.indexOf(foodItem.channelName) != -1) {
						food = foodItem.displayName;
					}
				}

				if (food != null) {

					JSONObject json = new JSONObject(intent.getExtras()
							.getString(PARSE_DATA_KEY));

					if (json.has("college")) {
						college = json.getString("college");
						message = Utilities.getStringResourceByName(college);
					}

					// Calls a function to define the notification
					defineNotification(context, message);
				} else
					Log.i(LOG_TAG, "No matched subscribed food");

			} catch (Exception e) {
				Log.d(LOG_TAG, "JSONException: " + e.getMessage());
			}
		} else {
			Log.i(LOG_TAG, "No subsribed food");
		}
	}

	public static void defineNotification(Context context, String message) {
		// Show the notification
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification;
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context);
		builder.setContentText(message)
				.setContentTitle(context.getString(R.string.app_name))
				.setSmallIcon(R.drawable.ic_launcher).setWhen(when);

		// Build an intent to launch from notification
		Intent notificationIntent = new Intent(context, FoodListActivity.class);
		notificationIntent.putExtra("name", college);

		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
		// Finish building notification
		builder.setContentIntent(intent);
		notification = builder.build();

		notification.vibrate = new long[] { 500, 500 };
		notification.sound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		notification.flags = Notification.FLAG_AUTO_CANCEL
				| Notification.FLAG_SHOW_LIGHTS;

		notificationManager.notify(0, notification);
	}

}
