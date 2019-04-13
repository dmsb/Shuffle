package com.example.shuffle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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
}
