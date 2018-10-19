package com.example.android.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Data.FavoriteContract;
import com.example.android.popularmovies.Data.MoviesContentProvider;
import com.example.android.popularmovies.Data.SortPreferences;
import com.example.android.popularmovies.Model.Movie;
import com.example.android.popularmovies.utils.JsonUtils;
import com.example.android.popularmovies.utils.NetworkUtils;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements recyclerAdapter.recyclerAdapterOnClickHandler, LoaderManager.LoaderCallbacks<String[]>,SharedPreferences.OnSharedPreferenceChangeListener {
    RecyclerView recyclerView;
    recyclerAdapter adapter;
    int items = 10;
    static int numOfCol = 2;
    private Toast mToast;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    String[] moviesId;
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;
    private SQLiteDatabase mDb;
    private MoviesContentProvider mCP;
    MoviesContentProvider moviesContentProvider = new MoviesContentProvider();
    String[] moviesPosterPath;
    List<Movie> mFavorites;

    private static final int MOVIE_LOADER_ID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numOfCol));

        adapter = new recyclerAdapter(this);

        recyclerView.setAdapter(adapter);


        int loaderId = MOVIE_LOADER_ID;
        setupSharedPreferences();

        LoaderManager.LoaderCallbacks<String[]> callback = MainActivity.this;


        Bundle bundleForLoader = null;


        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);
    }



    @Override
    public Loader<String[]> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String[]>(this) {

            String[] mMovieData = null;


            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }


            @Override
            public String[] loadInBackground() {

                //String sortOrder = SortPreferences.
                String locationQuery = SortPreferences
                        .getPreferredSorting(MainActivity.this);
                URL movieRequestUrl = NetworkUtils.buildUrl();
                //locationQuery = "favorite";
                if(locationQuery == "highest_rated"){
                    movieRequestUrl = NetworkUtils.buildTopUrl();
                }else if(locationQuery == "favorite"){
                    return getAllFavorite();
                }
                else{
                    movieRequestUrl = NetworkUtils.buildUrl();
                }

                try {
                    String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieRequestUrl);
                    //Log.e(" this is thejson",""+jsonMovieResponse);
                    moviesPosterPath = JsonUtils.parseMoviesJson(jsonMovieResponse);
                    moviesId = JsonUtils.parseMoviesIdJson(jsonMovieResponse);


                    return moviesPosterPath;
                    //return getAllFavorite();
                } catch (Exception e) {
                    e.printStackTrace();

                    return null;
                }
            }


            public void deliverResult(String[] data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        adapter.setMovieData(data);
        if (null == data) {
            showErrorMessage();
        } else {
            showMovieDataView();
        }

    }


    public String[] getAllFavorite(){
        loadSavedFavorites();
        String[] paths = {"",""};
        Log.d("loaded favs", String.valueOf(mFavorites));
       // Toast.makeText(this, (CharSequence) mFavorites,Toast.LENGTH_LONG);
        return paths;
       /* int Moive_Fav = 201;


       //Cursor cursor =  mCP.query(FavoriteContract.BASE_URI,null,null,null,null);

        ContentResolver resolver = getContentResolver();
        Cursor cursor = getContentResolver().query(FavoriteContract.FavoriteEntry.CONTENT_URI,null,null,null,null);
        //Cursor cursor = mDb.query(FavoriteContract.FavoriteEntry.CONTENT_URI,null,null,null,null);
        /*Cursor cursor =  mDb.query(
                FavoriteContract.FavoriteEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if(cursor != null){
            String[] movieImagePaths = new String[cursor.getCount()];
            cursor.moveToFirst();
            for(int i=0; i<cursor.getCount(); i++){
                cursor.moveToPosition(i);
                String path = cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_POSTER_PATH));
                movieImagePaths[i] = path;
            }
            Log.d("favorite data", movieImagePaths.toString());
            return movieImagePaths;
        }else{
            return null;
        }

*/
    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        PREFERENCES_HAVE_BEEN_UPDATED = true;
    }
    @Override
    protected void onStart() {
        super.onStart();


        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            Log.d("Main activity", "onStart: preferences were updated");
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    private void setupSharedPreferences() {



        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }
    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
    int mSort;
    public void setSort(String newSortKey) {

        @ColorInt
        int sortOrder = 0;

        @ColorInt
        int trailColor;

        if (newSortKey.equals(this.getString(R.string.highest_rated))) {
            sortOrder = 1;
        }else if(newSortKey.equals(this.getString(R.string.most_popular))) {
            sortOrder = 2;
        }

        mSort = sortOrder;
    }




    @Override
    public void onClick(int idForPoster) {
        String api_moive_id = moviesId[idForPoster];

        //Toast.makeText(this, R.string.detail_error_message+" id"+idForPoster+" movir"+moviesId[(int)idForPoster], Toast.LENGTH_SHORT).show();
        Context context = this;
        Class detailClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, detailClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, api_moive_id);
        startActivity(intentToStartDetailActivity);
    }

    private void loadSavedFavorites() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavorites().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> favoriteList) {
                Log.d("getFavs", "Updating list of favorites from LiveData in ViewModel");
                mFavorites = favoriteList;

               // if (mSortingBy.equals(FAVORITES)) {
                    //mMovies = favoriteList;
                    mFavorites = favoriteList;
                    //viewFavorites(mMovies);

                   // if (mMovies.size() < 1) {
                    //    Toast.makeText(getApplicationContext(), R.string.noSavedFavoritesMessage, Toast.LENGTH_LONG).show();
                   // }
              //  }
            }
        });
    }
}
