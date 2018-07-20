package com.weavedin.itunesmusicplayer.utils;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.weavedin.itunesmusicplayer.R;

public class ToolbarUtils {

    private static boolean hasBackButtonVisible = false;

    public static void hideToolbar(Activity activity) {
        if ((activity) != null) {
            activity.findViewById(R.id.toolbar)
                    .setVisibility(View.GONE);
        }
    }

    public static void showToolbar(Activity activity) {
        if ((activity) != null) {
            activity.findViewById(R.id.toolbar)
                    .setVisibility(View.VISIBLE);
        }
    }

    public static void hideSearchOption(Activity activity) {
        if ((activity) != null) {
            activity.findViewById(R.id.toolbar)
                    .findViewById(R.id.search_container)
                    .setVisibility(View.GONE);
        }
    }

    public static void showSearchOption(Activity activity) {
        if ((activity) != null) {
            activity.findViewById(R.id.toolbar)
                    .findViewById(R.id.search_container)
                    .setVisibility(View.VISIBLE);
        }
    }

    public static void showBackButton(Activity activity) {
        if ((activity) != null) {
            ImageView img = (ImageView) activity
                    .findViewById(R.id.toolbar)
                    .findViewById(R.id.navigation_btn);
            img.setImageResource(R.drawable.arrow_back);
            hasBackButtonVisible = true;
        }
    }

    public static void hideBackButton(Activity activity) {
        if ((activity) != null) {
            ImageView img = (ImageView) activity
                    .findViewById(R.id.toolbar)
                    .findViewById(R.id.navigation_btn);
            img.setImageResource(R.drawable.shape);
            hasBackButtonVisible = false;
        }
    }

    public static void showFavouritesButton(Activity activity) {
        if ((activity) != null) {
            activity.findViewById(R.id.toolbar)
                    .findViewById(R.id.favourites_btn)
                    .setVisibility(View.VISIBLE);
        }
    }

    public static void hideFavouritesButton(Activity activity) {
        if ((activity) != null) {
            activity.findViewById(R.id.toolbar)
                    .findViewById(R.id.favourites_btn)
                    .setVisibility(View.GONE);
        }
    }

    public static boolean hasBackButtonVisible() {
        return hasBackButtonVisible;
    }
}
