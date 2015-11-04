package com.mydomain.popularmovies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mydomain.popularmovies.R;
import com.mydomain.popularmovies.config.Constants;
import com.mydomain.popularmovies.fragments.DetailsFragment;

public class DetailsActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle arguments = new Bundle();
        arguments.putParcelable(Constants.MOVIE_DETAILS, getIntent().getExtras().getParcelable(Constants.MOVIE_DETAILS));

        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(arguments);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_details, detailsFragment)
                    .commit();

        }
    }

}
