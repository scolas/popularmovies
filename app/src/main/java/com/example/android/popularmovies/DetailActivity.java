package com.example.android.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Data.FavoriteContract;
import com.example.android.popularmovies.Data.FavoriteDbHelper;
import com.example.android.popularmovies.Data.TestUtil;
import com.example.android.popularmovies.Model.AllReviewRequest;
import com.example.android.popularmovies.Model.AllTrailerRequest;
import com.example.android.popularmovies.Model.Movie;
import com.example.android.popularmovies.Model.Review;
import com.example.android.popularmovies.Model.Trailer;
import com.example.android.popularmovies.RetroRequest.MovieApiEndpoint;
import com.example.android.popularmovies.RetroRequest.RetroClient;
import com.example.android.popularmovies.utils.JsonUtils;
import com.example.android.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by scott on 5/27/18.
 */

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie>, TrailerAdapter.TrailerAdapterOnClickHandler, View.OnClickListener  {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    TextView mTitle, mPlot, mRating, mDate, mOrigin, mDescription;
    private static final int MOVIE_LOADER_ID = 1;
    private static final int REVIEW_LOADER_ID = 2;
    String movieID;
    ImageView posterIv;
    private FavoriteListAdapter mAdapter;
    private SQLiteDatabase mDb;
    private Button markFav;
    Movie movie_temp;
    Movie mMovie;
    private static final String api_key = "f6adb7bb919d0b253c04644930bc8df3";


    private List<Trailer> mTrailer;
    private List<Review> mReview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        RecyclerView favoriteRecyclerView;
        //favoriteRecyclerView = (RecyclerView) this.findViewById(R.id.all_favorite_list_view);
        //favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(this));



        markFav = (Button) this.findViewById(R.id.markFav);
        markFav.setOnClickListener(this);


        FavoriteDbHelper dbHelper = new FavoriteDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        //TestUtil.insertFakeData(mDb);
        //Cursor cursor = getAllGuests();
       // mAdapter = new FavoriteListAdapter(this, cursor);


       // mAdapter = new FavoriteListAdapter(this);
        //favoriteRecyclerView.setAdapter(mAdapter);


        posterIv = findViewById(R.id.image_poster);
        mTitle = (TextView) findViewById(R.id.original_title_TxtView);
        mPlot = (TextView) findViewById(R.id.plot_synopsis_TxtView);
        mRating = (TextView) findViewById(R.id.rating_TxtView);
        mDate = (TextView) findViewById(R.id.release_TxtView);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        movieID = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (movieID == null) {
            //EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }




        int loaderId = MOVIE_LOADER_ID;
        int reviewId = REVIEW_LOADER_ID;


        LoaderManager.LoaderCallbacks<Movie> callback = DetailActivity.this;
        //LoaderManager.LoaderCallbacks<String[][]> reviewback = DetailActivity.this;


        Bundle bundleForLoader = null;


        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);
        //getSupportLoaderManager().initLoader(reviewId, bundleForLoader, callback);




    }


    public void getTrailer(){
        Log.d("getTrailer", "begining");
        MovieApiEndpoint service = RetroClient.getRetrofitInstance().create(MovieApiEndpoint.class);
        if(mTrailer != null){

        }
        else{
            Call<AllTrailerRequest> trailerRetro = service.getMovieTrailers( movieID, api_key);
            trailerRetro.enqueue(new Callback<AllTrailerRequest>() {
                @Override
                public void onResponse(Call<AllTrailerRequest> call, Response<AllTrailerRequest> response) {
                    if(response.body() != null){
                        mTrailer = response.body().getTrailers();
                        generateTrailerList(mTrailer);

                    }
                }

                @Override
                public void onFailure(Call<AllTrailerRequest> call, Throwable t) {
                    Toast.makeText(DetailActivity.this, "trailer error", Toast.LENGTH_LONG);
                }
            });
        }

    }
    private void generateTrailerList(List<Trailer> trailers) {


        RecyclerView mTrailerRecyclerView;
        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.all_trailer);



        TrailerAdapter adapter = new TrailerAdapter(trailers, this);
        mTrailerRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        mTrailerRecyclerView.setLayoutManager(layoutManager);

        adapter.notifyDataSetChanged();
    }
    public void getReview(){
        MovieApiEndpoint service = RetroClient.getRetrofitInstance().create(MovieApiEndpoint.class);
        if(mReview != null){
        }
        else{
            Call<AllReviewRequest> reviewRetro = service.getReviews( movieID, api_key);
            reviewRetro.enqueue(new Callback<AllReviewRequest>() {
                @Override
                public void onResponse(Call<AllReviewRequest> call, Response<AllReviewRequest> response) {
                    if(response.body() != null){
                        mReview = response.body().getReviews();
                        generateReviewList(mReview);

                    }
                }

                @Override
                public void onFailure(Call<AllReviewRequest> call, Throwable t) {
                    Toast.makeText(DetailActivity.this, "getReview error", Toast.LENGTH_LONG);
                }
            });
        }

    }

    private void generateReviewList(List<Review> reviews) {
        RecyclerView mReviewRecyclerView;
        mReviewRecyclerView = (RecyclerView) findViewById(R.id.recycler_review);


        recyclerViewAdapterReview adapter = new recyclerViewAdapterReview(reviews);
        mReviewRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        mReviewRecyclerView.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
    }

    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Movie>(this) {

            Movie mMovieData = null;


            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                   // mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }


            @Override
            public Movie loadInBackground() {



                URL movieRequestUrl = NetworkUtils.buildUrl(movieID);
                URL movieReviewRequestUrl = NetworkUtils.buildReviewUrl(movieID);

                try {
                    String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieRequestUrl);
                    String jsonMovieReviewResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieReviewRequestUrl);

                    //Movie simpleJsonMovieData = OpenWeatherJsonUtils
                    //      .getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);
                    Movie moviesTitle = JsonUtils.parseMovieJson(jsonMovieResponse);
                    //String moviesRTitle[][] = JsonUtils.parseMovieReviewJson(jsonMovieReviewResponse);
                    //moviesId = JsonUtils.parseMoviesIdJson(jsonMovieResponse);
                    return moviesTitle;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }


            public void deliverResult(Movie data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {

        if (null == data) {
            //showErrorMessage();
        } else {
            movie_temp = data;
           showMovieDataView(data);

        }

    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {

    }

    private void showMovieDataView(Movie movie) {
        Picasso.with(this).load("http://image.tmdb.org/t/p/w342/"+movie.getImage()).into(posterIv);
        this.getTrailer();
        this.getReview();
        mTitle.setText(movie.getTitle());
        mPlot.setText(movie.getPlot());
        mRating.setText(movie.getRating());
        mDate.setText(movie.getDate());

    }



    public void addToFavoriteList(View view){
        //Toast.makeText(this, "addTofavorite", Toast.LENGTH_SHORT).show();
        String path = "http://image.tmdb.org/t/p/w342/"+ movieID.toString();
        addNewFav(movieID, path);
        //mAdapter.swapCursor(getAllGuests());

    }







    public void addNewFav(String id, String posterPath){



        ContentValues cv = new ContentValues();
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_ID, id);
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_POSTER_PATH, posterPath);


        Cursor cursor = getMovie(id);
        if(cursor != null ){
            if(cursor.getCount() > 1){
                removeFav(id);
                Toast.makeText(this, "movie removed", Toast.LENGTH_SHORT).show();
            }



        }else{

            Log.d("addNewFav", "return mDb"+ cursor.getCount());
            Toast.makeText(this, "movie added", Toast.LENGTH_SHORT).show();

            mDb.insert(FavoriteContract.FavoriteEntry.TABLE_NAME, null, cv);

            //ContentResolver resolver = getContentResolver();

           // resolver.insert(FavoriteContract.FavoriteEntry.CONTENT_URI,cv);
        }


    }


    private boolean removeFav(String id) {
        return mDb.delete(FavoriteContract.FavoriteEntry.TABLE_NAME, FavoriteContract.FavoriteEntry._ID + "=" + id, null) > 0;
    }



    private Cursor getAllGuests() {
        // COMPLETED (6) Inside, call query on mDb passing in the table name and projection String [] order by COLUMN_TIMESTAMP
        return mDb.query(
                FavoriteContract.FavoriteEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private Cursor getMovie(String id) {
        String [] selection = {id};
        return mDb.query(
                FavoriteContract.FavoriteEntry.TABLE_NAME,
                null,
                FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_ID + "=?",
                selection,
                null,
                null,
                null
        );
    }


    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(String[] movie) {

        mTitle.setText(movie[1]);
        //mPlot.setText(movie.getPlot());
        //mRating.setText(movie.getRating());
        //mDate.setText(movie.getDate());


    }




    @Override
    public void onClick(Trailer movie) {

        String url = movie.getTrailerUrl(movie.getKey());
        Uri youtubeUri = Uri.parse(url);

        Intent intent = new Intent(Intent.ACTION_VIEW, youtubeUri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            this.startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        int action = view.getId();
        switch (action){
            case R.id.markFav:
                String path = "http://image.tmdb.org/t/p/w342/"+ movieID.toString();
                addNewFav(movieID, path);
                //addToFavoriteList(view);

        }
    }
}
