package com.example.shuffle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.shuffle.helpers.InstalledAppsChecker;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

public class SearchActivity extends AppCompatActivity {

    private ImageButton menuButton;
    private ImageButton searchButton;
    private ImageButton settingsButton;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(MainActivity.mSpotifyAppRemote);
    }

    private void onGenderClick(View view) {

        //loudness : sonoridade
        //instrumentalness : quanto de instrumentação tem na musica
        //speechiness : quanto de palavras faladas tem na musica
        //tempo : batidas por minuto (BPM)
        //valence : usado pra medor se a musica é feliz ou triste.
        //energy : usada pra dizer se a musica é agitada ou calma.
        //

        String selectedGenderValue = String.valueOf(view.getTag());

        ConnectionParams connectionParams =
                new ConnectionParams.Builder(MainActivity.CLIENT_ID)
                        .setRedirectUri(MainActivity.REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        MainActivity.mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");
                        //searchMusicsByGender(selectedGenderValue);
                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);
                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }

    private void parametrizedMusicSearch() {

    }
}
