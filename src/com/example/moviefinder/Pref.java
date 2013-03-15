package com.example.moviefinder;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;


public class Pref extends Activity {

	public static final String MYPREFS = "mySharedPreferences";
	public static final String KEY = "prefKey";
	public static final String titleKEY = "titleKey";
	public static final String IMGKEY = "imgKey";
	int mode = Activity.MODE_PRIVATE;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_info);
	}
	public void setPref(Context ctx, String movieId, String movieTitle, String movieImg){
		SharedPreferences mySharedPreferences = ctx.getSharedPreferences(MYPREFS, mode);
		String tmpID = mySharedPreferences.getString(KEY, "");
		String tmpTitle = mySharedPreferences.getString(titleKEY, "");
		String tmpImg = mySharedPreferences.getString(IMGKEY, "");
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		if(getIDPref(ctx) == null || getIDPref(ctx) == ""){
			editor.putString(KEY, movieId);
			editor.putString(titleKEY, movieTitle);
			editor.putString(IMGKEY, movieImg);

		}else{
			editor.putString(KEY, tmpID+",,,"+movieId);
			editor.putString(titleKEY, tmpTitle+",,,"+movieTitle);
			editor.putString(IMGKEY, tmpImg+",,,"+movieImg);
		}
		// add these lines to empty the preferences
/*
		editor.putString(KEY, "");
		editor.putString(titleKEY, "");
		editor.putString(IMGKEY, "");
*/

		editor.commit();
	}

	public String getIDPref(Context ctx){
		SharedPreferences mySharedPreferences = ctx.getSharedPreferences(MYPREFS, mode);
		String tmp = mySharedPreferences.getString(KEY, "");
		return tmp;
	} 

	public String getTitlePref(Context ctx){
		SharedPreferences mySharedPreferences = ctx.getSharedPreferences(MYPREFS, mode);
		String tmp = mySharedPreferences.getString(titleKEY, "");
		return tmp;
	} 
	public String getImgPref(Context ctx){
		SharedPreferences mySharedPreferences = ctx.getSharedPreferences(MYPREFS, mode);
		String tmp = mySharedPreferences.getString(IMGKEY, "");
		return tmp;
	} 

	public void removePref(Context ctx, String ids, String titles, String img){
		SharedPreferences mySharedPreferences = ctx.getSharedPreferences(MYPREFS, mode);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putString(KEY, ids);
		editor.putString(titleKEY, titles);
		editor.putString(IMGKEY, img);
		editor.commit();
	}


}
