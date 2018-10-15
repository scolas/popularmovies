package com.example.android.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.android.popularmovies.Model.Trailer;

import android.view.View.OnClickListener;
import java.util.List;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by scott on 8/1/18.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private List<Trailer> mTrailers;
    private final TrailerAdapterOnClickHandler mOnClickListener;

    public interface TrailerAdapterOnClickHandler{
        void onClick(Trailer movie);
    }

    public TrailerAdapter(List<Trailer> trailers, TrailerAdapterOnClickHandler listener) {
        mTrailers = trailers;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.favorite_list_item, parent, false);

        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Trailer trailer = mTrailers.get(position);


        holder.mTrailertxt.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
            return mTrailers.size();
    }



    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView mTrailertxt;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            mTrailertxt = (TextView) itemView.findViewById(R.id.favorite_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           // mOnClickListener.onListItemClick(getAdapterPosition());
            int adapterPosition = getAdapterPosition();
            Trailer trailer = mTrailers.get(adapterPosition);
            mOnClickListener.onClick(trailer);
        }


    }



}
