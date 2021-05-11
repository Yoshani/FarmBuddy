package com.huawei.farmfinder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.huawei.hms.location.Geofence;
import com.huawei.hms.location.GeofenceData;
import java.util.List;

import static com.huawei.hms.push.constant.RemoteMessageConst.Notification.CHANNEL_ID;

// Register a broadcast receiver for the geofence service.
public class GeoFenceBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_PROCESS_LOCATION = "com.huawei.farmfinder.GeoFenceBroadcastReceiver.ACTION_PROCESS_LOCATION";
    private static final String TAG = "GeoFenceReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            StringBuilder sb = new StringBuilder();
            String next = "\n";
            if (ACTION_PROCESS_LOCATION.equals(action)) {
                // Obtain the GeofenceData object from the intent.
                GeofenceData geofenceData = GeofenceData.getDataFromIntent(intent);
                if (geofenceData != null) {
                    // Obtain a result code.
                    int errorCode = geofenceData.getErrorCode();
                    // Obtain the geofence trigger type.
                    int conversion = geofenceData.getConversion();
                    // Obtain information about the triggered geofence.
                    List<Geofence> list = geofenceData.getConvertingGeofenceList();
                    // Obtain information about the location when the geofence is triggered.
                    Location mLocation = geofenceData.getConvertingLocation();
                    // Check whether the geofence event is triggered normally. If false is returned, an error occurs.
                    boolean status = geofenceData.isSuccess();
//
//                    String locId = list.get(0).getUniqueId();
//                    sendNotification(locId, context);

                    sb.append("errorcode: ").append(errorCode).append(next);
                    sb.append("conversion: ").append(conversion).append(next);
                    for (int i = 0; i < list.size(); i++){
                        sb.append("geoFence id :").append(list.get(i).getUniqueId()).append(next);
                    }
                    sb.append("location is :").append(mLocation.getLongitude()).append(" ").append(mLocation.getLatitude()).append(next);
                    sb.append("is successful :").append(status);
                    Log.i(TAG,sb.toString());
                    if (status) {
                        Toast.makeText(context.getApplicationContext(), "You are nearing a farm! Visit us for fresh supplies.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

//    private void sendNotification(String locId, Context context) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_location)
//                .setContentTitle("Location Reached")
//                .setContentText(" you reached " + locId)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                // Set the intent that will fire when the user taps the notification
//                .setAutoCancel(true);
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        // notificationId is a unique int for each notification that you must define
//        notificationManager.notify(1, builder.build());
//    }
}