package uby.luca.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import uby.luca.popularmovies.POJOs.Movie;
import uby.luca.popularmovies.R;
import uby.luca.popularmovies.data.MovieContract;


/**
 * Created by uburti on 18/02/2018.
 */

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.MovieHolder> {

    private Cursor movieCursor;
    private Context mContext;

    private final MovieOnClickHandler movieOnClickHandler;

    public FavoriteMovieAdapter(Context context, MovieOnClickHandler movieOnClickHandler) {
        this.mContext = context;
        this.movieOnClickHandler = movieOnClickHandler;
    }


    public interface MovieOnClickHandler {
        void movieOnClickImplementation(Movie clickedMovie);
    }

    public void add(Cursor movieCursor) {
        this.movieCursor = movieCursor;
        notifyDataSetChanged();
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        movieCursor.moveToPosition(position);
        String title = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
        String poster = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER));

        holder.movieIv.setContentDescription(title);
        Picasso.with(mContext)
                .load(poster)
                .placeholder(R.drawable.loading_popcorn)
                .error(R.drawable.img_not_found)
                .into(holder.movieIv);
    }

    @Override
    public int getItemCount() {
        return movieCursor.getCount();
    }

    class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView movieIv;

        MovieHolder(View view) {
            super(view);
            movieIv = view.findViewById(R.id.movie_iv);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            movieCursor.moveToPosition(getAdapterPosition());

            String movieId = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIEID));
            String title = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
            String poster = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER));
            String voteAverage = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTEAVERAGE));
            String plot = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_PLOT));
            String releaseDate = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASEDATE));

            Movie clickedMovie = new Movie(movieId, title, poster, voteAverage, plot, releaseDate);

            movieOnClickHandler.movieOnClickImplementation(clickedMovie);
        }
    }
}
