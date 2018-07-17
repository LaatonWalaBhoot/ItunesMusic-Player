package com.weavedin.itunesmusicplayer.ui.player;

import android.arch.lifecycle.ViewModelProviders;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weavedin.itunesmusicplayer.MainViewModel;
import com.weavedin.itunesmusicplayer.R;
import com.weavedin.itunesmusicplayer.utils.ToolbarUtils;
import com.weavedin.itunesmusicplayer.data.models.Result;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerScreenFragment extends Fragment implements MediaPlayer.OnCompletionListener {

    @BindView(R.id.artwork)
    ImageView mArtWork;

    @BindView(R.id.seekbar)
    DiscreteSeekBar mSeekbar;

    @BindView(R.id.song_name)
    TextView mSongName;

    @BindView(R.id.artist_name)
    TextView mArtistName;

    @BindView(R.id.play_btn)
    FloatingActionButton mPlayButton;

    @BindView(R.id.prepare_progress)
    ProgressBar mProgressBar;

    @BindView(R.id.favourites_btn)
    ImageView mFavouritesButton;

    private MainViewModel mMainViewModel;
    private Result mCurrentResult;
    private MediaPlayer mMediaPlayer;
    private final Handler mHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player,container,false);
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
        mCurrentResult = mMainViewModel.getmCurrentResult();
        initPlayer(mCurrentResult);
    }

    private void initPlayer(final Result result) {
        if(getContext()==null) {
            return;
        }
        showProgress();
        Glide.with(getContext()).load(result.getArtworkUrl100()).into(mArtWork);
        mSongName.setText(result.getTrackName());
        mArtistName.setText(result.getArtistName()+ " | "+ result.getCollectionName());
        if(mMainViewModel.isFavourite()) {
            mFavouritesButton.setImageResource(R.drawable.shape_heart_red);
        }
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(result.getPreviewUrl());
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPlayButton.setImageResource(R.drawable.combined_shape_2);
                hideProgress();
                mMediaPlayer.start();
                updateProgress();
            }
        });
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMediaPlayer.isPlaying()) {
                    mPlayButton.setImageResource(R.drawable.triangle);
                    mMediaPlayer.pause();
                }
                else {
                    mPlayButton.setImageResource(R.drawable.combined_shape_2);
                    mMediaPlayer.start();
                }
                updateProgress();
            }
        });
        mFavouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMainViewModel.isFavourite()) {
                    mFavouritesButton.setImageResource(R.drawable.shape_heart);
                    mMainViewModel.deleteFavourite(result);
                }
                else {
                    mFavouritesButton.setImageResource(R.drawable.shape_heart_red);
                    mMainViewModel.insertFavourite(result);
                }
            }
        });
    }

    private void updateProgress() {
        mSeekbar.setProgress((int)(((float)mMediaPlayer.getCurrentPosition()/mMediaPlayer.getDuration())*100)); // This math construction give a percentage of "was playing"/"song length"
        if (mMediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    updateProgress();
                }
            };
            mHandler.postDelayed(notification,500);
        }
    }

    private void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        mPlayButton.setVisibility(View.GONE);
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        mPlayButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        ToolbarUtils.hideSearchOption(getActivity());
        ToolbarUtils.showBackButton(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        mMediaPlayer.pause();
        ToolbarUtils.showSearchOption(getActivity());
        ToolbarUtils.hideBackButton(getActivity());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mPlayButton.setImageResource(R.drawable.triangle);
        mSeekbar.setProgress(0);
    }
}
