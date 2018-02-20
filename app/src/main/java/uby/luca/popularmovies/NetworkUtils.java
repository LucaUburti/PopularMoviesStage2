package uby.luca.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by uburti on 19/02/2018.
 */

public class NetworkUtils {
    private static final String THE_MOVIE_DATABASE_URL = "https://api.themoviedb.org/3/discover/movie";


    public static URL buildURL(int sortOrder, String apiKey) {
        String PARAM_API_KEY = "api_key";
        String PARAM_SORT_BY = "sort_by";

        String sortValue = "";
        if (sortOrder == MainActivity.HIGHEST_RATED) {
            sortValue = "vote_average.desc";
        }
        if (sortOrder == MainActivity.MOST_POPULAR) {
            sortValue = "popularity.desc";
        }

        Uri builtUri = Uri.parse(THE_MOVIE_DATABASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .appendQueryParameter(PARAM_SORT_BY, sortValue)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String readFromURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static ArrayList<Movie> parseJsonResults(String jsonResults) throws JSONException {
        final String JSON_RESULTS_KEY = "results";
        final String JSON_TITLE_KEY = "title";
        final String JSON_POSTER_KEY = "poster_path";
        final String JSON_VOTEAVERAGE_KEY = "vote_average";
        final String JSON_PLOT_KEY = "overview";
        final String JSON_DATE_KEY = "release_date";
        final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";


        ArrayList<Movie> movieList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonResults);
        JSONArray jsonMovies = jsonObject.optJSONArray(JSON_RESULTS_KEY);
        for (int i = 0; i < jsonMovies.length(); i++) {
            JSONObject jsonCurrentMovie = jsonMovies.getJSONObject(i);

            String title = jsonCurrentMovie.optString(JSON_TITLE_KEY);
            String poster = POSTER_BASE_URL+ jsonCurrentMovie.optString(JSON_POSTER_KEY);
            String voteAverage = jsonCurrentMovie.optString(JSON_VOTEAVERAGE_KEY);
            String plot = jsonCurrentMovie.optString(JSON_PLOT_KEY);
            String releaseDate=jsonCurrentMovie.optString(JSON_DATE_KEY);
            Movie currentMovie = new Movie(title, poster, voteAverage, plot, releaseDate);
            movieList.add(currentMovie);
        }

        return movieList;

    }
}
