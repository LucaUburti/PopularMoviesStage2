package uby.luca.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent=getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            String t=intent.getStringExtra(Intent.EXTRA_TEXT);
            Toast.makeText(this, "Movie clicked: "+t, Toast.LENGTH_LONG).show();
        }

    }
}
