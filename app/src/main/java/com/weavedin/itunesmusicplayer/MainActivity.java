package com.weavedin.itunesmusicplayer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.weavedin.itunesmusicplayer.data.remote.ApiService;
import com.weavedin.itunesmusicplayer.db.PlayerDb;
import com.weavedin.itunesmusicplayer.db.entity.Search;
import com.weavedin.itunesmusicplayer.di.components.DaggerMainActivityComponent;
import com.weavedin.itunesmusicplayer.di.components.MainActivityComponent;
import com.weavedin.itunesmusicplayer.ui.favourites.FavouritesScreenFragment;
import com.weavedin.itunesmusicplayer.ui.player.PlayerScreenFragment;
import com.weavedin.itunesmusicplayer.ui.search.SearchScreenFragment;
import com.weavedin.itunesmusicplayer.ui.start.StartScreenFragment;
import com.weavedin.itunesmusicplayer.utils.ToolbarUtils;

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

    @BindView(R.id.navigation_btn)
    ImageView mNavigationButton;

    @Inject
    Retrofit retrofit;

    private static final String START_SCREEN = StartScreenFragment.class.getSimpleName();
    private static final String SEARCH_SCREEN = SearchScreenFragment.class.getSimpleName();
    private static final String PLAYER_SCREEN = PlayerScreenFragment.class.getSimpleName();
    private static final String FAVOURITES_SCREEN = FavouritesScreenFragment.class.getSimpleName();


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
        mRoomDatabase = Room.databaseBuilder(this, PlayerDb.class, "MPLayer_db").build();
        mMainViewModel.setmRoomDatabase(mRoomDatabase);
        component = DaggerMainActivityComponent.builder().build();
        component.injectMainActivity(this);
        replaceFragment(new StartScreenFragment());
        liveData = mMainViewModel.getAllSearches();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.drop_down, searches);
        mSearchBar.setAdapter(arrayAdapter);
        liveData.observe(this, new Observer<List<Search>>() {
            @Override
            public void onChanged(@Nullable List<Search> searches) {
                if (searches != null) {
                    for (Search search : searches) {
                        MainActivity.this.searches.add(search.query);
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            }
        });
        mFavouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initFavouritesScreen();
            }
        });
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
        mSearchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
        mNavigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ToolbarUtils.hasBackButtonVisible()) {
                    onBackPressed();
                }
            }
        });
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                setToolbar();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToolbar();
    }

    @Override
    public void initSearchScreen() {
        addFragment(new SearchScreenFragment());
    }

    @Override
    public void initFavouritesScreen() {
        addFragment(new FavouritesScreenFragment());
    }

    @Override
    public void initPlayerScreen() {
        addFragment(new PlayerScreenFragment());
    }

    @Override
    public void initListNavigation() {
        getSupportFragmentManager().popBackStackImmediate();
    }

    private void replaceFragment(final Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commitNow();
    }

    private void addFragment(final Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(fragment.getClass().getSimpleName())
                .add(R.id.container, fragment)
                .commit();
    }

    private void performSearch() {
        if (!TextUtils.isEmpty(mSearchBar.getText().toString())) {
            mMainViewModel.initCall(retrofit.create(ApiService.class)
                    , mSearchBar.getText().toString());
            mMainViewModel.insertSearch(mSearchBar.getText().toString());
            hideKeyboard();
        }
    }

    private void hideKeyboard() {
        mSearchBar.clearFocus();
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (in != null) {
            in.hideSoftInputFromWindow(mSearchBar.getWindowToken(), 0);
        }
    }

    private void setToolbar() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
        if (f != null) {
            if (f.getClass().getSimpleName().equals(START_SCREEN)) {
                ToolbarUtils.hideToolbar(MainActivity.this);
            } else if (f.getClass().getSimpleName().equals(SEARCH_SCREEN)) {
                ToolbarUtils.showToolbar(MainActivity.this);
                ToolbarUtils.showSearchOption(MainActivity.this);
                ToolbarUtils.showFavouritesButton(MainActivity.this);
                ToolbarUtils.hideBackButton(MainActivity.this);
            } else if (f.getClass().getSimpleName().equals(PLAYER_SCREEN)) {
                ToolbarUtils.showToolbar(MainActivity.this);
                ToolbarUtils.showBackButton(MainActivity.this);
                ToolbarUtils.hideSearchOption(MainActivity.this);
                ToolbarUtils.showFavouritesButton(MainActivity.this);
            } else if (f.getClass().getSimpleName().equals(FAVOURITES_SCREEN)) {
                ToolbarUtils.showToolbar(MainActivity.this);
                ToolbarUtils.showBackButton(MainActivity.this);
                ToolbarUtils.hideSearchOption(MainActivity.this);
                ToolbarUtils.hideFavouritesButton(MainActivity.this);
            }
        }
    }
}
