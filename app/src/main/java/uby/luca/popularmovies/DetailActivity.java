package uby.luca.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uby.luca.popularmovies.POJOs.Movie;
import uby.luca.popularmovies.POJOs.Review;
import uby.luca.popularmovies.POJOs.Trailer;
import uby.luca.popularmovies.adapters.MovieAdapter;
import uby.luca.popularmovies.adapters.ReviewAdapter;
import uby.luca.popularmovies.adapters.TrailerAdapter;
import uby.luca.popularmovies.data.MovieContract;
import uby.luca.popularmovies.loaders.ReviewAsyncTaskLoader;
import uby.luca.popularmovies.loaders.TrailerAsyncTaskLoader;

public class DetailActivity extends AppCompatActivity {
    private Movie movie;
    private final int TRAILERLOADER_ID = 31;
    private final int REVIEWLOADER_ID = 32;
    private Context context = this;

    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.description_tv)
    TextView descriptionTv;
    @BindView(R.id.poster_iv)
    ImageView posterIv;
    @BindView(R.id.average_vote_tv)
    TextView averageVoteTv;
    @BindView(R.id.release_date_tv)
    TextView releaseDate;
    @BindView(R.id.trailer_rv)
    RecyclerView trailerRv;
    @BindView(R.id.review_rv)
    RecyclerView reviewRv;
    @BindView(R.id.fav_iv)
    ImageView favIv;

    //https://stackoverflow.com/questions/15643907/multiple-loaders-in-same-activity
    private LoaderManager.LoaderCallbacks<ArrayList<Trailer>> trailerLoader = new LoaderManager.LoaderCallbacks<ArrayList<Trailer>>() {
        @Override
        public Loader<ArrayList<Trailer>> onCreateLoader(int id, Bundle args) {
            return new TrailerAsyncTaskLoader(context, movie.getMovieId());
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> data) {
            if (data == null) {
                Toast.makeText(context, R.string.returned_data_is_null, Toast.LENGTH_SHORT).show();
            } else {
                trailerAdapter.add(data);
                Log.d("DetailActivity", "number of trailers found:" + data.size());

                trailerRv.setAdapter(trailerAdapter);

            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {
        }
    };

    private LoaderManager.LoaderCallbacks<ArrayList<Review>> reviewLoader = new LoaderManager.LoaderCallbacks<ArrayList<Review>>() {
        @Override
        public Loader<ArrayList<Review>> onCreateLoader(int id, Bundle args) {
            return new ReviewAsyncTaskLoader(context, movie.getMovieId());
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data) {
            if (data == null) {
                Toast.makeText(context, R.string.returned_data_is_null, Toast.LENGTH_SHORT).show();
            } else {
                reviewAdapter.add(data);
                Log.d("DetailActivity", "number of reviews found:" + data.size());
                reviewRv.setAdapter(reviewAdapter);
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Review>> loader) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            movie = bundle.getParcelable(MovieAdapter.PARCELED_MOVIE);
            if (movie != null) {
                titleTv.setText(movie.getTitle());
                descriptionTv.setText(movie.getPlot());

                String formattedVote = movie.getVoteAverage() + "/10";
                averageVoteTv.setText(formattedVote);

                String formattedDate = movie.getReleaseDate().replace("-", "/");
                releaseDate.setText(formattedDate);

                posterIv.setContentDescription(movie.getTitle());
                Picasso.with(this)
                        .load(movie.getPoster())
                        .placeholder(R.drawable.loading_popcorn)
                        .error(R.drawable.img_not_found)
                        .into(posterIv);

                if (isFavorite()) {
                    favIv.setImageResource(android.R.drawable.btn_star_big_on);
                } else {
                    favIv.setImageResource(android.R.drawable.btn_star_big_off);
                }


                favIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFavorite()) {  //already a favourite: call CP to delete movie from db
                            String stringUri = MovieContract.MovieEntry.CONTENT_URI.toString() + "/" + movie.getMovieId();
                            getContentResolver().delete(Uri.parse(stringUri), null, null);

                            favIv.setImageResource(android.R.drawable.btn_star_big_off);
                            Toast.makeText(context, R.string.removed_from_favorites, Toast.LENGTH_SHORT).show();
                        } else { //new favourite: call CP to insert movie into db
                            ContentValues cv = new ContentValues();
                            cv.put(MovieContract.MovieEntry.COLUMN_MOVIEID, movie.getMovieId());
                            cv.put(MovieContract.MovieEntry.COLUMN_PLOT, movie.getPlot());
                            cv.put(MovieContract.MovieEntry.COLUMN_POSTER, movie.getPoster());
                            cv.put(MovieContract.MovieEntry.COLUMN_RELEASEDATE, movie.getReleaseDate());
                            cv.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
                            cv.put(MovieContract.MovieEntry.COLUMN_VOTEAVERAGE, movie.getVoteAverage());
                            Log.d("DetailActivity", "onClick: inserting ContentValues:" + cv.toString());

                            getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);

                            favIv.setImageResource(android.R.drawable.btn_star_big_on);
                            Toast.makeText(context, R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                trailerRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                trailerAdapter = new TrailerAdapter(this);

                reviewRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                reviewAdapter = new ReviewAdapter(this);

                getSupportLoaderManager().initLoader(TRAILERLOADER_ID, null, trailerLoader);
                getSupportLoaderManager().initLoader(REVIEWLOADER_ID, null, reviewLoader);

            } else {
                Toast.makeText(this, R.string.error_movie_details, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.error_movie_details, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isFavorite() { //checks if movieId is already present locally
        String stringUri = MovieContract.MovieEntry.CONTENT_URI.toString() + "/" + movie.getMovieId();
        Log.d("DetailActivity", "isFavorite: checking movie with ID: " + movie.getMovieId());
        Cursor queryResult = getContentResolver().query(Uri.parse(stringUri), null, null, null, null);
        if (queryResult == null) {
            Log.e("DetailActivity", "isFavorite: null results... not present?");
            return false;
        }

        if (queryResult.getCount() > 0) {
            Log.d("DetailActivity", "isFavorite: movie already present: " + queryResult.getCount() + " times!");
            queryResult.close();
            return true;
        } else {
            Log.d("DetailActivity", "isFavorite: movie not present.");
            queryResult.close();
            return false;
        }

    }


}
