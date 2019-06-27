package com.example.shuffle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.shuffle.models.spotify.Playlist;
import com.example.shuffle.models.spotify.Track;
import com.example.shuffle.models.spotify.advanced_searchs.AudioFeatureResult;
import com.example.shuffle.models.spotify.query_result.TrackItem;
import com.example.shuffle.models.spotify.query_result.TypedItemResult;
import com.example.shuffle.service.SpotifyService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistGenerator {

    private Context applicationContext;
    private Activity activity;
    private SpotifyService spotifyService =  ConnectionBuilder.createService(SpotifyService.class);

    public PlaylistGenerator(Context applicationContext, Activity activity) {
        this.applicationContext = applicationContext;
        this.activity = activity;
    }

    public void createorderedShufflePlaylist(List<String> trackIds) {

        String ids = String.join(",", trackIds).replace("spotify:track:", "");
        Call<AudioFeatureResult> call =  spotifyService.getAutdioFeatureByTracks("Bearer " + ConnectionBuilder.ACCESS_TOKEN, ids);

        call.enqueue(new Callback<AudioFeatureResult>() {

            @Override
            public void onResponse(Call<AudioFeatureResult> call, Response<AudioFeatureResult> response) {
                PlaylistSorter sorter = new PlaylistSorter();
                createShufflePlaylist(sorter.orderByBattery(response.body(), applicationContext));
            }

            @Override
            public void onFailure(Call<AudioFeatureResult> call, Throwable t) {
                Log.i("DEU MERDA: ", t.getMessage());
            }
        });
    }

    private void createShufflePlaylist(List<String> trackIds) {

        Call<TypedItemResult<Playlist>> call =  spotifyService.getuserPlaylists("Bearer " + ConnectionBuilder.ACCESS_TOKEN);

        call.enqueue(new Callback<TypedItemResult<Playlist>>() {

            @Override
            public void onResponse(Call<TypedItemResult<Playlist>> call, Response<TypedItemResult<Playlist>> response) {

                TypedItemResult<Playlist> typedResult = response.body();

                Boolean createPlaylist = true;
                String shufflePlaylistId = null;

                for(Playlist currentPlaylist : typedResult.getItems()) {

                    if("Shuffle".equals(currentPlaylist.getName())) {
                        createPlaylist = false;
                        shufflePlaylistId = String.valueOf(currentPlaylist.getId());
                        break;
                    }
                }

                if(createPlaylist) {
                    createPlaylist(trackIds);
                } else {
                    findAndClearShufflePlaylist(shufflePlaylistId, trackIds);
                }
            }

            @Override
            public void onFailure(Call<TypedItemResult<Playlist>> call, Throwable t) {
                Log.i("DEU MERDA: ", t.getMessage());
            }
        });
    }

    private void insertTracksIntoShufflePlaylist(String shufflePlaylistId, List<String> trackIds) {

        String tracksUri = String.join(",", trackIds);

        Call<Void> call =  spotifyService.insertTracksIntoPlaylist("Bearer " + ConnectionBuilder.ACCESS_TOKEN, shufflePlaylistId, tracksUri);

        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                connected("spotify:playlist:" + shufflePlaylistId);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("DEU MERDA: ", t.getMessage());
            }
        });
    }

    private void createPlaylist(List<String> trackIds) {

        Playlist shufflePlaylist = new Playlist();
        shufflePlaylist.setName("Shuffle");
        shufflePlaylist.setDescription("The playlist made for your. @Shuffle");
        shufflePlaylist.setPublic(false);

        Call<Void> call =  spotifyService.createPlaylist("Bearer " + ConnectionBuilder.ACCESS_TOKEN, shufflePlaylist);

        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                String shufflePlaylistId = response.headers().get("location").replace("https://api.spotify.com/v1/playlists/", "");
                insertTracksIntoShufflePlaylist(shufflePlaylistId, trackIds);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("DEU MERDA: ", t.getMessage());
            }
        });
    }

    private void findAndClearShufflePlaylist(String shufflePlaylistId, List<String> tracksToInsert) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization","Bearer " + ConnectionBuilder.ACCESS_TOKEN);
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        Call<Playlist> call =  spotifyService.getPlaylistTracks(headers, shufflePlaylistId);

        call.enqueue(new Callback<Playlist>() {

            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {

                Playlist returnedPlaylist = response.body();
                List<Track> tracksToBeRemoved = new ArrayList<>();

                List<TrackItem> playlistTracks = returnedPlaylist.getTracks().getItems();
                if(playlistTracks!= null) {
                    for (TrackItem currentTrack : playlistTracks) {
                        tracksToBeRemoved.add(currentTrack.getTrack());
                    }
                }

                if(tracksToBeRemoved.isEmpty() == false) {
                    clearShufflePlaylist(shufflePlaylistId, tracksToBeRemoved, tracksToInsert);
                } else {
                    insertTracksIntoShufflePlaylist(shufflePlaylistId, tracksToInsert);
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                Log.i("DEU MERDA: ", t.getMessage());
            }
        });
    }

    private void clearShufflePlaylist(String shufflePlaylistId, List<Track> tracksToBeRemoved, List<String> tracksToInsert) {

        StringBuilder trackIds = new StringBuilder();
        trackIds.append("{\"tracks\":[");

        for(int i = 0; tracksToBeRemoved.size() > i; i++) {
            trackIds.append("{\"uri\": \"")
                    .append(tracksToBeRemoved.get(i).getUri())
                    .append("\"}");
            if(tracksToBeRemoved.size() > i + 1) {
                trackIds.append(", ");
            }
        }
        trackIds.append("]}");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization","Bearer " + ConnectionBuilder.ACCESS_TOKEN);
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        RequestBody body = RequestBody.create(MediaType.get("application/json"), trackIds.toString());
        Call<Void> call =  spotifyService.deleteTracksByPlaylist(headers, shufflePlaylistId, body);

        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                insertTracksIntoShufflePlaylist(shufflePlaylistId, tracksToInsert);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("DEU MERDA: ", t.getMessage());
            }
        });
    }

    private void connected(String shufflePlaylistUri) {

        // Play a
        ConnectionBuilder.mSpotifyAppRemote.getPlayerApi().play(shufflePlaylistUri);

        // Subscribe to PlayerState
        ConnectionBuilder.mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    if (playerState.track != null) {
                        Log.d("MainActivity", playerState.track.name + " by " + playerState.track.artist.name);
                    }
                });

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(shufflePlaylistUri));
        intent.putExtra(Intent.EXTRA_REFERRER,
                Uri.parse("android-app://" + this.applicationContext.getPackageName()));
        this.activity.startActivity(intent);
    }
}
