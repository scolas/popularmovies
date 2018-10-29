package com.example.android.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements recyclerAdapter.recyclerAdapterOnClickHandler, LoaderManager.LoaderCallbacks<String[]>,SharedPreferences.OnSharedPreferenceChangeListener, ListItemClickListener {
    RecyclerView recyclerView;
    RecyclerView recyclerView1;
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

    String[] mMovieData = null;
    RecyclerView.LayoutManager mLayoutManager;


    List<Movie> mMovies;
    private MovieAdapter mAdapter;
    Parcelable mListState;


    private static final int MOVIE_LOADER_ID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        recyclerView.setLayoutManager(new GridLayoutManager(this, numOfCol));

        adapter = new recyclerAdapter(this);

        recyclerView.setAdapter(adapter);

        mMovies = new ArrayList<>();
        mAdapter = new MovieAdapter(mMovies, this);
        mAdapter.setMovies(mMovies);

        int loaderId = MOVIE_LOADER_ID;
        setupSharedPreferences();

        if(PREFERENCES_HAVE_BEEN_UPDATED){adapter.notifyDataSetChanged();}

        loadSavedFavorites();
        LoaderManager.LoaderCallbacks<String[]> callback = MainActivity.this;


        Bundle bundleForLoader = null;


        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);
    }



    @Override
    public Loader<String[]> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String[]>(this) {




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


                String locationQuery = SortPreferences
                        .getPreferredSorting(MainActivity.this);
                URL movieRequestUrl = NetworkUtils.buildUrl();

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
                    moviesPosterPath = JsonUtils.parseMoviesJson(jsonMovieResponse);
                    moviesId = JsonUtils.parseMoviesIdJson(jsonMovieResponse);


                    return moviesPosterPath;
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
       // mLoadingIndicator.setVisibility(View.INVISIBLE);
       // adapter.setMovieData(data);
        if (null == data) {
        //    showErrorMessage();
        } else {
          //  showMovieDataView();
            //adapter.notifyDataSetChanged();
        }

    }


    public String[] getAllFavorite(){
        loadSavedFavorites();
        int i = 0;
        String[] paths = new String[10];

        if(mFavorites == null){
            //paths = {" "," "};
            Log.d("loaded favs", "mfav null");
            return paths;
        }
        if(mFavorites.size() < 1){
            //paths = {" "," "};
            Log.d("loaded favs", "mfav size");
            return paths;
        }


        for(Movie movie : mFavorites){
            //int mov = movie.getVideo();
            paths[i] = movie.getVideo();
            i++;
        }

        Log.d("loaded favs", String.valueOf(paths));
        return paths;

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
                mMovies = favoriteList;

            }
        });
    }



    private void viewFavorites(List<Movie> favorites) {

        if (favorites != null) {
            mMovies = favorites;
        }
        generateMovieList(mMovies);
        recyclerView1.getAdapter().notifyDataSetChanged();
        if (mMovies.size() < 1) {
            //Toast.makeText(getApplicationContext(), "no favorite", Toast.LENGTH_LONG).show();
        }
    }


    private void generateMovieList(List<Movie> movieList) {
        mAdapter = new MovieAdapter(movieList, this);
        mAdapter.setMovies(movieList);
        recyclerView1.setAdapter(mAdapter);
        mLayoutManager = new GridLayoutManager(this, 2);

        //Restore adapter to maintain scroll position

        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }

        recyclerView1.setLayoutManager(mLayoutManager);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }
}
