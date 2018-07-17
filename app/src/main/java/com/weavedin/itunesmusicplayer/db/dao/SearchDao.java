package com.weavedin.itunesmusicplayer.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.weavedin.itunesmusicplayer.db.entity.Search;

import java.util.List;

@Dao
public interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertSearchRecord(Search search);

    @Query("Select * from Search")
    LiveData<List<Search>> fetchAllQueries();
}
