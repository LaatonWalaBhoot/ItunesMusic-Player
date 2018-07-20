package com.weavedin.itunesmusicplayer.ui.search;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weavedin.itunesmusicplayer.R;
import com.weavedin.itunesmusicplayer.data.models.Result;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TracksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private OnTrackClickListener onTrackClickListener;

    @BindView(R.id.artwork)
    ImageView mArtWork;

    @BindView(R.id.name)
    TextView mName;

    @BindView(R.id.artist)
    TextView mArtist;

    @BindView(R.id.track)
    CardView mCardTrack;

    public TracksViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
    }

    public void setResult(Result result, boolean highlighted) {
        Glide.with(itemView.getContext()).load(result.getArtworkUrl100()).into(mArtWork);
        mArtist.setText(result.getArtistName() + " | " + result.getCollectionName());
        mName.setText(result.getTrackName());
        if (highlighted) {
            mCardTrack.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
        } else {
            mCardTrack.setCardBackgroundColor(itemView.getContext().getResources().getColor(android.R.color.white));
        }
    }

    public void setOnTrackClickListener(OnTrackClickListener onTrackClickListener) {
        this.onTrackClickListener = onTrackClickListener;
    }

    @Override
    public void onClick(View v) {
        if (onTrackClickListener != null) {
            onTrackClickListener.onTrackClick(getAdapterPosition(), this);
        }
    }

    public interface OnTrackClickListener {
        void onTrackClick(int position, TracksViewHolder tracksViewHolder);
    }
}
