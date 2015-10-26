package com.mydomain.popularmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mydomain.popularmovies.PopularMovies;
import com.mydomain.popularmovies.R;
import com.mydomain.popularmovies.adapters.GridAdapter;
import com.mydomain.popularmovies.beans.Movies;
import com.mydomain.popularmovies.config.Constants;
import com.mydomain.popularmovies.utils.ScrollListener;
import com.mydomain.popularmovies.utils.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Context context;
    ArrayList<Movies> moviesList = new ArrayList<Movies>();
    private int page = 1;
    GridAdapter gridAdapter ;
    LinearLayout progress;
    Parcelable parcelableGridState;
    int listPosition = -1;
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        //Set main layout
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progress = (LinearLayout)findViewById(R.id.linlaHeaderProgress);
        progress.setVisibility(View.VISIBLE);
        gridAdapter = new GridAdapter(this, moviesList);
        gridView = (GridView)findViewById(R.id.gridview);
        gridView.setAdapter(gridAdapter);
        //Restore cached Movies details
        if(savedInstanceState != null && savedInstanceState.containsKey(Constants.KEY_PARCELABLE_RESULTS_LIST)){
            moviesList.clear();
            ArrayList<Movies> cachedMoviesList = savedInstanceState.getParcelableArrayList(Constants.KEY_PARCELABLE_RESULTS_LIST);
            moviesList.addAll(cachedMoviesList);
            listPosition = savedInstanceState.getInt(Constants.KEY_LIST_POSITION);
            gridAdapter.notifyDataSetChanged();
            gridView.smoothScrollToPosition(listPosition);
            progress.setVisibility(View.GONE);
           }else{
            moviesList.clear();
            makeRequest(Constants.POPULAR_MOVIES_URL + page);
        }
        //Listener to listen the grid item click events
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Movies movies = moviesList.get(position);
                //Results results1 = new Results(results.getOriginalTitle(), results.getOverview(), results.getReleaseDate(), results.getPosterPath(), results.getVoteAverage());
                Bundle b = new Bundle();
                b.putParcelable(Constants.MOVIE_DETAILS, movies);
                Intent i = new Intent(getApplicationContext(), DetailsActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });
        //Listener to listen the scroll event
        gridView.setOnScrollListener(new ScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                if (page <= Constants.PAGES_THRESHOLD) {
                    makeRequest(Constants.POPULAR_MOVIES_URL + page);
                    return true;
                }
                return false;
            }
        });

    }

    //Method to send request to theMovieDB API
    public void makeRequest(String url){
        if(Utilities.isNetworkAvailable(context)) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new String(), new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray resultsObject = response.getJSONArray(Constants.RESULTS);
                        for (int i = 0; i < resultsObject.length(); i++) {
                            Movies results = new Movies();
                            JSONObject jsonResult = (JSONObject) resultsObject.get(i);
                            results.setOriginalTitle(jsonResult.getString(Constants.ORIGINAL_TITLE));
                            results.setOverview(jsonResult.getString(Constants.OVERVIEW));
                            results.setReleaseDate(jsonResult.getString(Constants.RELEASE_DATE));
                            results.setPosterPath(jsonResult.getString(Constants.POSTER_PATH));
                            results.setVoteAverage(Double.parseDouble(jsonResult.getString(Constants.VOTE_AVERAGE)));

                            moviesList.add(results);

                        }
                        gridAdapter.notifyDataSetChanged();
                        progress.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        Log.i(TAG, e.getMessage());
                    }

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG, error.getMessage());
                }
            });

            PopularMovies.getInstance().addToRequestQueue(jsonObjectRequest);
        }else{
            Toast.makeText(MainActivity.this, getResources().getString(R.string.text_network_status), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.most_popular) {
            moviesList.clear();
            progress.setVisibility(View.VISIBLE);
            makeRequest(Constants.POPULAR_MOVIES_URL + page);
            return true;
        }else if(id == R.id.high_rating){
            moviesList.clear();
            progress.setVisibility(View.VISIBLE);
            makeRequest(Constants.HIGHEST_RATED_MOVIES_URL + page);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //Save the state in order to restore
    protected void onSaveInstanceState(Bundle state){
        state.putParcelableArrayList(Constants.KEY_PARCELABLE_RESULTS_LIST, moviesList);
        listPosition = gridView.getFirstVisiblePosition();
        state.putInt(Constants.KEY_LIST_POSITION, listPosition);
        super.onSaveInstanceState(state);
  }

}
