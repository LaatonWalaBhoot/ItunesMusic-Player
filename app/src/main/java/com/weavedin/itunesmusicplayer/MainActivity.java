package com.weavedin.itunesmusicplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainNavigator {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,new StartScreenFragment())
                .commit();
    }

    @Override
    public void initSearchScreen() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,new SearchScreenFragment())
                .commit();
    }

    @Override
    public void initFavouritesScreen() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,new FavouritesScreenFragment())
                .commit();
    }

    @Override
    public void initPlayerScreen() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,new PlayerScreenFragment())
                .commit();
    }
}
