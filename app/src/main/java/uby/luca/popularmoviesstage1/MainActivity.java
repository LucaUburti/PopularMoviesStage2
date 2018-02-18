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
        mAdapter.add("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg");
        mAdapter.add("http://image.tmdb.org/t/p/w185//u351Rsqu5nd36ZpbWxIpd3CpbJW.jpg");
        mAdapter.add("http://image.tmdb.org/t/p/w185//itdfycoMpjGWiGdjLUKMdAe9oQ5.jpg");
        mAdapter.add("http://image.tmdb.org/t/p/w185//wVQch5MdY87uKwsoIyBLRQt7oND.jpg");
        mAdapter.add("http://image.tmdb.org/t/p/w185//6s5SuHiUCfx7KYCqzlnem82tpYn.jpg");
        mAdapter.add("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg");
        mAdapter.add("http://image.tmdb.org/t/p/w185//u351Rsqu5nd36ZpbWxIpd3CpbJW.jpg");
        mAdapter.add("http://image.tmdb.org/t/p/w185//itdfycoMpjGWiGdjLUKMdAe9oQ5.jpg");
        mAdapter.add("http://image.tmdb.org/t/p/w185//wVQch5MdY87uKwsoIyBLRQt7oND.jpg");
        mAdapter.add("http://image.tmdb.org/t/p/w185//6s5SuHiUCfx7KYCqzlnem82tpYn.jpg");
        mainRv.setAdapter(mAdapter);


    }
}
