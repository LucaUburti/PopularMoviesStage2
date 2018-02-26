package uby.luca.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
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
import uby.luca.popularmovies.loaders.MovieAsyncTaskLoader;
import uby.luca.popularmovies.POJOs.Movie;

import static uby.luca.popularmovies.adapters.MovieAdapter.PARCELED_MOVIE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movie>>, MovieAdapter.MovieOnClickHandler, FavoriteMovieAdapter.MovieOnClickHandler {

    @BindView(R.id.main_rv)
    RecyclerView mainRv;
    public static final int HIGHEST_RATED = 1;
    public static final int MOST_POPULAR = 2;
    public static final int FAVORITES = 3;
    public int sortOrder = MOST_POPULAR;//default sort order
    public  final int LOADER_ID = 37;

    private MovieAdapter mAdapter;
    private FavoriteMovieAdapter mFavoriteAdapter;
    private Context mContext = this;

    private LoaderManager.LoaderCallbacks<Cursor> favoritesLoader = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return null;
            //return new CursorLoader(mContext, Uri.parse("testURI"), null, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            Log.d("Loader", "onLoadFinished");
            if (data == null) {
                Toast.makeText(mContext, R.string.returned_cursor_is_null, Toast.LENGTH_SHORT).show();
            } else {
                mFavoriteAdapter.add(data);
                mainRv.setAdapter(mFavoriteAdapter);
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
        GridLayoutManager layoutManager;

        // from Stack Overflow:
        //https://stackoverflow.com/questions/3663665/how-can-i-get-the-current-screen-orientation
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            layoutManager = new GridLayoutManager(this, 4);
        }


        mainRv.setLayoutManager(layoutManager);
        mAdapter = new MovieAdapter(this, this);
        mFavoriteAdapter = new FavoriteMovieAdapter(this, this);

        if (!isOnline()) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_LONG).show();
        } else {
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
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
                    getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
                }
                return true;
            case R.id.menu_most_popular:
                sortOrder = MOST_POPULAR;
                if (!isOnline()) {
                    Toast.makeText(this, R.string.not_connected, Toast.LENGTH_LONG).show();
                } else {
                    getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
                }
                return true;
            case R.id.favorites:
                sortOrder = FAVORITES;
                getSupportLoaderManager().restartLoader(LOADER_ID, null, favoritesLoader);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        return new MovieAsyncTaskLoader(this, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        if (data == null) {
            Toast.makeText(this, R.string.returned_data_is_null, Toast.LENGTH_SHORT).show();
        } else {
            mAdapter.add(data);
            mainRv.setAdapter(mAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {


    }

    // from Stack Overflow:
    //    https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
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
