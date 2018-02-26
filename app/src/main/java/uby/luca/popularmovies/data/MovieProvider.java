package uby.luca.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import static uby.luca.popularmovies.data.MovieContract.MovieEntry.TABLE_NAME;

/**
 * Created by uburti on 26/02/2018.
 */

public class MovieProvider extends ContentProvider {
    private MovieDbHelper movieDbHelper;

    public static final int CODE_MOVIES = 100;
    public static final int CODE_MOVIE_WITH_ID = 101;
    public static final UriMatcher uriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
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
                Log.d("CP", "Full DB query");
                movieCursor = movieDb.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

                movieCursor.moveToFirst();
                while (!movieCursor.isAfterLast()) {
                    Log.d("CP", "query: " + DatabaseUtils.dumpCurrentRowToString(movieCursor));
                    movieCursor.moveToNext();
                }
                break;
            case CODE_MOVIE_WITH_ID:

                long rowId = ContentUris.parseId(uri);
                Log.d("CP", "single query: "+uri.toString());
                movieCursor=movieDb.query(TABLE_NAME, null, MovieContract.MovieEntry.COLUMN_MOVIEID+" = ?", new String[]{Long.toString(rowId)},null,null,null);


                break;

            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        Log.d("CP", "end of query: ");
        if (movieCursor != null) {
            Log.d("CP", "Cursor isn't null, returning data!");
            movieCursor.moveToFirst();
            while (!movieCursor.isAfterLast())  {
                Log.d("CP", "Cursor data: "+DatabaseUtils.dumpCurrentRowToString(movieCursor));
                movieCursor.moveToNext();
            }
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
                    Log.d("CP", "insert: " + values.toString());
                    Log.d("CP", "insertion URI: " + returnUri.toString());
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
        long rowId = ContentUris.parseId(uri);
        int affectedRows = movieDb.delete(TABLE_NAME, "_id=?", new String[]{Float.toString(rowId)});
        getContext().getContentResolver().notifyChange(uri, null);
        return affectedRows;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //TODO some Log.d cleanup...
}
