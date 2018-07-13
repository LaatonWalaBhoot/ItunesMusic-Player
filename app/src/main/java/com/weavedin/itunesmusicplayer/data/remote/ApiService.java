package com.weavedin.itunesmusicplayer.data.remote;

import com.weavedin.itunesmusicplayer.data.models.SearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/search")
    Call<SearchResult> getTracks(@Query("term") String query, @Query("limit") int limit);
}
