package com.weavedin.itunesmusicplayer.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.weavedin.itunesmusicplayer.data.models.Result;

import java.util.List;

@Dao
public interface FavouritesDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertFavouriteRecord(Result result);

    @Query("Select Count (*) FROM Result Where id = :result")
    int checkIfFavourite(Result result);

    @Delete
    void deleteFavouriteRecord(Result result);

    @Query("Select * from Result")
    LiveData<List<Result>> fetchAllFavourites();
}
