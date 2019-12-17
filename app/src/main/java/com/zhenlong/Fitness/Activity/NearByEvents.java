package com.zhenlong.Fitness.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.zhenlong.Fitness.Bean.BasicInfo;
import com.zhenlong.Fitness.Bean.Coordinate;
import com.zhenlong.Fitness.Bean.Event;
import com.zhenlong.Fitness.Bean.Msg;
import com.zhenlong.Fitness.R;
import com.zhenlong.Fitness.Service.JWebSocketClientService;
import com.zhenlong.Fitness.Util.JsonTransfer;

import java.util.List;

public class NearByEvents extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap map;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSocketClientService;
    private boolean isBound = false;
    private List<Event> events;
    private NearbyEventsReceiver nearbyEventsReceiver;
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
        setContentView(R.layout.activity_near_by_events);
        bindService();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.nearby_events_map);
        mapFragment.getMapAsync(this);
        initUserLocation();
        doRegisterReceiver();

    }

    private void queryNearByEvents(double longitude, double latitude) {
        Msg msg =new Msg();
        msg.setOperation("nearby_events");
        msg.setFromId(BasicInfo.getUserInfo().getUserid());
        Coordinate coordinate = new Coordinate();
        coordinate.setLongitude(longitude);
        coordinate.setLatitude(latitude);
        msg.setUserLocation(coordinate);
        jWebSocketClientService.sendMsg(JsonTransfer.MsgToJson(msg));
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
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);

                    map.moveCamera(center);
                    map.animateCamera(zoom);
                    queryNearByEvents(location.getLongitude(), location.getLatitude());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (isBound) {
            Log.e("unbinding service", serviceConnection.toString());
            unbindService(serviceConnection);
        }
        unregisterReceiver(nearbyEventsReceiver);
        super.onDestroy();
    }

    private void bindService() {
        if (isBound) {

        } else {
            Intent bindIntent = new Intent(NearByEvents.this, JWebSocketClientService.class);
            bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.setOnMarkerClickListener(this);
    }

    private void doRegisterReceiver() {
        nearbyEventsReceiver = new NearbyEventsReceiver();
        IntentFilter filter = new IntentFilter("com.zhenlong.fitness.nearby_event");
        registerReceiver(nearbyEventsReceiver, filter);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Event selectedEvent = events.get((int)marker.getTag());
        Intent intent = new Intent(this, JoinActivity.class);
        intent.putExtra("selected_event",selectedEvent);
        startActivity(intent);
        return false;
    }


    private class  NearbyEventsReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            Msg msg = (Msg) intent.getExtras().get("nearby_event");
            events = msg.getEvents();
            Log.e("the nearby events ",events.size()+"");
            for(int i =0;i<events.size();i++){
                LatLng eventLocation = new LatLng(events.get(i).getLatitude(), events.get(i).getLongtitude()
                );
                Marker marker =map.addMarker(new MarkerOptions().position(eventLocation).title(events.get(i).getTitle()));
                marker.setTag(i);

            }
        }
    }
}
