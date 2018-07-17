package com.weavedin.itunesmusicplayer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.weavedin.itunesmusicplayer.data.remote.ApiService;
//import com.weavedin.itunesmusicplayer.di.modules.component.DaggerMainActivityComponent;
import com.weavedin.itunesmusicplayer.db.PlayerDb;
import com.weavedin.itunesmusicplayer.db.entity.Search;
import com.weavedin.itunesmusicplayer.di.modules.component.DaggerMainActivityComponent;
import com.weavedin.itunesmusicplayer.di.modules.component.MainActivityComponent;
import com.weavedin.itunesmusicplayer.ui.favourites.FavouritesScreenFragment;
import com.weavedin.itunesmusicplayer.ui.player.PlayerScreenFragment;
import com.weavedin.itunesmusicplayer.ui.search.SearchScreenFragment;
import com.weavedin.itunesmusicplayer.ui.start.StartScreenFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements MainNavigator {

    @BindView(R.id.search_btn)
    ImageView mSearchButton;

    @BindView(R.id.search_bar)
    AutoCompleteTextView mSearchBar;

    @BindView(R.id.favourites_btn)
    ImageView mFavouritesButton;

    @Inject
    Retrofit retrofit;

    private MainActivityComponent component;
    private MainViewModel mMainViewModel;
    private PlayerDb mRoomDatabase;
    private LiveData<List<Search>> liveData;
    private List<String> searches = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mRoomDatabase = Room.databaseBuilder(this, PlayerDb.class,"MPLayer_db").build();
        mMainViewModel.setmRoomDatabase(mRoomDatabase);
        component = DaggerMainActivityComponent.builder().build();
        component.injectMainActivity(this);
        startFragment(new StartScreenFragment());
        liveData = mMainViewModel.getAllSearches();
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.drop_down,searches);
        mSearchBar.setAdapter(arrayAdapter);
        liveData.observe(this, new Observer<List<Search>>() {
            @Override
            public void onChanged(@Nullable List<Search> searches) {
                for(Search search: searches) {
                    MainActivity.this.searches.add(search.query);
                }
                arrayAdapter.notifyDataSetChanged();
            }
        });
        mFavouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFragment(new FavouritesScreenFragment());
            }
        });
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(mSearchBar.getText().toString())) {
                    mMainViewModel.initCall(retrofit.create(ApiService.class)
                            ,mSearchBar.getText().toString());
                    mMainViewModel.insertSearch(mSearchBar.getText().toString());
                    Toast.makeText(MainActivity.this, ""+searches.size(), Toast.LENGTH_SHORT).show();
                }
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
                .replace(R.id.container, fragment)
                .commitNow();
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()>0) {
            getSupportFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }
}
