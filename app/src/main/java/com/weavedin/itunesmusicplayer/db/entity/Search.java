package com.weavedin.itunesmusicplayer.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = {@Index(value = "query", unique = true)})
public class Search {

    public Search(String query) {
        this.query = query;
    }

    @PrimaryKey(autoGenerate = true)
    public long id = 0;
    public String query;
}
