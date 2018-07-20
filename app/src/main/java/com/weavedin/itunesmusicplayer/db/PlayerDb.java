package com.weavedin.itunesmusicplayer.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.weavedin.itunesmusicplayer.data.models.Result;
import com.weavedin.itunesmusicplayer.db.dao.FavouritesDao;
import com.weavedin.itunesmusicplayer.db.dao.SearchDao;
import com.weavedin.itunesmusicplayer.db.entity.Search;


@Database(entities = {Result.class, Search.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class PlayerDb extends RoomDatabase {
    public abstract FavouritesDao favouritesDao();

    public abstract SearchDao searchDao();
}