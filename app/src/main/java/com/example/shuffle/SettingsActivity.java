package com.example.shuffle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton menuButton;
    private ImageButton searchButton;
    private ImageButton settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.searchButton = findViewById(R.id.search_button);
        this.settingsButton = findViewById(R.id.settings_button);
        this.menuButton = findViewById(R.id.menu_button);
        
        // Settings button methods
        this.settingsButton.setOnLongClickListener((View v) -> {
            Toast toast = Toast.makeText(SettingsActivity.this, "Configurações", Toast.LENGTH_SHORT);
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
            Toast toast = Toast.makeText(SettingsActivity.this, "Estilos", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.show();
            return false;
        });

        // Menu methods
        this.menuButton.setOnLongClickListener((View v) -> {
            Toast toast = Toast.makeText(SettingsActivity.this, "Home", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.show();
            return false;
        });
        this.menuButton.setOnClickListener((View v) -> {
            Intent it = new Intent(this, MainActivity.class);
            startActivity(it);
        });

    }
}
