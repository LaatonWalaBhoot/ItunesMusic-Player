package com.weavedin.itunesmusicplayer.ui.search;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weavedin.itunesmusicplayer.MainViewModel;
import com.weavedin.itunesmusicplayer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchScreenFragment extends Fragment implements
        MainViewModel.OnResultReceivedListener {

    @BindView(R.id.tracks_container)
    ViewPager mTracksContainer;

    @BindView(R.id.header)
    TextView mSongCount;

    private MainViewModel mMainViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
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
        mMainViewModel.setResultReceivedListener(this);
    }

    @Override
    public void onResultSuccess() {
        mSongCount.setText(String.valueOf("All Songs - ")
                .concat(String.valueOf(mMainViewModel.getResultCount())));
        mTracksContainer.setAdapter(new ViewPagerAdapter(getChildFragmentManager(),1));
    }

    @Override
    public void onResultFailure(String message) {

    }
}
