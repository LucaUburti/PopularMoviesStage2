package uby.luca.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    Movie movie;
    @BindView(R.id.title_tv) TextView titleTv;
    @BindView(R.id.description_tv) TextView descriptionTv;
    @BindView(R.id.poster_iv) ImageView posterIv;
    @BindView(R.id.average_vote_tv) TextView averageVoteTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            movie = bundle.getParcelable(MovieAdapter.PARCELED_MOVIE);
            if (movie != null) {
                titleTv.setText(movie.getTitle());
                descriptionTv.setText(movie.getPlot());

                averageVoteTv.setText(movie.getVoteAverage());

                Picasso.with(this)
                        .load(movie.getPoster())
                        .placeholder(R.drawable.loading_popcorn)
                        .error(R.drawable.img_not_found)
                        .into(posterIv);

            } else {
                Toast.makeText(this, R.string.error_movie_details, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.error_movie_details, Toast.LENGTH_SHORT).show();
        }
    }
}
