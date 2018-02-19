package uby.luca.popularmoviesstage1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_rv)
    RecyclerView mainRv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mainRv.setLayoutManager(layoutManager);

        MovieAdapter mAdapter = new MovieAdapter(this);

        //loadJsonFromTMDB
        Movie movie1=new Movie("Film Uno","http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","voto 10", "Descrizione di un film molto bello" );
        Movie movie2=new Movie("Film Due","http://image.tmdb.org/t/p/w185//u351Rsqu5nd36ZpbWxIpd3CpbJW.jpg","voto 0", "Descrizione di un film molto brutto" );


        mAdapter.add(movie1);
        mAdapter.add(movie2);

        mainRv.setAdapter(mAdapter);


    }
}
