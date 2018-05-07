package com.bredeekmendes.popularmovies_stage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bredeekmendes.popularmovies_stage1.model.Movie;
import com.bredeekmendes.popularmovies_stage1.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    private Movie myDetailedMovie;
    private TextView mTitle;
    private TextView mSynopsis;
    private TextView mRelease;
    private TextView mRating;
    private ImageView mPoster;
    private String PARCELABLE_KEY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setTitle(R.string.detail_activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        PARCELABLE_KEY = this.getResources().getString(R.string.parcelable_key);

        Intent intent = getIntent();
        myDetailedMovie = intent.getParcelableExtra(PARCELABLE_KEY);

        mTitle = (TextView) findViewById(R.id.detail_title_tv);
        mSynopsis = (TextView) findViewById(R.id.detail_synopsis_tv);
        mRelease = (TextView) findViewById(R.id.detail_release_tv);
        mRating = (TextView) findViewById(R.id.detail_rating_tv);
        mPoster = (ImageView) findViewById(R.id.detail_poster_iv);

        String imageUrl = NetworkUtils.getImageUrl(myDetailedMovie.getImage());
        Picasso.with(this).load(imageUrl).into(mPoster);

        mTitle.setText(myDetailedMovie.getTitle());
        mSynopsis.setText(myDetailedMovie.getSynopsis());
        mRelease.setText(myDetailedMovie.getReleaseDate());
        mRating.setText(myDetailedMovie.getRating()+"/10");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
