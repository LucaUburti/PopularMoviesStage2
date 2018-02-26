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


/**
 * Created by uburti on 18/02/2018.
 */

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.MovieHolder> {

    private Cursor movieCursor;
    private Context mContext;
    static final String PARCELED_MOVIE = "parceledMovie";

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
        String title = movieCursor.getString(movieCursor.getColumnIndex("title")); //TODO replace this string
        String poster = movieCursor.getString(movieCursor.getColumnIndex("poster")); //TODO replace this string


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
            String movieId= movieCursor.getString(movieCursor.getColumnIndex("movieId")); //TODO replace this string
            //... repeat for "movieId","title","poster","voteAverage","plot","releaseDate"
            Movie clickedMovie=new Movie("movieId","title","poster","voteAverage","plot","releaseDate");

            movieOnClickHandler.movieOnClickImplementation(clickedMovie);
        }
    }
}
