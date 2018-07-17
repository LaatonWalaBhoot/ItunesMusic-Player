package com.weavedin.itunesmusicplayer.di.modules.component;

import com.weavedin.itunesmusicplayer.MainActivity;
import com.weavedin.itunesmusicplayer.di.modules.RetrofitModule;
import com.weavedin.itunesmusicplayer.di.modules.scope.PerActivityScope;

import dagger.Component;

@Component (modules = RetrofitModule.class)
@PerActivityScope
public interface MainActivityComponent {

    void injectMainActivity(MainActivity mainActivity);
}
