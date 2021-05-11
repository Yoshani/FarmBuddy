package com.huawei.farmfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;

public class FarmRecyclerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_farm_recycler_view);

        RecyclerView recyclerView = findViewById(R.id.farmRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FarmRecyclerViewAdapter farmRecyclerViewAdapter = new FarmRecyclerViewAdapter(this, Data.getInstance().getFarms());
        recyclerView.setAdapter(farmRecyclerViewAdapter);
    }
}