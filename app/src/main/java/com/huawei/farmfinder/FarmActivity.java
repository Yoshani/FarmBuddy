package com.huawei.farmfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;


public class FarmActivity extends AppCompatActivity {
    
    private static final String TAG = "FarmActivity";
    public static final String FARM_KEY = "farmKey";

    private TextView name, owner, description;
    private ImageView farmImage;
    private Button viewLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_farm);
        initViews();

        // set data
        Intent intent = getIntent();
        if (null != intent) {
            int farmId = intent.getIntExtra(FARM_KEY, -1);
            Log.d(TAG, "onCreate: called");
            if (farmId != -1) {
                Farm selectedFarm = Data.getInstance().getFarmById(farmId);
                if (null != selectedFarm) {
                    setData(selectedFarm);
                    onClickLocationButton(selectedFarm);
                }
            }
        }

    }

    private void onClickLocationButton(Farm farm) {
        viewLocationButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, MapActivity.class);
            intent1.putExtra(FARM_KEY, farm.getId());
            startActivity(intent1);
        });
    }

    private void initViews() {
        name = findViewById(R.id.name);
        owner = findViewById(R.id.owner);
        description = findViewById(R.id.description);
        farmImage = findViewById(R.id.farmImage);
        viewLocationButton = findViewById(R.id.viewLocation);
    }

    private void setData(Farm farm) {
        name.setText(farm.getName());
        String ownerText = "By " + farm.getOwner();
        owner.setText(ownerText);
        description.setText(farm.getDescription());
        Glide.with(this).asBitmap().load(farm.getImageUrl()).into(farmImage);
    }


}