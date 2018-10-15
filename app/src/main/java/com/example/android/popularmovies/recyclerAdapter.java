package com.example.android.popularmovies;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.zip.Inflater;

/**
 * Created by scott on 5/6/18.
 */

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.recyclerAdapterViewHolder> {
    int moviesCount = 0;
    private String[] mMovieData;
    private final recyclerAdapterOnClickHandler mClickHandler;
    private Toast mToast;



    public interface recyclerAdapterOnClickHandler {
        void onClick(int movies);
    }

    public recyclerAdapter(recyclerAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }



    @Override
    public recyclerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutforItemId = R.layout.movie_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);


        View view = layoutInflater.inflate(layoutforItemId,viewGroup,false);
        //RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
        return new recyclerAdapterViewHolder(view);
    }





    public class recyclerAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener{
        //TextView mitemView;
        public final ImageView mitemView;

        public recyclerAdapterViewHolder(View itemView){
            super(itemView);
            //mitemView = (TextView) itemView.findViewById(R.id.movieimageView);
            mitemView = (ImageView) itemView.findViewById(R.id.movieimageView);
            itemView.setOnClickListener(this);

        }


        //void bind(int index){mitemView.setText(String.valueOf(index));}
        void bind(int index){mitemView.setImageResource(R.drawable.blackpanther);}



        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            //String pMovie = mMovieData[adapterPosition];
            mClickHandler.onClick(adapterPosition);
        }




    }

    @Override
    public void onBindViewHolder(recyclerAdapterViewHolder holder, int position) {
        String moviePoster = mMovieData[position];

        Picasso.with(holder.mitemView.getContext()).load("http://image.tmdb.org/t/p/w342/"+moviePoster).into(holder.mitemView);
        //holder.mitemView.setImageResource(R.drawable.blackpanther);
        //holder.bind(position);
    }


    public void setMovieData(String[] movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.length;
    }


}
