package com.example.moviefinder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MovieInfo extends Activity{

	private ArrayAdapter<String> adapter;
	private Pref pref = new Pref();
	private int index=0;
	private List<String> movieIDArray = new ArrayList<String>();
	private List<String> movieTitleArray = new ArrayList<String>();
	private List<String> movieImgArray = new ArrayList<String>();
	private List<String> newMovieIDArray = new ArrayList<String>();
	private List<String> newMovieTitleArray = new ArrayList<String>();
	private List<String> newMovieImgArray = new ArrayList<String>();


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_info);

		final Bundle extras = getIntent().getExtras();

		ListView listV = (ListView) findViewById(R.id.actors);
		TextView title = (TextView) findViewById(R.id.title);
		ImageView iv = (ImageView) findViewById(R.id.movieImage);

		title.setText(extras.getString("title"));
		iv.setImageBitmap(getImageBitmap(extras.getString("picture")));
		adapter = new ArrayAdapter<String>(MovieInfo.this, android.R.layout.simple_list_item_1,
				extras.getStringArrayList("cast"));
		listV.setAdapter(adapter);
		listV.setDividerHeight(1);
		listV.setClickable(false);


		Button add = (Button) findViewById(R.id.addToWatchlist);
		if(checkOnWatchlist(extras.getString("id")))
			add.setText("Remove");


		add.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Button add = (Button) findViewById(R.id.addToWatchlist);
				if(checkOnWatchlist(extras.getString("id"))){
					for(int i=0;i<movieIDArray.size();i++){
						if(i==index){
						}else{
							newMovieIDArray.add(movieIDArray.get(i));
							newMovieTitleArray.add(movieTitleArray.get(i));
							newMovieImgArray.add(movieImgArray.get(i));
						}
					}

					String ids ="";
					String titles = "";
					String img = "";
					int i =0;
					while (i < newMovieIDArray.size()){
						if(i!=0){
							ids+=",,,"+newMovieIDArray.get(i);
							titles+=",,,"+newMovieTitleArray.get(i);
							img+=",,,"+newMovieImgArray.get(i);
						}else{
							ids+=newMovieIDArray.get(i);
							titles+=newMovieTitleArray.get(i);
							img+=newMovieImgArray.get(i);
						}
						i++;
					}
					pref.removePref(MovieInfo.this, ids, titles, img);
					add.setText("Add");

				}else{
					pref.setPref(MovieInfo.this, extras.getString("id"),extras.getString("title"), extras.getString("picture"));
					add.setText("Remove");
				}
			}
		});

	}

	public boolean checkOnWatchlist(String id){

		String movieID = pref.getIDPref(MovieInfo.this);
		String movieTitles = pref.getTitlePref(MovieInfo.this);
		String movieImg = pref.getImgPref(MovieInfo.this);
		movieIDArray=Arrays.asList(movieID.split(",,,"));
		movieTitleArray=Arrays.asList(movieTitles.split(",,,"));
		movieImgArray=Arrays.asList(movieImg.split(",,,"));		
		int i = 0;

		while (i < movieIDArray.size()){
			if(movieIDArray.get(i).equals(id)){
				index=i;
				return true;
			}
			i++;
		}

		return false;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	private Bitmap getImageBitmap(String url) {
		Bitmap bm = null;
		try {
			URL aURL = new URL(url);
			URLConnection conn = aURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
		} catch (IOException e) {
			//   Log.e(TAG, "Error getting bitmap", e);
		}
		return bm;
	} 
}