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

    public List<String> orderMusics(JSONArray tracksArray, Context context){


        List<String> ltReturnTracks = new ArrayList<>();
        JSONArray jsonMusicas = orderByBattery(tracksArray,context);

        jsonMusicas = orderByBattery(jsonMusicas,context);

        try{

            for(int i = 0; i < jsonMusicas.length(); i++) {

                ltReturnTracks.add("spotify:track:" + String.valueOf(tracksArray.getJSONObject(i).get("id")));

            }

        } catch (JSONException e) {
            Log.e("Failed to parse data: ", e.getMessage());
        }


        return ltReturnTracks;
    }


    public JSONArray orderByBattery(JSONArray tracksArray, Context context){

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level / (float)scale;

        JSONArray ltReturnTracks = new JSONArray();
        JSONArray ltAUXTracks    = new JSONArray();

        JSONArray ltReturn    = new JSONArray();

        try{

            for(int i = 0; i < tracksArray.length(); i++) {

                Double value = Double.valueOf((Double)tracksArray.getJSONObject(i).get("energy"));

                if( value < batteryPct){
                    ltReturnTracks.put(i,tracksArray.getJSONObject(i));
                }else{
                    ltAUXTracks.put(i,tracksArray.getJSONObject(i));
                }
            }


            for(int i = 0; i < ltReturnTracks.length(); i++) {

                ltReturn.put(i,ltReturnTracks.getJSONObject(i));
            }

            for(int i = 0; i < ltAUXTracks.length(); i++) {

                ltReturn.put(i,ltReturnTracks.getJSONObject(i));
            }



        } catch (JSONException e) {
            Log.e("Failed to parse data: ", e.getMessage());
        }


        return ltReturn;

    }
}
