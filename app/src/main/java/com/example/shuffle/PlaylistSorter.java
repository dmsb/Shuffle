package com.example.shuffle;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;



public class PlaylistSorter {


    public List<String> orderByBattery(JSONArray tracksArray, Context context){

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level / (float)scale;

        List<String> ltReturnTracks = new ArrayList<>();
        List<String> ltAUXTracks = new ArrayList<>();

        try{

            for(int i = 0; i < tracksArray.length(); i++) {

                Double value = Double.valueOf((Double)tracksArray.getJSONObject(i).get("energy"));

                if( value < batteryPct){
                    ltReturnTracks.add(String.valueOf(tracksArray.getJSONObject(i).get("id")));
                }else{
                    ltAUXTracks.add(String.valueOf(tracksArray.getJSONObject(i).get("id")));
                }
            }

            ltReturnTracks.addAll(ltAUXTracks);

        } catch (JSONException e) {
            Log.e("Failed to parse data: ", e.getMessage());
        }


        return ltReturnTracks;

    }
}
