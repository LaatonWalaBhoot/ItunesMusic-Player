package com.weavedin.itunesmusicplayer;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainNavigator {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.favourites_btn)
    ImageView mFavouritesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        startFragment(new StartScreenFragment());
        mFavouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFragment(new FavouritesScreenFragment());
            }
        });
    }

    @Override
    public void initSearchScreen() {
        startFragment(new SearchScreenFragment());
    }

    @Override
    public void initFavouritesScreen() {
        startFragment(new FavouritesScreenFragment());
    }

    @Override
    public void initPlayerScreen() {
        startFragment(new PlayerScreenFragment());
    }

    private void startFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,fragment)
                .commit();
    }
}
