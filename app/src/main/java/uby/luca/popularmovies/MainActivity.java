package uby.luca.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    @BindView(R.id.main_rv)
    RecyclerView mainRv;
    static final int HIGHEST_RATED = 1;
    static final int MOST_POPULAR = 2;
    int sortOrder = MOST_POPULAR;//default sort order
    int LOADER_ID = 37;
    static final String QUERY_URL_KEY = "QUERY_URL_KEY";
    MovieAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mainRv.setLayoutManager(layoutManager);
        mAdapter = new MovieAdapter(this);

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Movie>>(this) {
            @Nullable
            @Override
            public ArrayList<Movie> loadInBackground() {
                URL url = NetworkUtils.buildURL(sortOrder, getString(R.string.APIKEY));

                String jsonResults = null;
                try {
                    jsonResults = NetworkUtils.readFromURL(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ArrayList<Movie> movieList = null;
                try {
                    movieList = NetworkUtils.parseJsonResults(jsonResults);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return movieList;
            }

            @Override
            protected void onStartLoading() {
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        if (data == null) {
            Toast.makeText(getParent().getApplicationContext(), "data è null!", Toast.LENGTH_SHORT).show();
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
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
