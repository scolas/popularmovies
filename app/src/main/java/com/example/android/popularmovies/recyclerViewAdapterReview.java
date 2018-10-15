package com.example.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Model.Review;
import com.example.android.popularmovies.Model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by scott on 8/4/18.
 */

public class recyclerViewAdapterReview extends RecyclerView.Adapter<recyclerViewAdapterReview.ReviewListAdapter> {
    private List<Review> mReviews;

    public recyclerViewAdapterReview(List<Review> reviews) {
        mReviews = reviews;
        //mOnClickListener = listener;
    }

    @NonNull
    @Override
    public ReviewListAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_item, parent, false);

        return new ReviewListAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewListAdapter holder, int position) {
        Review review = mReviews.get(position);


        holder.mReviewtxt.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }



    public class ReviewListAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView mReviewtxt;

        public ReviewListAdapter(View itemView) {
            super(itemView);
            mReviewtxt = (TextView) itemView.findViewById(R.id.review_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // mOnClickListener.onListItemClick(getAdapterPosition());
        }
    }




}
