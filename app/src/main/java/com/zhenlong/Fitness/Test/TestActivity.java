package com.zhenlong.Fitness.Test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.zhenlong.Fitness.R;
import com.zhenlong.Fitness.Service.MapService;

public class TestActivity extends AppCompatActivity {
    private MapService mapService= new MapService();
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        btn = findViewById(R.id.);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Coordinate coordinate = null;
//                if (ActivityCompat.checkSelfPermission(TestActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED & ActivityCompat.checkSelfPermission(TestActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    coordinate = mapService.getMyCurrentLocation(TestActivity.this);
//                } else {
//                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
//                }
//            }
//        });
    }
}
