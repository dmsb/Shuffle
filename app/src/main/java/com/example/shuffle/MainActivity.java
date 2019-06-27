package com.example.shuffle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.shuffle.helpers.InstalledAppsChecker;
import com.example.shuffle.models.spotify.Playlist;
import com.example.shuffle.models.spotify.Track;
import com.example.shuffle.models.spotify.advanced_searchs.AudioFeatureResult;
import com.example.shuffle.models.spotify.query_result.TrackItem;
import com.example.shuffle.models.spotify.query_result.TypedItemResult;
import com.example.shuffle.service.SpotifyService;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.gms.ads.AdView;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ImageButton playButton;
    private ImageButton menuButton;
    private ImageButton searchButton;
    private ImageButton settingsButton;
    private AdView mAdView;
    public static Boolean isInstalledSpotify;

    private SpotifyService spotifyService =  ConnectionBuilder.createService(SpotifyService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*MobileAds.initialize(this, "ca-app-pub-8820391228754337~8248885609");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        this.searchButton = findViewById(R.id.search_button);
        this.settingsButton = findViewById(R.id.settings_button);
        this.menuButton = findViewById(R.id.menu_button);

        // Menu methods
        this.menuButton.setOnLongClickListener((View v) -> {
            Toast toast = Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.show();
            return false;
        });

        // Search button methods
        this.searchButton.setOnClickListener((View v) -> {
            Intent it = new Intent(this, SearchActivity.class);
            startActivity(it);
            finish();
        });
        this.searchButton.setOnLongClickListener((View v) -> {
            Toast toast = Toast.makeText(MainActivity.this, "Estilos", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.show();
            return false;
        });

        // Settings button methods
        this.settingsButton.setOnClickListener((View v) -> {
            Intent it = new Intent(this, SettingsActivity.class);
            startActivity(it);
            finish();
        });

        this.settingsButton.setOnLongClickListener((View v) -> {
            Toast toast = Toast.makeText(MainActivity.this, "Configurações", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.show();
            return false;
        });

        isInstalledSpotify = InstalledAppsChecker.isPackageInstalled("com.spotify.music", getPackageManager());
        if(isInstalledSpotify && ConnectionBuilder.ACCESS_TOKEN == null) {
            obtainAccessToken();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);

        if (ConnectionBuilder.AUTH_TOKEN_REQUEST_CODE == requestCode) {
            ConnectionBuilder.ACCESS_TOKEN = response.getAccessToken();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.playButton =  findViewById(R.id.play_button);

        this.playButton.setOnLongClickListener((View v) -> {
            Toast.makeText(MainActivity.this, "Shuffle", Toast.LENGTH_SHORT).show();
            return false;
        });

        this.playButton.setOnClickListener((View v) -> {

            //Rotation button
            float btn = this.playButton.getRotation() + 360F;
            this.playButton.animate().rotation(btn).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(1000);

            if(ConnectionBuilder.mSpotifyAppRemote != null && ConnectionBuilder.mSpotifyAppRemote.isConnected()) {
                buildShufflePlaylistAndPlay();
            } else {
                ConnectionParams connectionParams =
                        new ConnectionParams.Builder(ConnectionBuilder.CLIENT_ID)
                                .setRedirectUri(ConnectionBuilder.REDIRECT_URI)
                                .showAuthView(true)
                                .build();


                SpotifyAppRemote.connect(this, connectionParams,
                        new Connector.ConnectionListener() {

                            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                                ConnectionBuilder.mSpotifyAppRemote = spotifyAppRemote;
                                Log.d("MainActivity", "Connected! Yay!");
                                buildShufflePlaylistAndPlay();
                            }

                            public void onFailure(Throwable throwable) {
                                Log.e("MyActivity", throwable.getMessage(), throwable);
                                // Something went wrong when attempting to connect! Handle errors here
                            }
                        });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpotifyAppRemote.disconnect(ConnectionBuilder.mSpotifyAppRemote);
    }

    public void buildShufflePlaylistAndPlay() {

        if (ConnectionBuilder.ACCESS_TOKEN == null) {
            return;
        }

        Call<TypedItemResult<Track>> call =  spotifyService.getTopMusicsOfPlayer("Bearer " + ConnectionBuilder.ACCESS_TOKEN, 50);

        call.enqueue(new Callback<TypedItemResult<Track>>() {
            @Override
            public void onResponse(Call<TypedItemResult<Track>> call, Response<TypedItemResult<Track>> response) {

                final TypedItemResult<Track> typedResult = response.body();
                final List<String> trackIds = new ArrayList<>();

                for(final Track currentTrack : typedResult.getItems()) {
                    trackIds.add(currentTrack.getUri());
                }

                createorderedShufflePlaylist(trackIds);
            }

            @Override
            public void onFailure(Call<TypedItemResult<Track>> call, Throwable t) {
                Log.i("DEU MERDA: ", t.getMessage());
            }
        });
    }

    private AuthenticationRequest getAuthenticationRequest(AuthenticationResponse.Type type) {
        String[] scopes = "streaming user-read-recently-played user-top-read user-library-modify user-library-read playlist-read-private playlist-modify-public playlist-modify-private playlist-read-collaborative user-read-email user-read-birthdate user-read-private user-read-playback-state user-modify-playback-state user-read-currently-playing app-remote-control streaming".split("\\s+");
        return new AuthenticationRequest.Builder(ConnectionBuilder.CLIENT_ID, type, ConnectionBuilder.REDIRECT_URI)
                .setScopes(scopes)
                .build();
    }

    public void obtainAccessToken() {
        final AuthenticationRequest request = getAuthenticationRequest(AuthenticationResponse.Type.TOKEN);
        AuthenticationClient.openLoginActivity(this, ConnectionBuilder.AUTH_TOKEN_REQUEST_CODE, request);
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

    private void createorderedShufflePlaylist(List<String> trackIds) {

        String ids = String.join(",", trackIds).replace("spotify:track:", "");
        Call<AudioFeatureResult> call =  spotifyService.getAutdioFeatureByTracks("Bearer " + ConnectionBuilder.ACCESS_TOKEN, ids);

        call.enqueue(new Callback<AudioFeatureResult>() {

            @Override
            public void onResponse(Call<AudioFeatureResult> call, Response<AudioFeatureResult> response) {
                PlaylistSorter sorter = new PlaylistSorter();
                createShufflePlaylist(sorter.orderByBattery(response.body(), getApplicationContext()));
            }

            @Override
            public void onFailure(Call<AudioFeatureResult> call, Throwable t) {
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
            trackIds.append("{\"uri\":\"")
                    .append(tracksToBeRemoved.get(i).getUri())
                    .append("\"}");
            if(tracksToBeRemoved.size() > i + 1) {
                trackIds.append(",");
            }
        }
        trackIds.append("]}");
        String malformatedTracks = trackIds.toString();

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + ConnectionBuilder.ACCESS_TOKEN);
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        RequestBody body = RequestBody.create(MediaType.get("application/json"), malformatedTracks);

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
                Uri.parse("android-app://" + getApplicationContext().getPackageName()));
        startActivity(intent);
    }
}
