package uby.luca.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            movie = bundle.getParcelable(MovieAdapter.PARCELED_MOVIE);
            if (movie != null) {
                Toast.makeText(this, movie.getTitle() + movie.getPoster() + movie.getVoteAverage() + movie.getPlot(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.error_movie_details, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.error_movie_details, Toast.LENGTH_SHORT).show();
        }
    }
}
