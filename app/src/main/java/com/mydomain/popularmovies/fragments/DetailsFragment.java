package com.mydomain.popularmovies.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mydomain.popularmovies.PopularMovies;
import com.mydomain.popularmovies.R;
import com.mydomain.popularmovies.adapters.ReviewsAdapter;
import com.mydomain.popularmovies.adapters.TrailersAdapter;
import com.mydomain.popularmovies.beans.Movies;
import com.mydomain.popularmovies.beans.Reviews;
import com.mydomain.popularmovies.beans.Trailers;
import com.mydomain.popularmovies.config.Constants;
import com.mydomain.popularmovies.customview.NestedListView;
import com.mydomain.popularmovies.data.MoviesColumns;
import com.mydomain.popularmovies.data.MoviesProvider;
import com.mydomain.popularmovies.utils.Utilities;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailsFragment extends Fragment {

    private static final String TAG = DetailsFragment.class.getSimpleName();
    private static final int TRAILER = 0;
    private static final int REVIEWS = 1;
    ArrayList<Trailers> mTrailersList = new ArrayList<Trailers>();
    ArrayList<Reviews> mReviewsList = new ArrayList<Reviews>();
    Movies movies;
    private boolean mIsFavourite = false;
    private DBTask dbtask;
    @Bind(R.id.backdrop)
    ImageView mBackdrop;
    @Bind(R.id.fab_favourite)
    FloatingActionButton mFabFavourite;
    @Bind(R.id.movies_release_details_view)
    TextView mReleaseDate;
    @Bind(R.id.movies_rating_details_view)
    TextView mRating;
    @Bind(R.id.movies_overview_details_view)
    TextView mOverview;
    @Bind(R.id.trailers_listview_content)
    NestedListView mTrailersListView;
    TrailersAdapter mTrailersAdapter;
    @Bind(R.id.reviews_listview_content)
    NestedListView mReviewsListView;
    ReviewsAdapter mReviewsAdapter;

    Snackbar mSnackbar;

    public DetailsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle arguments = getArguments();
        if (arguments != null) {
            movies = arguments.getParcelable(Constants.MOVIE_DETAILS);

            dbtask = new DBTask();
            dbtask.execute(movies.getMovieID());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);

        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        if (movies != null) {
            if (getActivity().findViewById(R.id.movies_detail_container) == null)
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            CollapsingToolbarLayout collapsingToolbar =
                    (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(movies.getOriginalTitle());

            Picasso.with(getActivity()).load(Constants.POSTER_BASE_URL + Constants.IMAGE_RESOLUTION + movies.getBackdropPath())
                        .placeholder(R.drawable.placeholder_backdrop)
                        .error(R.drawable.error_backdrop)
                        .into(mBackdrop);

            mReleaseDate.setText(movies.getReleaseDate());
            mRating.setText(Double.toString(movies.getVoteAverage()) + Constants.RATING);
            mOverview.setText(movies.getOverview());

            makeRequest(makeUrl(TRAILER, movies.getMovieID()), TRAILER);
            mTrailersAdapter = new TrailersAdapter(getActivity(), mTrailersList);
            mTrailersListView.setAdapter(mTrailersAdapter);
            mTrailersListView.setExpanded(true);
            makeRequest(makeUrl(REVIEWS, movies.getMovieID()), REVIEWS);
            mReviewsAdapter = new ReviewsAdapter(getActivity(), mReviewsList);
            mReviewsListView.setAdapter(mReviewsAdapter);
            mReviewsListView.setExpanded(true);

            mTrailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Trailers trailers = mTrailersList.get(position);
                    if (trailers.getUrl() != null && !trailers.getUrl().isEmpty()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailers.getUrl()));
                        if (intent.resolveActivity(getActivity().getPackageManager()) != null)
                            startActivity(intent);
                    }

                }
            });


        }

        return view;
    }

    private String makeUrl(int type, String movieId) {
        String url = "";
        if (type == TRAILER) {
            url = Constants.MOVIE_BASE_URL + movieId + Constants.MOVIE_TRAILER_URL;
        } else if (type == REVIEWS) {
            url = Constants.MOVIE_BASE_URL + movieId + Constants.MOVIE_REVIEWS_URL;
        }
        return url.trim();
    }

    private Snackbar snackBar(View view, String snackbarData) {
        mSnackbar = Snackbar.make(view, snackbarData, Snackbar.LENGTH_LONG);
        return mSnackbar;
    }

    private Cursor checkFavourite(String movieID) {
        Cursor c = null;
        if (movieID != null && !movieID.isEmpty()) {
            String[] projection = {MoviesColumns.MOVIE_ID};
            String selectionClause = MoviesColumns.MOVIE_ID + "=?";
            String[] selectionArgs = {movieID};
            String sortOrder = "_id ASC";
            c = getActivity().getContentResolver().query(MoviesProvider.Movies.CONTENT_URI, projection,
                    selectionClause, selectionArgs, sortOrder);
        }
        return c;
    }


    @OnClick(R.id.fab_favourite)
    public void onClickFabFavourite(View view) {
        if (mIsFavourite) {
            int rows = deleteMovies();
            if (rows > 0) {
                snackBar(view, getResources().getString(R.string.snackbar_remove_favourites));
                if (mSnackbar != null && mSnackbar.isShown()) {
                    mSnackbar.dismiss();
                }
                mSnackbar.show();
                mFabFavourite.setImageDrawable(getResources().getDrawable(R.drawable.button_fav_star_off));
                mIsFavourite = false;
            }
        } else {
            insertMovies();
            snackBar(view, getResources().getString(R.string.snackbar_add_favourites));
            if (mSnackbar != null && mSnackbar.isShown()) {
                mSnackbar.dismiss();
            }
            mSnackbar.show();
            mFabFavourite.setImageDrawable(getResources().getDrawable(R.drawable.button_fav_star_on));
            mIsFavourite = true;
        }
    }

    private int deleteMovies() {
        String selectionClause = MoviesColumns.MOVIE_ID + "=?";
        String[] selectionArgs = {movies.getMovieID()};
        int rows = getActivity().getContentResolver().delete(MoviesProvider.Movies.CONTENT_URI,
                selectionClause, selectionArgs);
        return rows;
    }

    private void insertMovies() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesColumns.ORIGINAL_TITLE, movies.getOriginalTitle());
        contentValues.put(MoviesColumns.OVERVIEW, movies.getOverview());
        contentValues.put(MoviesColumns.RELEASE_DATE, movies.getReleaseDate());
        contentValues.put(MoviesColumns.POSTER_PATH, movies.getPosterPath());
        contentValues.put(MoviesColumns.VOTE_AVERAGE, movies.getVoteAverage());
        contentValues.put(MoviesColumns.BACKDROP_PATH, movies.getBackdropPath());
        contentValues.put(MoviesColumns.MOVIE_ID, movies.getMovieID());

        getActivity().getContentResolver().insert(MoviesProvider.Movies.CONTENT_URI, contentValues);

    }

    private void makeRequest(String url, final int type) {
        if (Utilities.isNetworkAvailable(getActivity())) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new String(),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response != null) {
                                    JSONArray resultsObject = response.getJSONArray(Constants.RESULTS);
                                    if (type == TRAILER) {
                                        mTrailersList.clear();
                                        for (int i = 0; i < resultsObject.length(); i++) {
                                            Trailers trailers = new Trailers();
                                            JSONObject jsonResult = (JSONObject) resultsObject.get(i);
                                            trailers.setName(jsonResult.getString(Constants.NAME));
                                            String url = Constants.YOUTUBE_BASE_URL + jsonResult.getString(Constants.KEY);
                                            trailers.setUrl(url);
                                            mTrailersList.add(trailers);
                                        }
                                        if (mTrailersList != null && mTrailersList.size() < 0) {
                                            Trailers trailers = new Trailers();
                                            trailers.setName(getResources().getString(R.string.error_no_trailers_available));
                                            mTrailersList.add(trailers);
                                        }
                                        mTrailersAdapter.notifyDataSetChanged();
                                    } else if (type == REVIEWS) {
                                        mReviewsList.clear();
                                        for (int i = 0; i < resultsObject.length(); i++) {
                                            Reviews reviews = new Reviews();
                                            JSONObject jsonResult = (JSONObject) resultsObject.get(i);
                                            reviews.setAuthor(jsonResult.getString(Constants.AUTHOR));
                                            reviews.setContent(jsonResult.getString(Constants.CONTENT));
                                            mReviewsList.add(reviews);
                                        }
                                        if (mReviewsList != null && mReviewsList.size() < 0) {
                                            Reviews reviews = new Reviews();
                                            reviews.setAuthor(getResources().getString(R.string.error_no_reviews_available));
                                            mReviewsList.add(reviews);
                                        }
                                        mReviewsAdapter.notifyDataSetChanged();
                                    }
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
        }
    }

    private class DBTask extends AsyncTask<String, Void, Cursor> {
        @Override
        protected Cursor doInBackground(String... string) {
            return checkFavourite(string[0]);
        }

        @Override
        protected void onPostExecute(Cursor c) {
            if (c != null && c.getCount() > 0) {
                mIsFavourite = true;
                mFabFavourite.setImageDrawable(getResources().getDrawable(R.drawable.button_fav_star_on));
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dbtask.cancel(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.fragment_details_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Trailers trailers = null;
        int id = item.getItemId();
        if (id == R.id.action_share_trailer) {
            if (mTrailersList != null && mTrailersList.size() > 0) {
                trailers = mTrailersList.get(0);
                if (trailers != null && !TextUtils.isEmpty(trailers.getUrl())) {
                    String sendData = getResources().getString(R.string.text_movie) + Constants.SEPERATOR + movies.getOriginalTitle()
                            + getResources().getString(R.string.text_trailer) + Constants.SEPERATOR + trailers.getUrl();
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, sendData);
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
