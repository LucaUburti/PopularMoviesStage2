package uby.luca.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by uburti on 21/02/2018.
 */

class MovieAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Movie>> {
    private int sortOrder;

    MovieAsyncTaskLoader(@NonNull Context context, int sortOrder) {
        super(context);
        this.sortOrder = sortOrder;
    }

    @Nullable
    @Override
    public ArrayList<Movie> loadInBackground() {
        URL url = NetworkUtils.buildURL(sortOrder, BuildConfig.API_KEY);

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
}

