package com.example.shuffle;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.example.shuffle.models.spotify.advanced_searchs.AudioFeature;

import java.util.ArrayList;
import java.util.List;

public class PlaylistSorter {

    public List<String> orderByBattery(List<AudioFeature> tracks, Context context){

        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, iFilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float)scale;

        List<String> tracksIdOrderedByBattery        = new ArrayList<>();
        List<String> tracksWithEnergyMoreThenBattery = new ArrayList<>();

        for(AudioFeature currentTrack : tracks) {
            if( currentTrack.getEnergy() < batteryPct){
                tracksIdOrderedByBattery.add(currentTrack.getUri());
            } else {
                tracksWithEnergyMoreThenBattery.add(currentTrack.getUri());
            }
        }
        tracksIdOrderedByBattery.addAll(tracksWithEnergyMoreThenBattery);

        return tracksIdOrderedByBattery;
    }
}
