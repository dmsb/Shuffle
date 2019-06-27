package com.example.shuffle;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
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
import com.example.shuffle.models.spotify.query_result.GenericTypedItemResult;
import com.example.shuffle.models.spotify.query_result.TypedItemResult;
import com.example.shuffle.service.SpotifyService;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private ImageButton menuButton;
    private ImageButton searchButton;
    private ImageButton settingsButton;
    private SpotifyService spotifyService =  ConnectionBuilder.createService(SpotifyService.class);
    private PlaylistGenerator playlistGenerator;
    public static Boolean isInstalledSpotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.searchButton = findViewById(R.id.search_button);
        this.settingsButton = findViewById(R.id.settings_button);
        this.menuButton = findViewById(R.id.menu_button);

        // Search button methods
        this.searchButton.setOnLongClickListener((View v) -> {
            Toast toast = Toast.makeText(SearchActivity.this, "Estilos", Toast.LENGTH_SHORT);
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
            Toast toast = Toast.makeText(SearchActivity.this, "Configurações", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.show();
            return false;
        });

        // Menu methods
        this.menuButton.setOnLongClickListener((View v) -> {
            Toast toast = Toast.makeText(SearchActivity.this, "Home", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.show();
            return false;
        });
        this.menuButton.setOnClickListener((View v) -> {
            Intent it = new Intent(this, MainActivity.class);
            startActivity(it);
            finish();
        });

        isInstalledSpotify = InstalledAppsChecker.isPackageInstalled("com.spotify.music", getPackageManager());
        if(isInstalledSpotify && ConnectionBuilder.ACCESS_TOKEN == null) {
            ConnectionBuilder.obtainAccessToken(this);
        }
        this.playlistGenerator = new PlaylistGenerator(getApplicationContext(), this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(ConnectionBuilder.mSpotifyAppRemote);
    }

    public void onGenderClick(View view) {

        //loudness : sonoridade
        //instrumentalness : quanto de instrumentação tem na musica
        //speechiness : quanto de palavras faladas tem na musica
        //tempo : batidas por minuto (BPM)
        //valence : usado pra medor se a musica é feliz ou triste.
        //energy : usada pra dizer se a musica é agitada ou calma.

        String tagStringValue = String.valueOf(view.getTag());

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
                        searchByPredicate(tagStringValue);
                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);
                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }

    private void searchByPredicate(final String selectedGenreTag) {
        AvailableGenres selectedGenre = AvailableGenres.valueOf(selectedGenreTag);
        String genre = String.join(",", selectedGenre.getGenres()[0]);
        Map<String, String> predicate = new HashMap();
        predicate.put("q", "\"genre:" + genre + "\"");
        predicate.put("type", "track");
        predicate.put("limit", "50");

        Call<GenericTypedItemResult> call =  spotifyService.searchByPredicate("Bearer " + ConnectionBuilder.ACCESS_TOKEN, predicate);

        call.enqueue(new Callback<GenericTypedItemResult>() {
            @Override
            public void onResponse(Call<GenericTypedItemResult> call, Response<GenericTypedItemResult> response) {

                final GenericTypedItemResult typedResult = response.body();
                final List<String> trackIds = new ArrayList<>();

                for(final Track currentTrack : typedResult.getTracks().getItems()) {
                    trackIds.add(currentTrack.getUri());
                }
                playlistGenerator.createorderedShufflePlaylist(trackIds);
            }

            @Override
            public void onFailure(Call<GenericTypedItemResult> call, Throwable t) {
                Log.i("DEU MERDA: ", t.getMessage());
            }
        });
    }
}
