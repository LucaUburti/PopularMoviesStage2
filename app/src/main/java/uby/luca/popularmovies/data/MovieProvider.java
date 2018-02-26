package uby.luca.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static uby.luca.popularmovies.data.MovieContract.MovieEntry.TABLE_NAME;

/**
 * Created by uburti on 26/02/2018.
 */

public class MovieProvider extends ContentProvider {
    private MovieDbHelper movieDbHelper;

    private static final int CODE_MOVIES = 100;
    private static final int CODE_MOVIE_WITH_ID = 101;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, CODE_MOVIES);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", CODE_MOVIE_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());  //create underlying db
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase movieDb = movieDbHelper.getReadableDatabase();
        Cursor movieCursor;
        switch (uriMatcher.match(uri)) {
            case CODE_MOVIES:
                Log.d("MovieProvider", "Full DB query"+uri.toString());
                movieCursor = movieDb.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case CODE_MOVIE_WITH_ID:
                long rowId = ContentUris.parseId(uri);
                Log.d("MovieProvider", "single query: "+uri.toString());
                movieCursor=movieDb.query(TABLE_NAME, null, MovieContract.MovieEntry.COLUMN_MOVIEID+" = ?", new String[]{Long.toString(rowId)},null,null,null);
                break;

            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        if (movieCursor != null) {
            Log.d("MovieProvider", "Rows returned from query: "+movieCursor.getCount());
            movieCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return movieCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase movieDb = movieDbHelper.getWritableDatabase();
        Uri returnUri;
        switch (uriMatcher.match(uri)) {
            case CODE_MOVIES:
                long id = movieDb.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                    Log.d("MovieProvider", "inserted URI: " + returnUri.toString());
                } else {
                    throw new android.database.SQLException("Failed to insert data " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase movieDb = movieDbHelper.getWritableDatabase();
        if (uriMatcher.match(uri) != CODE_MOVIE_WITH_ID) {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Log.d("MovieProvider", "delete uri: " + uri.toString());
        long rowId = ContentUris.parseId(uri); //here the rowId is actually the movieId
        int affectedRows = movieDb.delete(TABLE_NAME, MovieContract.MovieEntry.COLUMN_MOVIEID+" = ?", new String[]{Long.toString(rowId)});
        Log.d("MovieProvider", "Rows deleted: " + Integer.toString(affectedRows));
        getContext().getContentResolver().notifyChange(uri, null);
        return affectedRows;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
