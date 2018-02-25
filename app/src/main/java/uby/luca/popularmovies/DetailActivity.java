package uby.luca.popularmovies;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    Movie movie;
    private final int TRAILERLOADER_ID = 31;
    private final int REVIEWLOADER_ID = 32;

    TrailerAdapter trailerAdapter;
//  ReviewAdapter reviewAdapter = new ReviewAdapter(this);

    //https://stackoverflow.com/questions/15643907/multiple-loaders-in-same-activity
    private LoaderManager.LoaderCallbacks<ArrayList<Trailer>> trailerLoader = new LoaderManager.LoaderCallbacks<ArrayList<Trailer>>() {
        @Override
        public  Loader<ArrayList<Trailer>> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<ArrayList<Trailer>>(getApplicationContext()) {
                @Nullable
                @Override
                public ArrayList<Trailer> loadInBackground() {
                    return null;
                }

                @Override
                protected void onStartLoading() {
                    forceLoad();
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> data) {
            ArrayList<Trailer> fakeResults=new ArrayList<>();
            fakeResults.add(new Trailer("3b946aGm0zs","Official Trailer 1"));
            fakeResults.add(new Trailer("64-iSYVmMVY","Official Trailer 2"));
            fakeResults.add(new Trailer("3b946aGm0zs","Official Trailer 3"));
            fakeResults.add(new Trailer("64-iSYVmMVY","Official Trailer 4"));
            fakeResults.add(new Trailer("3b946aGm0zs","Official Trailer 5"));
            fakeResults.add(new Trailer("64-iSYVmMVY","Official Trailer 6"));
            trailerAdapter.add(fakeResults);
            trailerRv.setAdapter(trailerAdapter);


        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks<ArrayList<Review>> reviewLoader = new LoaderManager.LoaderCallbacks<ArrayList<Review>>() {
        @Override
        public Loader<ArrayList<Review>> onCreateLoader(int id, Bundle args) {
            return null;
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data) {


        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Review>> loader) {

        }
    };


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



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }


        trailerRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        trailerAdapter = new TrailerAdapter(this);

        //reviewRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //ReviewAdapter reviewAdapter = new ReviewAdapter(this);

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

                Picasso.with(this)
                        .load(movie.getPoster())
                        .placeholder(R.drawable.loading_popcorn)
                        .error(R.drawable.img_not_found)
                        .into(posterIv);

                getSupportLoaderManager().initLoader(TRAILERLOADER_ID, null, trailerLoader);
                //getSupportLoaderManager().initLoader(REVIEWLOADER_ID, null, reviewLoader);

            } else {
                Toast.makeText(this, R.string.error_movie_details, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.error_movie_details, Toast.LENGTH_SHORT).show();
        }
    }

    class Trailer {
        String key;
        String name;

        Trailer(String key, String name) {
            this.key = key;
            this.name = name;
        }

        String getKey() {
            return key;
        }

        void setKey(String key) {
            this.key = key;
        }

        String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }
    }

    class Review {
        String author;
        String content;
        String url;

        Review(String author, String content, String url) {
            this.author = author;
            this.content = content;
            this.url = url;
        }

        String getAuthor() {
            return author;
        }

        void setAuthor(String author) {
            this.author = author;
        }

        String getContent() {
            return content;
        }

        void setContent(String content) {
            this.content = content;
        }

        String getUrl() {
            return url;
        }

        void setUrl(String url) {
            this.url = url;
        }
    }
}
