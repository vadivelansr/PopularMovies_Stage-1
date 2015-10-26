package com.mydomain.popularmovies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.mydomain.popularmovies.R;
import com.mydomain.popularmovies.beans.Movies;
import com.mydomain.popularmovies.config.Constants;
import com.squareup.picasso.Picasso;

/**
 * Created by techkt.com on 10/9/2015.
 */
public class DetailsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);
        // get intent data
        Movies movies = getIntent().getExtras().getParcelable(Constants.MOVIE_DETAILS);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(movies.getOriginalTitle());

        ImageView imageView = (ImageView)findViewById(R.id.movies_poster_details_view);
        Picasso.with(this).load(Constants.POSTER_BASE_URL + Constants.IMAGE_RESOLUTION + movies.getPosterPath()).into(imageView);
        TextView releaseDate = (TextView)findViewById(R.id.movies_release_details_view);
        releaseDate.setText(movies.getReleaseDate());
        TextView rating = (TextView)findViewById(R.id.movies_rating_details_view);
        rating.setText(Double.toString(movies.getVoteAverage()) + Constants.RATING);
        TextView overview = (TextView)findViewById(R.id.movies_overview_details_view);
        overview.setText(movies.getOverview());



    }
}
