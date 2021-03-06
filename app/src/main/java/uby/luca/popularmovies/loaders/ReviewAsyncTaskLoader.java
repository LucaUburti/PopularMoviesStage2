package uby.luca.popularmovies.loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import uby.luca.popularmovies.BuildConfig;
import uby.luca.popularmovies.POJOs.Review;
import uby.luca.popularmovies.utils.NetworkUtils;

/**
 * Created by uburti on 25/02/2018.
 */

public class ReviewAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Review>> {

    private String movieId;

    public ReviewAsyncTaskLoader(@NonNull Context context, String movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Nullable
    @Override
    public ArrayList<Review> loadInBackground() {
        URL url = NetworkUtils.buildReviewURL(BuildConfig.API_KEY, movieId);

        String jsonResults = null;
        try {
            jsonResults = NetworkUtils.readFromURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (jsonResults == null) {
            return null;
        }
        ArrayList<Review> reviewList = null;
        try {
            reviewList = NetworkUtils.parseReviewJsonResults(jsonResults);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviewList;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}

