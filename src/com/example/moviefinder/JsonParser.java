package com.example.moviefinder;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class JsonParser extends Service{


	private final IBinder jBinder = new LocalBinder();

	public ArrayList<String> jsonparser(String jsonDoc,String groupName, String keyWord){

		ArrayList<String> data;

		/** This happens on a click **/
		data = new ArrayList<String>();
		String text;
		text = jsonDoc;
		try {
			JSONObject res = new JSONObject(text);
			JSONArray jsonArray = res.getJSONArray(groupName);

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String item;
				item = jsonObject.getString(keyWord);
				data.add(item);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}	

		return data;

	}
	public ArrayList<String> jsonparser(String jsonDoc,String groupName, String subGroupName, String keyWord){

		ArrayList<String> data;

		/** This happens on a click **/
		data = new ArrayList<String>();
		String text;
		text = jsonDoc;
		try {

			JSONObject res = new JSONObject(text);
			JSONArray jsonArray = res.getJSONArray(groupName);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				JSONObject subJsonObject = jsonObject.getJSONObject(subGroupName);
				String item;
				item = subJsonObject.getString(keyWord);
				data.add(item);


			}
		} catch (Exception e) {
			e.printStackTrace();
		}	

		return data;

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return jBinder;
	}

	public class LocalBinder extends Binder{
		JsonParser getService() {

			return JsonParser.this;
		}
	}
}
