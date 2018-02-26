package uby.luca.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uby.luca.popularmovies.POJOs.Trailer;
import uby.luca.popularmovies.R;

import static android.content.ContentValues.TAG;

/**
 * Created by uburti on 25/02/2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {
    private ArrayList<Trailer> trailerList;
    private Context mContext;

    public TrailerAdapter(Context context) {
        this.mContext = context;
    }

    public void add(ArrayList<Trailer> trailerList) {
        this.trailerList = trailerList;
        notifyDataSetChanged();
    }

    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View trailerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new TrailerHolder(trailerView);
    }

    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {
        Trailer currentTrailer = trailerList.get(position);
        holder.trailerNameTv.setText(currentTrailer.getName());
        holder.trailerThumbnailIv.setContentDescription(currentTrailer.getName());

        String trailerThumbnailUrl = "http://img.youtube.com/vi/" + currentTrailer.getKey() + "/0.jpg";
        Picasso.with(mContext)
                .load(trailerThumbnailUrl)
                .placeholder(R.drawable.loading_popcorn)
                .error(R.drawable.img_not_found)
                .into(holder.trailerThumbnailIv);
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView trailerThumbnailIv;
        TextView trailerNameTv;

        TrailerHolder(View trailerView) {
            super(trailerView);
            trailerThumbnailIv = trailerView.findViewById(R.id.trailer_thumbnail_iv);
            trailerNameTv = trailerView.findViewById(R.id.trailer_name_tv);
            trailerView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String key = trailerList.get(getAdapterPosition()).getKey();
            String trailerYoutubeUrl = "http://www.youtube.com/watch?v=" + key;
            Log.d(TAG, "onClick: " + trailerYoutubeUrl);
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerYoutubeUrl));
            if (i.resolveActivity(mContext.getPackageManager()) != null) {
                mContext.startActivity(i);
            } else {
                Toast.makeText(mContext, R.string.no_apps_to_open_resource, Toast.LENGTH_SHORT).show();
            }

        }
    }


}
