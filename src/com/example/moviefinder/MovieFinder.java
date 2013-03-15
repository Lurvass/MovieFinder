package com.example.moviefinder;
import java.util.ArrayList;
import com.example.moviefinder.JsonParser.LocalBinder;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MovieFinder extends Activity {

	private String movieResults;
	private ArrayList<String> movieTitles= new ArrayList<String>();
	private ArrayList<String> movieIds= new ArrayList<String>();
	private ArrayList<String> moviePictures = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	MovieFileFetcher mService = new MovieFileFetcher();
	JsonParser jService = new JsonParser();

	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder service) {
			LocalBinder binder = (LocalBinder) service;
			jService = binder.getService();
		}
		public void onServiceDisconnected(ComponentName arg0) {

		}
	};


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final EditText searchField = (EditText) findViewById(R.id.searchField);
		Button searchButton = (Button) findViewById(R.id.searchButton);
		Intent intent = new Intent(MovieFinder.this, JsonParser.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);


		searchButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String search = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?" +
						"apikey=b7vymuknrx5ngtjej5apdner&page_limit=10&q="+searchField.getText().toString().replace(" ", "+");
				clearList((ListView) findViewById(R.id.similarMovieList));
				ListView lw = (ListView) findViewById(R.id.movieList);


				getJsonFile(search);

				printThis(lw);
				addListener(lw);

			}
		});
	}

	public void addtabListener(ListView lw){
		lw.setOnItemClickListener(new OnItemClickListener() { 
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				try{
					Intent intent = new Intent(MovieFinder.this, MovieInfo.class);
					startActivity(intent);
				}catch(Exception e){
					e.printStackTrace();
				}
			} 
		}); 
	}

	public void addListener(ListView lw){
		lw.setOnItemClickListener(new OnItemClickListener() { 
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				ListView lw = (ListView) findViewById(R.id.similarMovieList);
				lw.setOnItemClickListener(new OnItemClickListener() { 
					public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
						String search = "http://api.rottentomatoes.com/api/public/v1.0/movies/" +
								movieIds.get(position)+"/cast.json?apikey=b7vymuknrx5ngtjej5apdner";
						Intent intent = new Intent(MovieFinder.this, MovieInfo.class);
						intent.putExtra("cast", getCast(search));
						intent.putExtra("title", movieTitles.get(position));
						intent.putExtra("id", movieIds.get(position));
						intent.putExtra("picture", moviePictures.get(position));
						startActivity(intent);
					}
				});
				String selectedTitle=movieTitles.get(position);
				String selectedID= movieIds.get(position);
				String selectedImg=moviePictures.get(position);
				String search="http://api.rottentomatoes.com/api/public/v1.0/movies/" +
						selectedID +"/similar.json?apikey=b7vymuknrx5ngtjej5apdner&limit=5";
				clearList((ListView) findViewById(R.id.movieList));
				getJsonFile(search);
				addSelected(selectedTitle,selectedID, selectedImg);

				printThis(lw);
			} 
		}); 
	}

	public ArrayList<String> getCast(String search){
		try{
			movieResults = mService.onCreate(search);
			return jService.jsonparser(movieResults,"cast", "name");
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public void clearList(ListView listV){
		movieTitles.clear();
		movieIds.clear();
		moviePictures.clear();
		adapter = new ArrayAdapter<String>(MovieFinder.this, android.R.layout.simple_list_item_1, movieTitles);
		listV.setAdapter(adapter);
		listV.setDividerHeight(1);
	}

	public void getJsonFile(String search){
		try{
			movieResults = mService.onCreate(search);
			System.out.println(jService.jsonparser(movieResults,"movies", "title")+" denna Šr tom?");
			movieTitles=jService.jsonparser(movieResults,"movies", "title");
			movieIds=jService.jsonparser(movieResults,"movies", "id");
			moviePictures=jService.jsonparser(movieResults,"movies" ,"posters", "profile");
			System.out.println("moviePictures test: " + moviePictures);


		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void printThis(ListView listV){
		adapter = new ArrayAdapter<String>(MovieFinder.this, android.R.layout.simple_list_item_1, movieTitles);
		listV.setAdapter(adapter);
		listV.setDividerHeight(5);
		if(movieTitles.isEmpty()){
			AlertDialog alertDialog = new AlertDialog.Builder(MovieFinder.this).create();
			alertDialog.setTitle("Could not find movie.");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			alertDialog.show();
		} 
	}

	public void addSelected(String selectedTitle, String selectedID, String selectedImg){
		movieTitles.add(0, selectedTitle);
		movieIds.add(0, selectedID);
		moviePictures.add(0, selectedImg);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
