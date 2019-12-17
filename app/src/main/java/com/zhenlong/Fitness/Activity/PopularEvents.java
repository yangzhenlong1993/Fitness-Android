package com.zhenlong.Fitness.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yalantis.phoenix.PullToRefreshView;
import com.zhenlong.Fitness.Adapter.EventListAdapter;
import com.zhenlong.Fitness.Bean.BasicInfo;
import com.zhenlong.Fitness.Bean.Event;
import com.zhenlong.Fitness.Bean.Msg;
import com.zhenlong.Fitness.R;
import com.zhenlong.Fitness.Service.JWebSocketClientService;
import com.zhenlong.Fitness.Util.JsonTransfer;

import java.util.ArrayList;
import java.util.List;

public class PopularEvents extends AppCompatActivity implements AdapterView.OnItemClickListener, PullToRefreshView.OnRefreshListener {
    private PullToRefreshView pullToRefreshView;
    private PEventsReceiver pEventsReceiver;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSocketClientService;
    private List<Event> pEvents = new ArrayList<>();
    private EventListAdapter eventListAdapter;
    private ListView pListView;

    private boolean isBound = false;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSocketClientService = binder.getService();
            jWebSocketClientService.sendMsg(buildGetPopularEventsMsg());

            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    private String buildGetPopularEventsMsg() {
        Msg msg = new Msg();
        msg.setFromId(BasicInfo.getUserInfo().getUserid());
        msg.setOperation("popular_events");

        return JsonTransfer.MsgToJson(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_events);
        pListView = findViewById(R.id.popular_event_ls);
        pullToRefreshView = findViewById(R.id.popular_pull_to_refresh);
        pullToRefreshView.setOnRefreshListener(this);
        pListView.setOnItemClickListener(this);
        //绑定服务
        bindService();
        //注册广播
        doRegisterReceiver();
        initEventsList();
    }

    private void initEventsList() {
        eventListAdapter = new EventListAdapter(this, pEvents);
        pListView.setAdapter(eventListAdapter);
        pListView.setSelection(pEvents.size());
    }


    private void doRegisterReceiver() {
        pEventsReceiver = new PEventsReceiver();
        IntentFilter filter = new IntentFilter("com.zhenlong.fitness.popular_events");
        registerReceiver(pEventsReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        if (isBound) {
            Log.e("unbinding service", serviceConnection.toString());
            unbindService(serviceConnection);
        }
        //解绑广播接收器
        unregisterReceiver(pEventsReceiver);
        super.onDestroy();
    }

    private void bindService() {
        if (isBound) {

        } else {
            Intent bindIntent = new Intent(PopularEvents.this, JWebSocketClientService.class);
            bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onRefresh() {
        pullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                String query = buildGetPopularEventsMsg();
                jWebSocketClientService.sendMsg(query);
                pullToRefreshView.setRefreshing(false);
            }
        }, 100);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Event selectedEvent = (Event) pListView.getItemAtPosition(position);
        Intent intent = new Intent(this, JoinActivity.class);
        intent.putExtra("selected_event", selectedEvent);
        startActivity(intent);
    }

    private class PEventsReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Msg msg = (Msg) intent.getExtras().get("popular_events");
            pEvents = msg.getEvents();
            initEventsList();
        }
    }
}
