package com.example.shuffle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageButton playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Rotation button
        this.playButton =  findViewById(R.id.play_button);
        float btn = this.playButton.getRotation() + 360F;
        this.playButton.animate().rotation(btn).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(1000);

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
