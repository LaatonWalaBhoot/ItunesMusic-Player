package com.weavedin.itunesmusicplayer.ui.player;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.weavedin.itunesmusicplayer.MainNavigator;
import com.weavedin.itunesmusicplayer.MainViewModel;
import com.weavedin.itunesmusicplayer.R;
import com.weavedin.itunesmusicplayer.utils.ToolbarUtils;
import com.weavedin.itunesmusicplayer.data.models.Result;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.joda.time.format.DateTimeFormat;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

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

    @BindView(R.id.list_btn)
    ImageView mListButton;

    @BindView(R.id.end_time)
    TextView mEndTime;

    public static final org.joda.time.format.DateTimeFormatter TIME_FORMATTER =
            DateTimeFormat.forPattern("mm:ss").withZoneUTC();

    private MainViewModel mMainViewModel;
    private Result mCurrentResult;
    private MainNavigator mMainNavigator;
    private static MediaPlayer instance;
    private boolean isFavourite = false;
    private final Handler mHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
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
        mCurrentResult = mMainViewModel.getmCurrentResult();
        initPlayer(mCurrentResult);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainNavigator = (MainNavigator) context;
    }

    private static MediaPlayer getInstance() {
        if (instance == null) {
            synchronized (MediaPlayer.class) {
                if (instance == null) {
                    instance = new MediaPlayer();
                }
            }

        }
        return instance;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mPlayButton.setImageResource(R.drawable.triangle);
        mSeekbar.setProgress(0);
    }

    private void initPlayer(final Result result) {
        if (getContext() == null) {
            return;
        }
        showProgress();
        Glide.with(getContext())
                .load(result.getArtworkUrl100().replace("100", "500"))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mArtWork);
        mSongName.setText(result.getTrackName());
        mArtistName.setText(result.getArtistName() + " | " + result.getCollectionName());
        try {
            getInstance().reset();
            getInstance().setDataSource(result.getPreviewUrl());
            getInstance().prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mEndTime.setText(TIME_FORMATTER.print(mCurrentResult.getTrackTimeMillis()));
        getInstance().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPlayButton.setImageResource(R.drawable.combined_shape_2);
                hideProgress();
                getInstance().start();
                updateProgress();
            }
        });
        mMainViewModel.getAllFavourites().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(@Nullable List<Result> results) {
                if(results!=null) {
                    isFavourite = false;
                    for (Result result1: results) {
                        if(result1.getTrackId().equals(mMainViewModel.getmCurrentResult().getTrackId())) {
                            isFavourite = true;
                        }
                    }
                    if(isFavourite) {
                        mFavouritesButton.setImageResource(R.drawable.shape_heart_red);
                    }
                    else {
                        mFavouritesButton.setImageResource(R.drawable.shape_heart);
                    }
                }
            }
        });
        mFavouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFavourite) {
                    mMainViewModel.deleteFavourite(result);
                }
                else {
                    mMainViewModel.insertFavourite(result);
                }
            }
        });
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getInstance().isPlaying()) {
                    mPlayButton.setImageResource(R.drawable.triangle);
                    getInstance().pause();
                } else {
                    mPlayButton.setImageResource(R.drawable.combined_shape_2);
                    getInstance().start();
                }
                updateProgress();
            }
        });
        mListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainNavigator.initListNavigation();
            }
        });
    }

    private void updateProgress() {
        // This math construction give a percentage of "was playing"/"song length"
        mSeekbar.setProgress((int) (((float) getInstance().getCurrentPosition() / getInstance().getDuration()) * 100));
        if (getInstance().isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    updateProgress();
                }
            };
            mHandler.postDelayed(notification, 500);
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
}
