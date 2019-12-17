package com.zhenlong.Fitness.Activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zhenlong.Fitness.Bean.BasicInfo;
import com.zhenlong.Fitness.Bean.Event;
import com.zhenlong.Fitness.Bean.Msg;
import com.zhenlong.Fitness.R;
import com.zhenlong.Fitness.Service.JWebSocketClientService;
import com.zhenlong.Fitness.Util.DateTransfer;
import com.zhenlong.Fitness.Util.JsonTransfer;

public class JoinActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    private GoogleMap map;
    private Event event;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSocketClientService;
    private boolean isBound = false;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {

            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSocketClientService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            isBound = false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        event = (Event) getIntent().getExtras().get("selected_event");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.activity_join_map);
        mapFragment.getMapAsync(this);
        bindService();
        doRegisterWeight();
        initText();

    }

    private void initMap() {
      LatLng eventLocation =   new LatLng(event.getLatitude(),event.getLongtitude()
        );
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(eventLocation);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
        map.moveCamera(center);
        map.animateCamera(zoom);
        map.addMarker(new MarkerOptions().position(eventLocation).title(event.getTitle()));

    }

    private void bindService() {
        if (isBound) {

        } else {
            Intent bindIntent = new Intent(JoinActivity.this, JWebSocketClientService.class);
            bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onDestroy() {
        if (isBound) {
            Log.e("unbinding service", serviceConnection.toString());
            unbindService(serviceConnection);
        }

        super.onDestroy();
    }

    private void initText() {
        titleTV.setText(event.getTitle());
        locationTV.setText(event.getLocationname());
        startTimeTV.setText(DateTransfer.dateToString(event.getStarttime()));
        intervalTV.setText(event.getEventinterval().toString());
        countTV.setText(event.getEventcount().toString());
        categoryTV.setText(event.getCategory());
        durationTV.setText(event.getDuration().toString());
    }

    private TextView titleTV, locationTV, startTimeTV, intervalTV, countTV, categoryTV, durationTV;

    private void doRegisterWeight() {
        Button joinBtn = findViewById(R.id.join_confirm_btn);
        joinBtn.setOnClickListener(this);
        titleTV = findViewById(R.id.join_event_title);
        locationTV = findViewById(R.id.join_event_location);
        startTimeTV = findViewById(R.id.join_event_start_time);
        intervalTV = findViewById(R.id.join_event_interval);
        countTV = findViewById(R.id.join_event_count);
        categoryTV = findViewById(R.id.join_event_category);
        durationTV = findViewById(R.id.join_event_duration);
    }

    @Override
    public void onClick(View v) {
        String query =  buildJoinMsg();
        jWebSocketClientService.sendMsg(query);
    }

    private String buildJoinMsg() {

        Msg msg = new Msg();
        msg.setOperation("join_event");
        msg.setFromId(BasicInfo.getUserInfo().getUserid());
        msg.setEvent(event);
        return JsonTransfer.MsgToJson(msg);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        initMap();
    }
}
