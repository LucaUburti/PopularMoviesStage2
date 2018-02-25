package uby.luca.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by uburti on 25/02/2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<uby.luca.popularmovies.TrailerAdapter.TrailerHolder> {
    private ArrayList<DetailActivity.Trailer> trailerList;
    private Context mContext;

    public TrailerAdapter(Context context) {
        this.mContext = context;
    }
    void add(ArrayList<DetailActivity.Trailer> trailerList){
        this.trailerList=trailerList;
        notifyDataSetChanged();
    }

    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View trailerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new TrailerHolder(trailerView);
    }

    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {
        DetailActivity.Trailer currentTrailer=trailerList.get(position);
        holder.trailerNameTv.setText(currentTrailer.getName());

        String trailerUrl="http://img.youtube.com/vi/" + currentTrailer.getKey() + "/0.jpg";
        Picasso.with(mContext)
                .load(trailerUrl)
                .placeholder(R.drawable.loading_popcorn)
                .error(R.drawable.img_not_found)
                .into(holder.trailerThumbnailIv);
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public class TrailerHolder extends RecyclerView.ViewHolder {
        ImageView trailerThumbnailIv;
        TextView trailerNameTv;
        public TrailerHolder(View trailerView) {
            super(trailerView);
            trailerThumbnailIv = trailerView.findViewById(R.id.trailer_thumbnail_iv);
            trailerNameTv=trailerView.findViewById(R.id.trailer_name_tv);
        }
    }
}
