package com.mydomain.popularmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mydomain.popularmovies.R;
import com.mydomain.popularmovies.beans.Movies;
import com.mydomain.popularmovies.config.Constants;
import com.mydomain.popularmovies.fragments.DetailsFragment;
import com.mydomain.popularmovies.fragments.MainFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String MOVIES_FRAGMENT = "MOVIES_FRAGMENT";
    private static final String DETAILS_FRAGMENT = "DETAILS_FRAGMENT";

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set main layout
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.movies_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movies_container, new MainFragment(), MOVIES_FRAGMENT)
                        .commit();
            }
        } else {
            mTwoPane = false;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_main, new MainFragment(), MOVIES_FRAGMENT)
                        .commit();
            }
        }


    }

    public void onFragmentInteraction(Movies movies) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(Constants.MOVIE_DETAILS, movies);
        if (mTwoPane) {
            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movies_detail_container, detailsFragment, DETAILS_FRAGMENT).commit();

        } else {
            Intent i = new Intent(this, DetailsActivity.class);
            i.putExtras(arguments);
            startActivity(i);
        }
    }


}
