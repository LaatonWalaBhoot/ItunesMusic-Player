package com.weavedin.itunesmusicplayer.ui.favourites;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weavedin.itunesmusicplayer.MainViewModel;
import com.weavedin.itunesmusicplayer.R;
import com.weavedin.itunesmusicplayer.utils.ToolbarUtils;
import com.weavedin.itunesmusicplayer.ui.search.TracksAdapter;
import com.weavedin.itunesmusicplayer.data.models.Result;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouritesScreenFragment extends Fragment {


    private TracksAdapter mTracksAdapter;
    private LiveData<List<Result>> mFavourites;
    private MainViewModel mMainViewModel;

    @BindView(R.id.item_list)
    RecyclerView mTracksList;

    @BindView(R.id.header)
    TextView mHeader;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getActivity()==null) {
            return;
        }
        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        mTracksList.setLayoutManager(new LinearLayoutManager(getContext()));
        mTracksAdapter = new TracksAdapter();
        mFavourites = mMainViewModel.getAllFavourites();
        mFavourites.observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(@Nullable List<Result> results) {
                mTracksAdapter.setList(results);
                mHeader.setText("Favourites - " + String.valueOf(results.size()) );
            }
        });
        mTracksList.setAdapter(mTracksAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ToolbarUtils.hideFavouritesButton(getActivity());
        ToolbarUtils.hideSearchOption(getActivity());
        ToolbarUtils.showBackButton(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        ToolbarUtils.showFavouritesButton(getActivity());
        ToolbarUtils.showSearchOption(getActivity());
        ToolbarUtils.hideBackButton(getActivity());
    }
}
