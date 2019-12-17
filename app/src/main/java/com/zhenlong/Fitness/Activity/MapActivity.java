package com.zhenlong.Fitness.Activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.zhenlong.Fitness.R;
import com.zhenlong.Fitness.Util.ShowToast;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, View.OnClickListener {

    private GoogleMap map;
    private double mLongitude = 0, mLatitude = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initMap();
        initUserLocation();
        Button confirmBtn = findViewById(R.id.map_confirm);
        confirmBtn.setOnClickListener(this);
    }

    private void initUserLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                    CameraUpdate center =
                            CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),
                                    location.getLongitude()));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

                    map.moveCamera(center);
                    map.animateCamera(zoom);
                }
            }
        });
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        LatLng chosenLoc = new LatLng(latLng.latitude, latLng.longitude);
        map.addMarker(new MarkerOptions().position(chosenLoc).title("select here"));
        mLatitude = latLng.latitude;
        mLongitude = latLng.longitude;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (mLatitude != 0 && mLongitude != 0) {
            Intent intent = new Intent();
            intent.putExtra("longitude", mLongitude);
            intent.putExtra("latitude", mLatitude);
            setResult(1, intent);
            finish();
        } else {
            ShowToast.show(this, "Please select a location");
        }

    }
}
