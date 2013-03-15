package com.example.moviefinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MovieFileFetcher {

	private final IBinder mBinder = new LocalBinder();

	public String onCreate(String keyWord){

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(keyWord);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Log.e("MovieFinder", "Failed to download file");
			}
		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return builder.toString();
	}

	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}


	public class LocalBinder extends Binder{
		MovieFileFetcher getService() {
			// Return this instance of MyService so clients can call public
			// methods
			return MovieFileFetcher.this;
		}
	}
}
