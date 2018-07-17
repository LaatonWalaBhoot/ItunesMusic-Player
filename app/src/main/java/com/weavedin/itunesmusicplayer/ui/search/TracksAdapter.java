package com.weavedin.itunesmusicplayer.ui.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weavedin.itunesmusicplayer.R;
import com.weavedin.itunesmusicplayer.data.models.Result;

import java.util.List;

public class TracksAdapter extends RecyclerView.Adapter<TracksViewHolder> implements TracksViewHolder.OnTrackClickListener {

    private List<Result> mResults;
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public TracksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_track,parent,false);
        return new TracksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TracksViewHolder holder, int position) {
        holder.setResult(mResults.get(position));
        holder.setOnTrackClickListener(this);
    }

    @Override
    public int getItemCount() {
        if(mResults!=null) {
            return mResults.size();
        }
        else {
            return 0;
        }
    }

    @Override
    public void onTrackClick(int position, TracksViewHolder tracksViewHolder) {
        if(onItemClickListener!=null) {
            onItemClickListener.onItemClick(mResults.get(position));
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setList(List<Result> mResults) {
        this.mResults = mResults;
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        void onItemClick(Result result);
    }
}
