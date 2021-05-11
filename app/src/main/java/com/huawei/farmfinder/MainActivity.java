package com.huawei.farmfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout mainScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        initViews();

        // navigate to farms list screen
        mainScreen.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FarmRecyclerViewActivity.class);
            startActivity(intent);
        });

    }

    private void initViews() {
        mainScreen = findViewById(R.id.main);
    }
}