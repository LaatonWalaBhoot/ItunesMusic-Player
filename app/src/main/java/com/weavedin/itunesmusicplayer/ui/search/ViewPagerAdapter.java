package com.weavedin.itunesmusicplayer.ui.search;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.weavedin.itunesmusicplayer.ui.search.TracksFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter{

    private int count;
    private FragmentManager fragmentManager;

    public ViewPagerAdapter(FragmentManager fm, int count) {
        super(fm);
        this.count = count;
        this.fragmentManager = fm;
    }

    @Override
    public int getCount() {
        return count;
    }


    @Override
    public Fragment getItem(int position) {
        return new TracksFragment();
    }
}
