package com.weavedin.itunesmusicplayer.db;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.weavedin.itunesmusicplayer.data.models.Result;

public class Converters {

    @TypeConverter
    public static Result fromString(String value) {
        return new Gson().fromJson(value, Result.class);
    }

    @TypeConverter
    public static String fromResult(Result result) {
        return new Gson().toJson(result);
    }

}