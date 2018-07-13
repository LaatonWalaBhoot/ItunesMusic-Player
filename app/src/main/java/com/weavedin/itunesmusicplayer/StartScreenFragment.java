package com.weavedin.itunesmusicplayer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartScreenFragment extends Fragment {

    @BindView(R.id.search_container)
    CardView mSearchContainer;

    private MainNavigator mMainNavigator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainNavigator = (MainNavigator)context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainNavigator.initSearchScreen();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if((getActivity())!=null) {
            if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
            }
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if((getActivity())!=null) {
            if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().show();
            }
        }
    }
}
