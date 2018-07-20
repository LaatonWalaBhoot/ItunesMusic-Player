package com.weavedin.itunesmusicplayer.ui.search;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.weavedin.itunesmusicplayer.MainNavigator;
import com.weavedin.itunesmusicplayer.MainViewModel;
import com.weavedin.itunesmusicplayer.R;
import com.weavedin.itunesmusicplayer.data.models.Result;
import com.weavedin.itunesmusicplayer.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TracksFragment extends Fragment implements TracksAdapter.OnItemClickListener {

    private TracksAdapter mTracksAdapter;
    private List<Result> mResults;
    private MainViewModel mMainViewModel;
    private MainNavigator mMainNavigator;
    private int position;
    private int children;

    @BindView(R.id.item_list)
    RecyclerView mTracksList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracks, container, false);
        ButterKnife.bind(this, view);
        mTracksList.setLayoutManager(new LinearLayoutManager(getContext()));
        ViewCompat.setNestedScrollingEnabled(mTracksList, false);
        mTracksAdapter = new TracksAdapter();
        mTracksAdapter.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() == null) {
            return;
        }
        mMainNavigator = (MainNavigator) getParentFragment().getContext();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() == null) {
            return;
        }
        if (getArguments() != null) {
            position = getArguments().getInt(Constants.POSITION);
            children = getArguments().getInt(Constants.CHILDREN);
        }
        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        mResults = mMainViewModel.getResults(position*children, (position+1)*children);
        mTracksAdapter.setList(mResults);
        mTracksList.setAdapter(mTracksAdapter);
    }

    @Override
    public void onItemClick(Result result) {
        mMainViewModel.setmCurrentResult(result);
        mMainNavigator.initPlayerScreen();
        Log.d("LFC", "TRACKS");
    }
}
