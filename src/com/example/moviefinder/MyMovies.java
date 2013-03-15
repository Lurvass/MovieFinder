package com.example.moviefinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MyMovies extends Activity {
	private ArrayAdapter<String> adapter;
	private List<String> movieTitleArray = new ArrayList<String>();
	private List<String> movieIDArray = new ArrayList<String>();
	private List<String> moviePictures = new ArrayList<String>();
	private Pref pref = new Pref();
	private MovieFinder mf = new MovieFinder();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stored_movies);
		onResume();


	}
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		// Update your UI here.

		ListView listV = (ListView) findViewById(R.id.listStoredMovies);
		listV.setOnItemClickListener(new OnItemClickListener() { 
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {


				String movieID = pref.getIDPref(MyMovies.this);
				String movieTitle = pref.getTitlePref(MyMovies.this);
				String movieImg = pref.getImgPref(MyMovies.this);
				movieIDArray=Arrays.asList(movieID.split(",,,"));
				movieTitleArray=Arrays.asList(movieTitle.split(",,,"));
				moviePictures=Arrays.asList(movieImg.split(",,,"));
				String search = "http://api.rottentomatoes.com/api/public/v1.0/movies/" +
						movieIDArray.get(position)+"/cast.json?apikey=b7vymuknrx5ngtjej5apdner";

				Intent intent = new Intent(MyMovies.this, MovieInfo.class);
				intent.putExtra("cast", mf.getCast(search));
				intent.putExtra("title", movieTitleArray.get(position));
				intent.putExtra("id", movieIDArray.get(position));
				intent.putExtra("picture", moviePictures.get(position));
				startActivity(intent);

			}
		});
		String movieTitle = pref.getTitlePref(MyMovies.this);

		if(movieTitle.split(",,,") != null) {
			movieTitleArray=Arrays.asList(movieTitle.split(",,,"));
		}else{
			movieTitleArray.add(movieTitle);
		}

		adapter = new ArrayAdapter<String>(MyMovies.this, android.R.layout.simple_list_item_1,
				movieTitleArray);
		listV.setAdapter(adapter);



	}
}
