package com.weavedin.itunesmusicplayer.ui.search;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.weavedin.itunesmusicplayer.MainViewModel;
import com.weavedin.itunesmusicplayer.R;
import com.weavedin.itunesmusicplayer.utils.ToolbarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchScreenFragment extends Fragment implements
        MainViewModel.OnResultReceivedListener {

    @BindView(R.id.tracks_container)
    ViewPager mTracksContainer;

    @BindView(R.id.header)
    TextView mSongCount;

    @BindView(R.id.placeholder)
    TextView mPlaceholder;

    @BindView(R.id.load_progress)
    ProgressBar mProgress;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    private MainViewModel mMainViewModel;
    private int mChildren;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() == null) {
            return;
        }
        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        mMainViewModel.setResultReceivedListener(this);
        mTracksContainer.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
            @Override
            public void onDraw() {
                mChildren = mTracksContainer.getMeasuredHeight() / dpToPx(80) - 1;
            }
        });
        mTabLayout.setupWithViewPager(mTracksContainer, true);
    }

    @Override
    public void onResultRequest() {
        showProgress();
    }

    @Override
    public void onResultSuccess() {
        hideProgress();
        mSongCount.setText(String.valueOf("All Songs - ")
                .concat(String.valueOf(mMainViewModel.getResultCount())));
        mTracksContainer.setAdapter(new ViewPagerAdapter(getChildFragmentManager(),
                mMainViewModel.getResultCount() / mChildren, mChildren));
    }

    @Override
    public void onResultFailure(String message) {
        hideProgress();
        mTracksContainer.setVisibility(View.GONE);
        mPlaceholder.setVisibility(View.VISIBLE);
        mPlaceholder.setTextSize(10);
        mPlaceholder.setText(message);
    }

    private void showProgress() {
        mTracksContainer.setVisibility(View.GONE);
        mPlaceholder.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        mTracksContainer.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
        mPlaceholder.setVisibility(View.GONE);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
