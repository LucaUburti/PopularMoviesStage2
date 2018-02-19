package uby.luca.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by uburti on 18/02/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private ArrayList<Movie> movieList = new ArrayList<>();
    private Context mContext;

    MovieAdapter(Context context) {
        this.mContext = context;
    }

    void add(Movie movie) { //single movie added
        movieList.add(movie);
        notifyItemInserted(movieList.size() - 1);
    }
    void add(ArrayList<Movie> movieList){ //whole list added
        this.movieList=movieList;
        notifyDataSetChanged();
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        Movie movie = movieList.get(position);

        Picasso.with(mContext)
                .load(movie.getPoster())
                .placeholder(R.drawable.loading_popcorn)
                .error(R.drawable.img_not_found)
                .into(holder.movieIv);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView movieIv;

        MovieHolder(View view) {
            super(view);
            movieIv = view.findViewById(R.id.movie_iv);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, movieList.get(getAdapterPosition()).getTitle());
            mContext.startActivity(intent);
        }
    }
}
