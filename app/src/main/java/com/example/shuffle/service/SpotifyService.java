package com.example.shuffle.service;

import com.example.shuffle.models.spotify.Playlist;
import com.example.shuffle.models.spotify.Track;
import com.example.shuffle.models.spotify.advanced_searchs.AudioFeatureResult;
import com.example.shuffle.models.spotify.query_result.TypedItemResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface SpotifyService {

    @GET("v1/me/top/tracks")
    Call<TypedItemResult<Track>> getTopMusicsOfPlayer(@Header("Authorization") String authorization, @Query("limit") Integer limit);

    @GET("v1/me/playlists")
    Call<TypedItemResult<Playlist>> getuserPlaylists(@Header("Authorization") String authorization);

    @POST("v1/me/playlists")
    Call<Void> createPlaylist(@Header("Authorization") String authorization, @Body Playlist playlist);

    @GET("v1/playlists/{id}?fields=tracks.items.track(uri)")
    Call<Playlist> getPlaylistTracks(@HeaderMap Map<String, String> headers, @Path("id") String playlistId);

    @HTTP(method = "DELETE", path = "v1/playlists/tracks", hasBody = true)
    Call<Void> deleteTracksByPlaylist(@HeaderMap Map<String, String> headers, @Body String tracksIds);

    @POST("v1/playlists/{id}/tracks")
    Call<Void> insertTracksIntoPlaylist(@Header("Authorization") String authorization, @Path("id") String playlistId, @Query("uris") String uris);

    @GET("v1/audio-features")
    Call<AudioFeatureResult> getAutdioFeatureByTracks(@Header("Authorization") String authorization, @Query("ids") String ids);
}
