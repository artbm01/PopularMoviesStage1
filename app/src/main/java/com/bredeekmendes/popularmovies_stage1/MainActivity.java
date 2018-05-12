package com.bredeekmendes.popularmovies_stage1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bredeekmendes.popularmovies_stage1.model.Movie;
import com.bredeekmendes.popularmovies_stage1.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.bredeekmendes.popularmovies_stage1.utils.JsonUtils.parseMovieJson;
import static com.bredeekmendes.popularmovies_stage1.utils.NetworkUtils.getJsonResponseFromUrlRequest;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Movie>>, MovieAdapter.OnClickedListener, SharedPreferences.OnSharedPreferenceChangeListener{

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private static final int MOVIE_LOADER_ID = 0;
    private SharedPreferences sp;
    private String PARCELABLE_KEY;
    private ProgressBar mProgressBar;
    private NetworkReceiver receiver = new NetworkReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies_main);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        setActivityCustomTitle();
        PARCELABLE_KEY = this.getResources().getString(R.string.parcelable_key);
        if (receiver!=null){
            populateUI();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setActivityCustomTitle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setActivityCustomTitle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings_menu){
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this) {

            List<Movie> cacheMovieData = new ArrayList<>();
            String sortByKey = MainActivity.this.getString(R.string.pref_sortby_key);
            String defaultSortBy = MainActivity.this.getString(R.string.pref_sortby_popularity);
            String sortByPreference = sp.getString(sortByKey, defaultSortBy);

            @Override
            protected void onStartLoading() {

                if (receiver!=null){
                    forceLoad();
                }
            }

            @Override
            public List<Movie> loadInBackground() {

                try {
                    String json = getJsonResponseFromUrlRequest(NetworkUtils.getUrl(MainActivity.this, sortByPreference));
                    cacheMovieData = parseMovieJson(json);
                    return cacheMovieData;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }

            public void deliverResult(List<Movie> data) {
                cacheMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        mMovieAdapter.setMovieData(data);
        callRecyclerView();
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

    /**
     * This method is responsible for calling the async task to retrieve the movie data and
     * populate the views accordingly
     */
    private void populateUI(){
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);
        sp.registerOnSharedPreferenceChangeListener(this);
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        callProgressBar();
    }

    @Override
    public void onClicked(Movie myMovie) {
        Context context = this;
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(PARCELABLE_KEY,myMovie);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID,null,this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }



    /**
     * Makes the progress bar visible and hides the recycler view
     */
    private void callProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    /**
     * Makes the recycler view visible and hides the progress bar
     */
    private void callRecyclerView() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will set up the activity title based on the user sort preference
     */
    public void setActivityCustomTitle(){
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        String sortPopular = this.getString(R.string.pref_sortby_popularity);
        String sortRate = this.getString(R.string.pref_sortby_rate);
        String prefKey = sp.getString(this.getString(R.string.pref_sortby_key),sortPopular);
        String title = null;
        if (prefKey.equals(sortPopular)) {
            title = this.getResources().getString(R.string.pref_sortby_label_popularity);
        }
        else if (prefKey.equals(sortRate)){
            title = this.getResources().getString(R.string.pref_sortby_label_rate);
        }
        setTitle(title);
    }

    public class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager conn =  (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = conn.getActiveNetworkInfo();
            if (networkInfo != null){
                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID,null,MainActivity.this);
            }
            else{
                String noConnectivityString = getString(R.string.string_not_connected);
                Toast.makeText(MainActivity.this, noConnectivityString, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
