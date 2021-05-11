package com.huawei.farmfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.CheckBox;
import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.MapFragment;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static com.huawei.farmfinder.FarmActivity.FARM_KEY;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "MapActivity";

    private HuaweiMap hMap;
    private MapFragment mMapFragment;
    private CheckBox mCheckBox;
    private GeoFenceActivity geoFenceActivity;
    private static ArrayList<Farm> geoFencedFarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);

        // set fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // If the API level is 23 or higher (Android 6.0 or later), you need to dynamically apply for permissions.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "sdk >= 23 M");
            // Check whether your app has the specified permission and whether the app operation corresponding to the permission is allowed.
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Request permissions for your app.
                String[] strings =
                        {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                // Request permissions.
                ActivityCompat.requestPermissions(this, strings, 1);
            }
        }

        setContentView(R.layout.activity_map);
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mCheckBox = findViewById(R.id.alertChekBox);
        if (null == geoFencedFarms) {
            geoFencedFarms = new ArrayList<>();
        }
        if (null == geoFenceActivity) {
            geoFenceActivity = new GeoFenceActivity(this);
        }

        mMapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(HuaweiMap map) {
        Log.d(TAG, "onMapReady: ");
        hMap = map;
        hMap.setMyLocationEnabled(true);
        hMap.getUiSettings().setMyLocationButtonEnabled(true);
        hMap.setBuildingsEnabled(true);
        hMap.setTrafficEnabled(true);

        // display farm marker
        Intent intent = getIntent();
        if (null != intent) {
            int farmId = intent.getIntExtra(FARM_KEY, -1);
            Log.d(TAG, "onCreate: called");
            if (farmId != -1) {
                Farm selectedFarm = Data.getInstance().getFarmById(farmId);
                if (null != selectedFarm) {
                    LatLng mCoordinates = new LatLng(selectedFarm.getLocation().getLat(), selectedFarm.getLocation().getLng());
                    hMap.addMarker(new MarkerOptions().position(mCoordinates)
                            .anchorMarker(0.5f, 0.9f)
                            .title(selectedFarm.getName()));
                    hMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCoordinates, 10));
                    hMap.animateCamera(CameraUpdateFactory.zoomIn());

                    if (checkIfFarmGeoFenced(selectedFarm)) {
                        mCheckBox.setChecked(true);
                    }
                    alertWhenNearFarm(selectedFarm);
                }
            }
        }
    }

    public void alertWhenNearFarm(Farm farm) {
        mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()) {
                geoFenceActivity.addGeoFenceLocation(farm);
                geoFencedFarms.add(farm);
            }
            else
            {
                geoFenceActivity.removeWithID(farm);
                geoFencedFarms.remove(farm);
            }
        });
    }

    private boolean checkIfFarmGeoFenced(Farm farm) {
        return geoFencedFarms.contains(farm);
    }
}