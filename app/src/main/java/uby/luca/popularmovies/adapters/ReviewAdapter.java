package uby.luca.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import uby.luca.popularmovies.POJOs.Review;
import uby.luca.popularmovies.R;

/**
 * Created by uburti on 25/02/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    private ArrayList<Review> reviewList;
    private Context mContext;

    public ReviewAdapter(Context context) {
        this.mContext = context;
    }

    public void add(ArrayList<Review> reviewList) {
        this.reviewList = reviewList;
        notifyDataSetChanged();
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View reviewView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewHolder(reviewView);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        Review currentReview = reviewList.get(position);
        holder.reviewAuthorTv.setText(currentReview.getAuthor());
        holder.reviewContentTv.setText(currentReview.getContent());
        holder.reviewUrlTv.setText(currentReview.getUrl());

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    class ReviewHolder extends RecyclerView.ViewHolder {
        TextView reviewAuthorTv;
        TextView reviewContentTv;
        TextView reviewUrlTv;

        ReviewHolder(View reviewView) {
            super(reviewView);
            reviewAuthorTv = reviewView.findViewById(R.id.review_author_tv);
            reviewContentTv = reviewView.findViewById(R.id.review_content_tv);
            reviewUrlTv = reviewView.findViewById(R.id.review_url_tv);
        }

    }

}
