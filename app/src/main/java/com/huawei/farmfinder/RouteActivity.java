package com.huawei.farmfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.huawei.farmfinder.utils.NetworkRequestManager;
import com.huawei.hms.maps.CameraUpdate;
import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.SupportMapFragment;
import com.huawei.hms.maps.model.BitmapDescriptorFactory;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.LatLngBounds;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.MarkerOptions;
import com.huawei.hms.maps.model.Polyline;
import com.huawei.hms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.huawei.farmfinder.FarmActivity.FARM_KEY;

public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "RouteActivity";

    private SupportMapFragment mSupportMapFragment;
    private HuaweiMap hMap;
    private Marker mMarkerOrigin;
    private Marker mMarkerDestination;

    //private LatLng userLocation = new LatLng(7.0718902483971835, 80.0251324066693);

    //private LatLng farmLocation;
    private LatLng userLocation = new LatLng(7.07189, 80.0251);

    private LatLng farmLocation = new LatLng(7.07240, 80.0152);

    private List<Polyline> mPolylines = new ArrayList<>();

    private List<List<LatLng>> mPaths = new ArrayList<>();

    private LatLngBounds mLatLngBounds;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    renderRoute(mPaths, mLatLngBounds);
                    break;
                case 1:
                    Bundle bundle = msg.getData();
                    String errorMsg = bundle.getString("errorMsg");
                    Toast.makeText(RouteActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.mapfragment_routeplanning);
        if (fragment instanceof SupportMapFragment) {
            mSupportMapFragment = (SupportMapFragment) fragment;
            mSupportMapFragment.getMapAsync(this);

            // If the API level is 23 or higher (Android 6.0 or later), you need to dynamically apply for permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.i(TAG, "sdk >= 23 M");
                // Check whether your app has the specified permission and whether the app operation corresponding to the permission is allowed.
                if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Request permissions for your app.
                    String[] strings =
                            {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                    // Request permissions.
                    ActivityCompat.requestPermissions(this, strings, 1);
                }
            }
        }
    }

    @Override
    public void onMapReady(HuaweiMap huaweiMap) {
        hMap = huaweiMap;

        // display farm marker
        Intent intent = getIntent();
        if (null != intent) {
            int farmId = intent.getIntExtra(FARM_KEY, -1);
            Log.d(TAG, "onCreate: called");
            if (farmId != -1) {
                Farm selectedFarm = Data.getInstance().getFarmById(farmId);
                if (null != selectedFarm) {
                    //farmLocation = new LatLng(selectedFarm.getLocation().getLat(), selectedFarm.getLocation().getLng());

                    hMap.moveCamera(CameraUpdateFactory.newLatLngZoom(farmLocation, 13));
                    addOriginMarker(userLocation);
                    addDestinationMarker(farmLocation);
                    getDrivingRouteResult();
                }
            }
        }
    }

    private void addOriginMarker(LatLng latLng) {
        if (null != mMarkerOrigin) {
            mMarkerOrigin.remove();
        }
        mMarkerOrigin = hMap.addMarker(new MarkerOptions().position(latLng)
                .anchorMarker(0.5f, 0.9f)
                .title("Your Location")
                .snippet(latLng.toString()));
    }

    private void addDestinationMarker(LatLng latLng) {
        if (null != mMarkerDestination) {
            mMarkerDestination.remove();
        }
        mMarkerDestination = hMap.addMarker(
                new MarkerOptions().position(latLng)
                        .anchorMarker(0.5f, 0.9f)
                        .title("farmName").snippet(latLng.toString()));
    }

    public void getDrivingRouteResult() {
        NetworkRequestManager.getDrivingRoutePlanningResult(userLocation, farmLocation,
                new NetworkRequestManager.OnNetworkListener() {
                    @Override
                    public void requestSuccess(String result) {
                        generateRoute(result);
                    }

                    @Override
                    public void requestFail(String errorMsg) {
                        Message msg = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putString("errorMsg", errorMsg);
                        msg.what = 1;
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                    }
                });
    }

    private void generateRoute(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray routes = jsonObject.optJSONArray("routes");
            if (null == routes || routes.length() == 0) {
                return;
            }
            JSONObject route = routes.getJSONObject(0);

            // get route bounds
            JSONObject bounds = route.optJSONObject("bounds");
            if (null != bounds && bounds.has("southwest") && bounds.has("northeast")) {
                JSONObject southwest = bounds.optJSONObject("southwest");
                JSONObject northeast = bounds.optJSONObject("northeast");
                LatLng sw = new LatLng(southwest.optDouble("lat"), southwest.optDouble("lng"));
                LatLng ne = new LatLng(northeast.optDouble("lat"), northeast.optDouble("lng"));
                mLatLngBounds = new LatLngBounds(sw, ne);
            }

            // get paths
            JSONArray paths = route.optJSONArray("paths");
            for (int i = 0; i < paths.length(); i++) {
                JSONObject path = paths.optJSONObject(i);
                List<LatLng> mPath = new ArrayList<>();

                JSONArray steps = path.optJSONArray("steps");
                for (int j = 0; j < steps.length(); j++) {
                    JSONObject step = steps.optJSONObject(j);

                    JSONArray polyline = step.optJSONArray("polyline");
                    for (int k = 0; k < polyline.length(); k++) {
                        if (j > 0 && k == 0) {
                            continue;
                        }
                        JSONObject line = polyline.getJSONObject(k);
                        double lat = line.optDouble("lat");
                        double lng = line.optDouble("lng");
                        LatLng latLng = new LatLng(lat, lng);
                        mPath.add(latLng);
                    }
                }
                mPaths.add(i, mPath);
            }
            mHandler.sendEmptyMessage(0);

        } catch (JSONException e) {
            Log.e(TAG, "JSONException" + e.toString());
        }
    }

    /**
     * Render the route planning result
     *
     * @param paths
     * @param latLngBounds
     */
    private void renderRoute(List<List<LatLng>> paths, LatLngBounds latLngBounds) {
        if (null == paths || paths.size() <= 0 || paths.get(0).size() <= 0) {
            return;
        }

        for (int i = 0; i < paths.size(); i++) {
            List<LatLng> path = paths.get(i);
            PolylineOptions options = new PolylineOptions().color(Color.BLUE).width(5);
            for (LatLng latLng : path) {
                options.add(latLng);
            }

            Polyline polyline = hMap.addPolyline(options);
            mPolylines.add(i, polyline);
        }

        addOriginMarker(paths.get(0).get(0));
        addDestinationMarker(paths.get(0).get(paths.get(0).size() - 1));

        if (null != latLngBounds) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, 5);
            hMap.moveCamera(cameraUpdate);
        } else {
            hMap.moveCamera(CameraUpdateFactory.newLatLngZoom(paths.get(0).get(0), 13));
        }

    }
}