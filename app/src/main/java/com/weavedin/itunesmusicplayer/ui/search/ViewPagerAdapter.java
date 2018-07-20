package com.weavedin.itunesmusicplayer.ui.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.weavedin.itunesmusicplayer.MainViewModel;
import com.weavedin.itunesmusicplayer.ui.search.TracksFragment;
import com.weavedin.itunesmusicplayer.utils.Constants;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private int count;
    private int children;
    private FragmentManager fragmentManager;
    private TracksFragment tracksFragment;

    public ViewPagerAdapter(FragmentManager fm, int count, int children) {
        super(fm);
        this.fragmentManager = fm;
        this.children = children;
        this.count = count;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.POSITION, position);
        bundle.putInt(Constants.CHILDREN, children);
        tracksFragment = new TracksFragment();
        tracksFragment.setArguments(bundle);
        return tracksFragment;
    }
}
