package com.example.shuffle.service;

import com.example.shuffle.models.spotify.Playlist;
import com.example.shuffle.models.spotify.Track;
import com.example.shuffle.models.spotify.query_result.TypedItemResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public interface SpotifyService {

    @GET("v1/me/top/tracks")
    Call<TypedItemResult<Track>> getTopMusicsOfPlayer(@Header("Authorization") String authorization, @Query("limit") Integer limit);

    @GET("v1/me/playlists")
    Call<TypedItemResult<Playlist>> getuserPlaylists(@Header("Authorization") String authorization);
}
