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
 * Created by uburti on 25/02/2018.
 */

public class TrailerAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Trailer>> {

    String movieId;

    public TrailerAsyncTaskLoader(@NonNull Context context, String movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Nullable
    @Override
    public ArrayList<Trailer> loadInBackground() {
        URL url = NetworkUtils.buildTrailerURL(BuildConfig.API_KEY, movieId);

        String jsonResults = null;
        try {
            jsonResults = NetworkUtils.readFromURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (jsonResults == null) {
            return null;
        }
        ArrayList<Trailer> trailerList = null;
        try {
            trailerList = NetworkUtils.parseTrailerJsonResults(jsonResults);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailerList;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}

