package uby.luca.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by uburti on 26/02/2018.
 */

class MovieDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movielist.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_MOVIELIST_TABLE = "CREATE TABLE " +
                MovieContract.MovieEntry.TABLE_NAME +               " (" +
                MovieContract.MovieEntry._ID +                      " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieEntry.COLUMN_MOVIEID +           " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_PLOT +              " TEXT, " +
                MovieContract.MovieEntry.COLUMN_POSTER +            " TEXT, " +
                MovieContract.MovieEntry.COLUMN_RELEASEDATE +       " TEXT, " +
                MovieContract.MovieEntry.COLUMN_TITLE +             " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_VOTEAVERAGE +       " TEXT" +
                ");";
        Log.d("MovieDbHelper", "Table creation SQL string: "+SQL_CREATE_MOVIELIST_TABLE);
        db.execSQL(SQL_CREATE_MOVIELIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ MovieContract.MovieEntry.TABLE_NAME);
    }
}
