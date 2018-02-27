package uby.luca.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uby.luca.popularmovies.adapters.FavoriteMovieAdapter;
import uby.luca.popularmovies.adapters.MovieAdapter;
import uby.luca.popularmovies.data.MovieContract;
import uby.luca.popularmovies.loaders.MovieAsyncTaskLoader;
import uby.luca.popularmovies.POJOs.Movie;

import static uby.luca.popularmovies.adapters.MovieAdapter.PARCELED_MOVIE;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieOnClickHandler, FavoriteMovieAdapter.MovieOnClickHandler {
    @BindView(R.id.main_rv)
    RecyclerView mainRv;
    public static final int HIGHEST_RATED = 1;
    public static final int MOST_POPULAR = 2;
    public static final int FAVORITES = 3;
    private int sortOrder = MOST_POPULAR;//default sort order
    private static final String SORT_KEY = "sortOrder";
    GridLayoutManager layoutManager;
    private final int ONLINE_LOADER_ID = 37;
    private final int FAVORITE_LOADER_ID = 38;
    private MovieAdapter onlineAdapter;
    private FavoriteMovieAdapter favoriteAdapter;
    private Context mContext = this;

    private LoaderManager.LoaderCallbacks<ArrayList<Movie>> onlineLoader = new LoaderManager.LoaderCallbacks<ArrayList<Movie>>() {    //https://stackoverflow.com/questions/15643907/multiple-loaders-in-same-activity
        @Override
        public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
            return new MovieAsyncTaskLoader(mContext, sortOrder);
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
            if (data == null) {
                Toast.makeText(mContext, R.string.returned_data_is_null, Toast.LENGTH_SHORT).show();
            } else {
                onlineAdapter.add(data);
                mainRv.setAdapter(onlineAdapter);
                Log.d("MainActivity", "onlineLoader finished loading");
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
        }
    };


    private LoaderManager.LoaderCallbacks<Cursor> favoritesLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(mContext, MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data == null) {
                Toast.makeText(mContext, R.string.returned_cursor_is_null, Toast.LENGTH_SHORT).show();
            } else {
                favoriteAdapter.add(data);
                mainRv.setAdapter(favoriteAdapter);
                if (data.getCount() == 0) {
                    Toast.makeText(mContext, R.string.no_favorites_yet, Toast.LENGTH_SHORT).show();
                }
                Log.d("MainActivity", "favoritesLoader finished loading");
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SORT_KEY)) {
                sortOrder = (savedInstanceState.getInt(SORT_KEY)); //on rotation remember the last sorting criteria we used
            }
        }

        int orientation = this.getResources().getConfiguration().orientation;   // from Stack Overflow: https://stackoverflow.com/questions/3663665/how-can-i-get-the-current-screen-orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            layoutManager = new GridLayoutManager(this, 4);
        }

        mainRv.setLayoutManager(layoutManager);
        onlineAdapter = new MovieAdapter(this, this);
        favoriteAdapter = new FavoriteMovieAdapter(this, this);

        if (!isOnline()) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_LONG).show();
        } else {
            if (sortOrder == HIGHEST_RATED || sortOrder == MOST_POPULAR) {    //check to see which loader we need to use, needed to avoid jumping to another sorting criteria after rotations
                getSupportLoaderManager().initLoader(ONLINE_LOADER_ID, null, onlineLoader);
            } else {
                getSupportLoaderManager().initLoader(FAVORITE_LOADER_ID, null, favoritesLoader);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.highest_rated:
                sortOrder = HIGHEST_RATED;
                if (!isOnline()) {
                    Toast.makeText(this, R.string.not_connected, Toast.LENGTH_LONG).show();
                } else {
                    if (getSupportLoaderManager().getLoader(FAVORITE_LOADER_ID) != null) {
                        Log.d("MainActivity", "Calling online loader, stopping favorite loader");
                        getSupportLoaderManager().destroyLoader(FAVORITE_LOADER_ID);    //if switching to a different Loader, check if the other Loader is still running and stop it. Prevents the wrong Loader returning data when coming back from DetailActivity
                    }
                    getSupportLoaderManager().restartLoader(ONLINE_LOADER_ID, null, onlineLoader);
                }
                return true;
            case R.id.menu_most_popular:
                sortOrder = MOST_POPULAR;
                if (!isOnline()) {
                    Toast.makeText(this, R.string.not_connected, Toast.LENGTH_LONG).show();
                } else {
                    if (getSupportLoaderManager().getLoader(FAVORITE_LOADER_ID) != null) {
                        Log.d("MainActivity", "Calling online loader, stopping favorite loader");
                        getSupportLoaderManager().destroyLoader(FAVORITE_LOADER_ID);
                    }
                    getSupportLoaderManager().restartLoader(ONLINE_LOADER_ID, null, onlineLoader);
                }
                return true;
            case R.id.favorites:
                sortOrder = FAVORITES;
                if (getSupportLoaderManager().getLoader(ONLINE_LOADER_ID) != null) {
                    Log.d("MainActivity", "Calling favorite loader, stopping online loader");
                    getSupportLoaderManager().destroyLoader(ONLINE_LOADER_ID);
                }
                getSupportLoaderManager().restartLoader(FAVORITE_LOADER_ID, null, favoritesLoader);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isOnline() {        // from Stack Overflow: https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SORT_KEY, sortOrder);
    }

    @Override
    public void movieOnClickImplementation(Movie clickedMovie) {
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCELED_MOVIE, clickedMovie);
        intent.putExtras(bundle);

        startActivity(intent);
    }
}
