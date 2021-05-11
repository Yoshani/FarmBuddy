package com.huawei.farmfinder;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.huawei.hms.location.Geofence;
import com.huawei.hms.location.GeofenceRequest;
import com.huawei.hms.location.GeofenceService;
import com.huawei.hms.location.LocationServices;
import java.util.ArrayList;

public class GeoFenceActivity {

    private final Context mContext;
    private final GeofenceService geofenceService;
    private ArrayList<String> idList;
    private ArrayList<Geofence> geofenceList;
    private final String TAG;
    private PendingIntent pendingIntent;
    private boolean geoFenceActivated;

    // data is passed into the constructor

    protected GeoFenceActivity(Context context) {
        this.mContext = context;
        // Create a GeofenceService instance.
        geofenceService = LocationServices.getGeofenceService(mContext);
        geofenceList = new ArrayList<>();
        // Obtain a PendingIntent object.
        pendingIntent = getPendingIntent();
        TAG = "geoFence";
        geoFenceActivated = false;
    }

    private GeofenceRequest getAddGeofenceRequest() {
        GeofenceRequest.Builder builder = new GeofenceRequest.Builder();
        // Trigger callback immediately after a geofence is added if a user is already in the geofence.
        builder.setInitConversions(GeofenceRequest.ENTER_INIT_CONVERSION);
        builder.createGeofenceList(geofenceList);
        return builder.build();
    }


    // Dynamically register GeoFenceBroadcastReceiver through PendingIntent. A broadcast message will be sent when the geofence is triggered.
    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(mContext, GeoFenceBroadcastReceiver.class);
        intent.setAction(GeoFenceBroadcastReceiver.ACTION_PROCESS_LOCATION);
        return PendingIntent.getBroadcast(mContext,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void requestGeoFenceWithNewIntent() {
        // Add a geofence by passing PendingIntent, and process response to the geofence addition request.
        try {
            geofenceService.createGeofenceList(getAddGeofenceRequest(), pendingIntent)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "add geofence success!");
                        } else {
                            Log.w(TAG, "add geofence failed : " + task.getException().getMessage());
                        }
                    });
        } catch (Exception e) {
            Log.d(TAG, "requestGeoFenceWithNewIntent: " + e.toString());
        }
    }

    public void removeWithID(Farm farm) {
        idList = new ArrayList<>();
        String id = farm.getName() + ": " + farm.getId();
        idList.add(id);
        // Remove a geofence based on its ID, and process response to the geofence deletion request.
        geofenceService.deleteGeofenceList(idList)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "delete geofence with ID "+ id +" success!");
                        // remove geofence with ID
                        for (Geofence geofence: geofenceList) {
                            if (geofence.getUniqueId().equals(id)) {
                                geofenceList.remove(geofence);
                            }
                        }
                    } else {
                        Log.w(TAG, "delete geofence with ID "+ id + " failed");
                    }
                });
    }

    public void addGeoFenceLocation(Farm farm) {
        String id = farm.getName() + ": " + farm.getId();
        geofenceList.add(new Geofence.Builder()
                .setUniqueId(id)
                .setValidContinueTime(10000)
                // Pass the longitude and latitude of the geofence center, and radius of the geofence (unit: meters).
                .setRoundArea(farm.getLocation().getLat(),farm.getLocation().getLng(), 2000)
                // Trigger callback when a user enters the geofence.
                .setConversions(Geofence.ENTER_GEOFENCE_CONVERSION)
                .setNotificationInterval(1000)
                .build());
        Log.d(TAG, "addGeoFenceLocation: number of geofences activated: "+geofenceList.size());
        Log.d(TAG, "addGeoFenceLocation: GeoFencing activated");
        requestGeoFenceWithNewIntent();
    }
}