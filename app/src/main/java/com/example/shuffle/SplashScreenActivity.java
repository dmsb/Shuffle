package com.example.shuffle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                getMainPage();
            }
        }, 2000);

    }

    private void getMainPage() {
        Intent it = new Intent(SplashScreenActivity.this,
                MainActivity.class);
        startActivity(it);
        finish();
    }
}
