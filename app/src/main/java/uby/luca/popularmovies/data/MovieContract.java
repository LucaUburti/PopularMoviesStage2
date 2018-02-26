package uby.luca.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by uburti on 26/02/2018.
 */

public final class MovieContract {
    public static final String AUTHORITY = "uby.luca.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movielist";
        public static final String COLUMN_MOVIEID = "movieId";
        public static final String COLUMN_PLOT = "plot";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_RELEASEDATE = "releaseDate";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_VOTEAVERAGE = "voteAverage";
    }


}


