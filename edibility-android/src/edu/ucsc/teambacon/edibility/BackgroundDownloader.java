package edu.ucsc.teambacon.edibility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

//This class downloads from the net the camera setup instructions.
public class BackgroundDownloader extends AsyncTask<String, String, String> {
	private static String LOG_TAG = "Background Downloader";
	private static final int MAX_SETUP_DOWNLOAD_TRIES = 3;
	
	//Instance variables
	private BDCompletionTask onComplete;
	
	public BackgroundDownloader(BDCompletionTask task) {
		// Create a new BackgroundDownloader and start downloading
		this.onComplete = task;
	}
	
	@Override
	protected String doInBackground(String... urls) {
		Log.d(LOG_TAG, "Starting the download.");
		String downloadedString = null;
		String urlString = urls[0];
		URI url = URI.create(urlString);
		int numTries = 0;
		while (downloadedString == null && numTries < MAX_SETUP_DOWNLOAD_TRIES && !isCancelled()) {
			numTries++;
			HttpGet request = new HttpGet(url);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = null;
			try {
				response = httpClient.execute(request);
			} catch (ClientProtocolException ex) {
				Log.e(LOG_TAG, ex.toString());
			} catch (IOException ex) {
				Log.e(LOG_TAG, ex.toString());
			}
			if (response != null) {
				// Checks the status code.
				int statusCode = response.getStatusLine().getStatusCode();
				Log.d(LOG_TAG, "Status code: " + statusCode);

				if (statusCode == HttpURLConnection.HTTP_OK) {
					// Correct response. Reads the real result.
					// Extracts the string content of the response.
					HttpEntity entity = response.getEntity();
					InputStream iStream = null;
					try {
						iStream = entity.getContent();
					} catch (IOException ex) {
						Log.e(LOG_TAG, ex.toString());
					}
					if (iStream != null) {
						downloadedString = ConvertStreamToString(iStream);
						Log.d(LOG_TAG, "Received string: " + downloadedString);
						return downloadedString;
					}
				}
			}
		}
		// Returns the instructions, if any.
		return downloadedString;
	}
	
	@Override
	protected void onPostExecute(String s) {
		// Pass downloaded string to post execute function
		this.onComplete.execute(s);
	}
	
	// Convert the stream to a string
	public static String ConvertStreamToString(InputStream is) {
		
		if (is == null) {
			return null;
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append((line + "\n"));
			}
		} catch (IOException e) {
			Log.d(LOG_TAG, e.toString());
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Log.d(LOG_TAG, e.toString());
			}
		}
		return sb.toString();
	}
}