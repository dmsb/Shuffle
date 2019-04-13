package com.example.shuffle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "0599d96797ef4cd19d655b778aacaa27"; //código do conta
    private static final String REDIRECT_URI = "https://open.spotify.com/";
    private SpotifyAppRemote mSpotifyAppRemote;

    private ImageButton playButton;
    private ImageButton menuButton;
    private ImageButton searchButton;
    private ImageButton settingsButton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        });
        this.settingsButton.setOnLongClickListener((View v) -> {
            Toast toast = Toast.makeText(MainActivity.this, "Configurações", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.show();
            return false;
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

                        // Now you can start interacting with App Remote
                        connected();

                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private void connected() {
        // Play a playlist
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");
        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }
}
