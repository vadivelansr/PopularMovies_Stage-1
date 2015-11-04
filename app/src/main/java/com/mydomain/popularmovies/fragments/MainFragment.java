package com.mydomain.popularmovies.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mydomain.popularmovies.PopularMovies;
import com.mydomain.popularmovies.R;
import com.mydomain.popularmovies.adapters.GridAdapter;
import com.mydomain.popularmovies.beans.Movies;
import com.mydomain.popularmovies.config.Constants;
import com.mydomain.popularmovies.data.MoviesColumns;
import com.mydomain.popularmovies.data.MoviesProvider;
import com.mydomain.popularmovies.utils.ScrollListener;
import com.mydomain.popularmovies.utils.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = MainFragment.class.getSimpleName();
    private static final int DB_LOADER = 0;

    private Context context;
    ArrayList<Movies> mMoviesList = new ArrayList<Movies>();
    ArrayList<Movies> mFavouritesList = new ArrayList<Movies>();
    private int mPage = 1;
    GridAdapter mGridAdapter;
    LinearLayout mProgress;
    private int mPosition = ListView.INVALID_POSITION;
    @Bind(R.id.gridview)
    GridView mGridView;
    private boolean mOnFavourites = false;
    Snackbar mSnackbar;
    CoordinatorLayout mCoordinatorLayout;

    public MainFragment() {
        }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        context = getActivity();

        mProgress = (LinearLayout) rootView.findViewById(R.id.linlaHeaderProgress);
        mProgress.setVisibility(View.VISIBLE);
        mCoordinatorLayout = (CoordinatorLayout)getActivity().findViewById(R.id.coordinator_layout);

        mGridAdapter = new GridAdapter(getActivity(), mMoviesList);
        mGridView.setAdapter(mGridAdapter);
        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.KEY_PARCELABLE_RESULTS_LIST)) {
            mMoviesList.clear();
            ArrayList<Movies> cachedMoviesList = savedInstanceState.getParcelableArrayList(Constants.KEY_PARCELABLE_RESULTS_LIST);
            mMoviesList.addAll(cachedMoviesList);
            mPosition = savedInstanceState.getInt(Constants.KEY_LIST_POSITION);
            mGridAdapter.notifyDataSetChanged();
            mGridView.smoothScrollToPosition(mPosition);
            mProgress.setVisibility(View.GONE);
        } else {
            mMoviesList.clear();
            makeRequest(Constants.POPULAR_MOVIES_URL + mPage);
        }
        //Listener to listen the grid item click events
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Movies movies = mMoviesList.get(position);
                ((OnFragmentInteractionListener) getActivity()).onFragmentInteraction(movies);
                mPosition = position;
            }
        });
        //Listener to listen the scroll event
        mGridView.setOnScrollListener(new ScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {

                if (!mOnFavourites && page <= Constants.PAGES_THRESHOLD) {
                    makeRequest(Constants.POPULAR_MOVIES_URL + page);
                    return true;
                }
                return false;
            }
        });

        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DB_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    //Method to send request to theMovieDB API
    public void makeRequest(String url) {
        if (Utilities.checkNetworkState(context)) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new String(),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response != null) {
                                    JSONArray resultsObject = response.getJSONArray(Constants.RESULTS);
                                    for (int i = 0; i < resultsObject.length(); i++) {
                                        Movies movies = new Movies();
                                        JSONObject jsonResult = (JSONObject) resultsObject.get(i);
                                        movies.setOriginalTitle(jsonResult.getString(Constants.ORIGINAL_TITLE));
                                        movies.setOverview(jsonResult.getString(Constants.OVERVIEW));
                                        movies.setReleaseDate(jsonResult.getString(Constants.RELEASE_DATE));
                                        movies.setPosterPath(jsonResult.getString(Constants.POSTER_PATH));
                                        movies.setVoteAverage(Double.parseDouble(jsonResult.getString(Constants.VOTE_AVERAGE)));
                                        movies.setBackdropPath(jsonResult.getString(Constants.BACKDROP_PATH));
                                        movies.setMovieID(jsonResult.getString(Constants.MOVIE_ID));

                                        mMoviesList.add(movies);

                                    }
                                    mGridAdapter.notifyDataSetChanged();
                                    mProgress.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                Log.i(TAG, e.getMessage());
                            }

                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            PopularMovies.getInstance().addToRequestQueue(jsonObjectRequest);
        } else {
            displayNetworkStatusSnack();
        }
    }
    private void displayNetworkStatusSnack(){
        snackBar(getResources().getString(R.string.text_network_status));
        if (mSnackbar != null && mSnackbar.isShown()) {
            mSnackbar.dismiss();
        }
        mSnackbar.show();
    }
    private Snackbar snackBar(String snackbarData) {
        mSnackbar = Snackbar.make(mCoordinatorLayout, snackbarData, Snackbar.LENGTH_LONG);
        return mSnackbar;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.frgment_menu_main, menu);

    }

    private ArrayList<Movies> loadFavourites(Cursor c) {
        ArrayList<Movies> favouriteMovies = new ArrayList<Movies>();
        if (c != null) {
            while (c.moveToNext()) {
                Movies movies = new Movies();
                movies.setOriginalTitle(c.getString(c.getColumnIndex(MoviesColumns.ORIGINAL_TITLE)));
                movies.setOverview(c.getString(c.getColumnIndex(MoviesColumns.OVERVIEW)));
                movies.setPosterPath(c.getString(c.getColumnIndex(MoviesColumns.POSTER_PATH)));
                movies.setBackdropPath(c.getString(c.getColumnIndex(MoviesColumns.BACKDROP_PATH)));
                movies.setMovieID(c.getString(c.getColumnIndex(MoviesColumns.MOVIE_ID)));
                movies.setReleaseDate(c.getString(c.getColumnIndex(MoviesColumns.RELEASE_DATE)));
                movies.setVoteAverage(Double.parseDouble(c.getString(c.getColumnIndex(MoviesColumns.VOTE_AVERAGE))));
                favouriteMovies.add(movies);
            }
        }
        return favouriteMovies;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.most_popular) {
            mMoviesList.clear();
            mProgress.setVisibility(View.VISIBLE);
            mOnFavourites = false;
            makeRequest(Constants.POPULAR_MOVIES_URL + mPage);
            mProgress.setVisibility(View.GONE);
            return true;
        } else if (id == R.id.high_rating) {
            mMoviesList.clear();
            mProgress.setVisibility(View.VISIBLE);
            mOnFavourites = false;
            makeRequest(Constants.HIGHEST_RATED_MOVIES_URL + mPage);
            mProgress.setVisibility(View.GONE);
            return true;
        } else if (id == R.id.favourites) {
            mOnFavourites = true;
            mMoviesList.clear();
            mProgress.setVisibility(View.VISIBLE);
            mMoviesList.addAll(mFavouritesList);
            mGridAdapter.notifyDataSetChanged();
            mProgress.setVisibility(View.GONE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Save the state in order to restore
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putParcelableArrayList(Constants.KEY_PARCELABLE_RESULTS_LIST, mMoviesList);
        state.putInt(Constants.KEY_LIST_POSITION, mPosition);

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Movies movies);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Utilities.isNetworkAvailable(getActivity()))
            mGridAdapter.notifyDataSetChanged();
        else
                displayNetworkStatusSnack();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String sortOrder = MoviesColumns._ID + " ASC";
        switch (i) {
            case DB_LOADER:
                return new CursorLoader(getActivity(), MoviesProvider.Movies.CONTENT_URI, null, null, null, sortOrder);
            default:
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mFavouritesList.clear();
        mFavouritesList.addAll(loadFavourites(cursor));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavouritesList.clear();
    }

}
