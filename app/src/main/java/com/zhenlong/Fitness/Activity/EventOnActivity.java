package com.zhenlong.Fitness.Activity;

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
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
import com.zhenlong.Fitness.Adapter.ParticipantsListAdapter;
import com.zhenlong.Fitness.Bean.BasicInfo;
import com.zhenlong.Fitness.Bean.Coordinate;
import com.zhenlong.Fitness.Bean.Event;
import com.zhenlong.Fitness.Bean.Msg;
import com.zhenlong.Fitness.Bean.Participants;
import com.zhenlong.Fitness.R;
import com.zhenlong.Fitness.Service.JWebSocketClient;
import com.zhenlong.Fitness.Service.JWebSocketClientService;
import com.zhenlong.Fitness.Util.DateTransfer;
import com.zhenlong.Fitness.Util.JsonTransfer;
import com.zhenlong.Fitness.Util.ShowToast;
import com.zhenlong.Fitness.Weight.PeriscopeLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventOnActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    private JWebSocketClient client;
    private Event event;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSocketClientService;
    private boolean isBound = false;
    private EventOnReceiver eventOnReceiver;
    private List<Participants> participants = new ArrayList<>();
    private ParticipantsListAdapter participantsListAdapter;
    private PeriscopeLayout periscopeLayout;
    private GoogleMap map;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            Log.e("EventOnActivity", "服务与活动绑定成功");
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSocketClientService = binder.getService();
            client = jWebSocketClientService.client;
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("EventOnActivity", "服务与活动成功断开");
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_on);
        event = (Event) getIntent().getExtras().get("selected_event");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.event_on_map);
        mapFragment.getMapAsync(this);
        bindService();
        doRegisterWeight();

        initEventStatus();
        setText();
        doRegisterReceiver();
        initParticipantsList();

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

    private void initEventStatus() {
        Date currentTime = new Date();
        Date eventStartTime = event.getStarttime();
        int eventDuration = event.getDuration();
        Calendar cal = Calendar.getInstance();
        cal.setTime(eventStartTime);
        cal.add(Calendar.MINUTE, eventDuration);
        Date eventEndTime = cal.getTime();
        if (currentTime.before(eventStartTime)) {
            startEventBtn.setText("event does not start");
            startEventBtn.setEnabled(false);
        } else if (currentTime.after(eventStartTime) && currentTime.before(eventEndTime)) {
            startEventBtn.setText("event is going on");
            startEventBtn.setEnabled(true);
        } else {
            startEventBtn.setText("event is end");
            startEventBtn.setEnabled(false);
        }
    }

    private void bindService() {
        if (isBound) {

        } else {
            Intent bindIntent = new Intent(EventOnActivity.this, JWebSocketClientService.class);
            bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
        }
    }

    private void doRegisterReceiver() {

        eventOnReceiver = new EventOnReceiver();
        IntentFilter filter = new IntentFilter("com.zhenlong.fitness.event_on");
        registerReceiver(eventOnReceiver, filter);

    }

    @Override
    protected void onDestroy() {
        if (isBound) {
            Log.e("unbinding service", serviceConnection.toString());
            unbindService(serviceConnection);
        }

        unregisterReceiver(eventOnReceiver);
        super.onDestroy();
    }

    private TextView titleTV, locationTV, startTimeTV, intervalTV, countTV, categoryTV, durationTV;
    private Button startEventBtn;
    private ListView participantsLs;

    private void doRegisterWeight() {
        startEventBtn = findViewById(R.id.event_on_start_btn);
        startEventBtn.setOnClickListener(this);
        titleTV = findViewById(R.id.event_on_title);
        locationTV = findViewById(R.id.event_on_location);
        startTimeTV = findViewById(R.id.event_on_start_time);
        intervalTV = findViewById(R.id.event_on_interval);
        countTV = findViewById(R.id.event_on_count);
        categoryTV = findViewById(R.id.event_on_category);
        durationTV = findViewById(R.id.event_on_duration);
        participantsLs = findViewById(R.id.event_on_participants);
        periscopeLayout = findViewById(R.id.event_on_periscope);
    }

    private void initParticipantsList() {
        participantsListAdapter = new ParticipantsListAdapter(this, participants);
        participantsLs.setAdapter(participantsListAdapter);
        participantsLs.setSelection(participants.size());
    }

    private void setText() {
        titleTV.setText("Title: "+event.getTitle());
        locationTV.setText("Location Name: "+event.getLocationname());
        startTimeTV.setText("Date: "+DateTransfer.dateToString(event.getStarttime()));
        intervalTV.setText("Repeat: "+event.getEventinterval().toString());
        countTV.setText("Count: "+event.getEventcount().toString());
        categoryTV.setText("Category: "+event.getCategory());
        durationTV.setText("Duration: "+event.getDuration().toString());
    }

    //need to be improved here use Handler
    @Override
    public void onClick(View v) {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Coordinate coordinate = new Coordinate();
                    coordinate.setLongitude(location.getLongitude());
                    coordinate.setLatitude(location.getLatitude());
                    jWebSocketClientService.sendMsg(buildEventOnMsg(coordinate));
                }
            }
        });


    }

    private String buildEventOnMsg(Coordinate coordinate) {
        Msg msg = new Msg();
        msg.setOperation("start_event");
        msg.setFromId(BasicInfo.getUserInfo().getUserid());
        msg.setEvent(event);
        msg.setUserLocation(coordinate);
        return JsonTransfer.MsgToJson(msg);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        initMap();
    }


    private class EventOnReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Msg msg = (Msg) intent.getExtras().get("event_on_msg");//就算不是这个key也可能不是赋予空值


            if (event.getEventid().intValue() == msg.getEvent().getEventid().intValue()) { // 防止不同event之间的广播互相干扰，必须当选中的event等于message中的event才执行代码,注意integer类型的大小比较
                if (msg.getCode() == Msg.INSERT_SUCCESS) {
                    startEventBtn.setEnabled(false);
                    startEventBtn.setText("u have joined this event");
                } else if (msg.getCode() == Msg.INSERT_FAIL) {
                    ShowToast.show(EventOnActivity.this, "your current location is far away from the destination");
                }


                if (msg.getOperation().equals("start_notification")) {
                    startEventBtn.setEnabled(true);
                    startEventBtn.setText("event is going on");
                } else if (msg.getOperation().equals("end_notification")) {
                    startEventBtn.setEnabled(false);
                    startEventBtn.setText("event end");
                } else if (msg.getOperation().equals("start_event")) {

                    participants = msg.getParticipants();
                    initParticipantsList();
                    for (int i = 0; i < 10; i++) {
                        periscopeLayout.addHeart();
                    }

                }
            }
        }
    }
}
