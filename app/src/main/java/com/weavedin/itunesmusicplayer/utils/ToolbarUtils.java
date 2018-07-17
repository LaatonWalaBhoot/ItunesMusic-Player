package com.weavedin.itunesmusicplayer.utils;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.weavedin.itunesmusicplayer.R;

public class ToolbarUtils {

    public static void hideToolbar(Activity activity) {
        if((activity)!=null) {
            ((AppCompatActivity)activity).findViewById(R.id.toolbar)
                    .setVisibility(View.GONE);
        }
    }

    public static void showToolbar(Activity activity) {
        if((activity)!=null) {
            ((AppCompatActivity)activity).findViewById(R.id.toolbar)
                    .setVisibility(View.VISIBLE);
        }
    }

    public static void hideSearchOption(Activity activity) {
        if((activity)!=null) {
            ((AppCompatActivity)activity)
                    .findViewById(R.id.toolbar)
                    .findViewById(R.id.search_container)
                    .setVisibility(View.GONE);
        }
    }

    public static void showSearchOption(Activity activity) {
        if((activity)!=null) {
            ((AppCompatActivity)activity)
                    .findViewById(R.id.toolbar)
                    .findViewById(R.id.search_container)
                    .setVisibility(View.VISIBLE);
        }
    }

    public static void showBackButton(Activity activity) {
        if((activity)!=null) {
            ImageView img = (ImageView)((AppCompatActivity)activity)
                    .findViewById(R.id.toolbar)
                    .findViewById(R.id.navigation_btn);
            img.setImageResource(R.drawable.arrow_back);
        }
    }

    public static void hideBackButton(Activity activity) {
        if((activity)!=null) {
            ImageView img = (ImageView)((AppCompatActivity)activity)
                    .findViewById(R.id.toolbar)
                    .findViewById(R.id.navigation_btn);
            img.setImageResource(R.drawable.shape);
        }
    }

    public static void showFavouritesButton(Activity activity) {
        if((activity)!=null) {
            ((AppCompatActivity)activity).findViewById(R.id.toolbar)
                    .findViewById(R.id.favourites_btn)
                    .setVisibility(View.VISIBLE);
        }
    }

    public static void hideFavouritesButton(Activity activity) {
        if((activity)!=null) {
            ((AppCompatActivity)activity).findViewById(R.id.toolbar)
                    .findViewById(R.id.favourites_btn)
                    .setVisibility(View.GONE);
        }
    }
}
