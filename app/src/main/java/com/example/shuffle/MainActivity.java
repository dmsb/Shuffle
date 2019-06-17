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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.google.android.gms.ads.AdView;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ImageButton playButton;
    private ImageButton menuButton;
    private ImageButton searchButton;
    private ImageButton settingsButton;
    private AdView mAdView;
    private static final String REDIRECT_URI = "https://open.spotify.com/";

    private SpotifyAppRemote mSpotifyAppRemote;

   // public static final String CLIENT_ID = "d7ebba782ca24b48a51094cfc9dbd152"; //jorge id
    public static final String CLIENT_ID = "0599d96797ef4cd19d655b778aacaa27"; //diogo id
    public static final int AUTH_TOKEN_REQUEST_CODE = 1337;

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    public static String ACCESS_TOKEN;
    private Call mCall;

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

        if(ACCESS_TOKEN == null) {
            obtainAccessToken();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);

        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            ACCESS_TOKEN = response.getAccessToken();
        }
    }

    public void buildShufflePlaylistAndPlay() {

        if (ACCESS_TOKEN == null) {
            return;
        }

        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/tracks")
                .addHeader("Authorization","Bearer " + ACCESS_TOKEN)
                .build();

        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Failed to parse data: ", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    JSONArray tracks = object.getJSONArray("items");
                    List<String> trackIds = new ArrayList<>();

                    for(int i = 0; i < tracks.length(); i++) {
                        String trackId = String.valueOf(tracks.getJSONObject(i).get("uri"));
                        trackIds.add(trackId);
                    }

                    createShufflePlaylist(trackIds);

                } catch (JSONException e) {
                    Log.e("Failed to parse data: ", e.getMessage());
                }
            }
        });
    }

    private AuthenticationRequest getAuthenticationRequest(AuthenticationResponse.Type type) {
        String[] scopes = "streaming user-read-recently-played user-top-read user-library-modify user-library-read playlist-read-private playlist-modify-public playlist-modify-private playlist-read-collaborative user-read-email user-read-birthdate user-read-private user-read-playback-state user-modify-playback-state user-read-currently-playing app-remote-control streaming".split("\\s+");
        return new AuthenticationRequest.Builder(CLIENT_ID, type, "https://open.spotify.com/")
                .setScopes(scopes)
                .build();
    }

    public void obtainAccessToken() {
        final AuthenticationRequest request = getAuthenticationRequest(AuthenticationResponse.Type.TOKEN);
        AuthenticationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    private void createShufflePlaylist(List<String> trackIds) {

        List<String> orderedTrackIds = orderShufflePlaylist(trackIds);

        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/playlists")
                .addHeader("Authorization","Bearer " + ACCESS_TOKEN)
                .build();

        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ERROR: ", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    JSONArray playlists = object.getJSONArray("items");
                    Boolean createPlaylist = true;
                    String shufflePlaylistId = null;
                    for(int i = 0; i < playlists.length(); i++) {
                        String playListName = String.valueOf(playlists.getJSONObject(i).get("name"));
                        if("Shuffle".equals(playListName)) {
                            createPlaylist = false;
                            shufflePlaylistId = String.valueOf(playlists.getJSONObject(i).get("id"));
                            break;
                        }
                    }

                    if(createPlaylist) {
                        createPlaylist(orderedTrackIds);
                    } else {
                        findAndClearShufflePlaylist(shufflePlaylistId, orderedTrackIds);
                    }

                } catch (JSONException e) {
                    Log.e("Failed to parse data: ", e.getMessage());
                }
            }
        });
    }

    private List<String> orderShufflePlaylist(List<String> trackIds) {

        List<String> ltReturnTracks = new ArrayList<>();
        String params = "";


        for(int i = 0; i < trackIds.size(); i++) {

            String trackId = trackIds.get(i).replace("spotify:track:", "");
            params += trackId;

            if(i < trackIds.size() - 1) {
                params += "%2C";
            }
        }

        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/audio-features/?ids=" + params)
                .addHeader("Authorization","Bearer " + ACCESS_TOKEN)
                .build();

        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ERROR: ", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    Gson gsonObject = new Gson();
                    List<AudioFeature> convertedTracks = gsonObject.fromJson(
                            object.getString("audio_features"), new TypeToken<List<AudioFeature>>(){}.getType());
                    PlaylistSorter sorter = new PlaylistSorter();
                    ltReturnTracks.addAll(sorter.orderByBattery(convertedTracks, getApplicationContext()));

                } catch (JSONException e) {
                    Log.e("Failed to parse data: ", e.getMessage());
                }
            }
        });

        return ltReturnTracks;
    }

    private void insertTracksIntoShufflePlaylist(String shufflePlaylistId, List<String> trackIds) {

        String tracksUri = String.join(",", trackIds)
                    .replaceAll(":", "%3A")
                    .replaceAll(",", "%2C");
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/playlists/" + shufflePlaylistId + "/tracks?uris=" + tracksUri)
                .addHeader("Authorization","Bearer " + ACCESS_TOKEN)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .post(RequestBody.create(MediaType.parse("application/json"), "{}"))
                .build();

        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ERROR: ", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("INFO: ", response.message());
                connected("spotify:playlist:" + shufflePlaylistId);
            }
        });
    }

    private void createPlaylist(List<String> trackIds) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),"{\"name\":\"Shuffle\",\"description\":\"Shuffle auto generated playlist\",\"public\":false}\"");
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/playlists")
                .addHeader("Authorization","Bearer " + ACCESS_TOKEN)
                .post(body)
                .build();

        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ERROR: ", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("INFO: ", response.message());
                String shufflePlaylistId = response.header("location").replace("https://api.spotify.com/v1/playlists/", "");
                findAndClearShufflePlaylist(shufflePlaylistId, trackIds);
            }
        });
    }

    private void findAndClearShufflePlaylist(String shufflePlaylistId, List<String> tracksToInsert) {
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/playlists/" + shufflePlaylistId + "?fields=tracks.items.track(uri)")
                .addHeader("Authorization","Bearer " + ACCESS_TOKEN)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();

        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ERROR: ", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<String> tracksToBeRemoved = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    JSONArray tracks = object.getJSONObject("tracks").getJSONArray("items");

                    for (int i = 0; i < tracks.length(); i++) {
                        String trackUri = String.valueOf(tracks.getJSONObject(i).getJSONObject("track").get("uri"));
                        tracksToBeRemoved.add(trackUri);
                    }
                } catch (JSONException e) {
                    Log.e("ERROR", e.getMessage());
                }

                if(tracksToBeRemoved.isEmpty() == false) {
                    clearShufflePlaylist(shufflePlaylistId, tracksToBeRemoved, tracksToInsert);
                } else {
                    insertTracksIntoShufflePlaylist(shufflePlaylistId, tracksToInsert);
                }

            }
        });
    }

    private void clearShufflePlaylist(String shufflePlaylistId, List<String> tracksToBeRemoved, List<String> tracksToInsert) {
        StringBuilder trackIds = new StringBuilder();
        trackIds.append("{\"tracks\":[");

        for(int i = 0; tracksToBeRemoved.size() > i; i++) {
            trackIds.append("{\"uri\": \"")
                    .append(tracksToBeRemoved.get(i))
                    .append("\"}");
            if(tracksToBeRemoved.size() > i + 1) {
                trackIds.append(", ");
            }
        }
        trackIds.append("]}");

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), trackIds.toString());
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/playlists/" + shufflePlaylistId + "/tracks")
                .addHeader("Authorization","Bearer " + ACCESS_TOKEN)
                .delete(body)
                .build();

        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ERROR: ", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("INFO: ", response.message());
                insertTracksIntoShufflePlaylist(shufflePlaylistId, tracksToInsert);
            }
        });
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

            boolean isInstalledSpotify = InstalledAppsChecker.isPackageInstalled("com.spotify.music", getPackageManager());

            if(isInstalledSpotify) {
                ConnectionParams connectionParams =
                        new ConnectionParams.Builder(CLIENT_ID)
                                .setRedirectUri(REDIRECT_URI)
                                .showAuthView(true)
                                .build();

                SpotifyAppRemote.connect(this, connectionParams,
                        new Connector.ConnectionListener() {

                            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                                mSpotifyAppRemote = spotifyAppRemote;
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
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private void connected(String shufflePlaylistUri) {

        // Play a
        mSpotifyAppRemote.getPlayerApi().play(shufflePlaylistUri);

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(shufflePlaylistUri));
        intent.putExtra(Intent.EXTRA_REFERRER,
                Uri.parse("android-app://" + getApplicationContext().getPackageName()));
        startActivity(intent);
    }
}
