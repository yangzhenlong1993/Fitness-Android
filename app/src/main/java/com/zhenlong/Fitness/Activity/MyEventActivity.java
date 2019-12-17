package com.zhenlong.Fitness.Activity;

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

import androidx.appcompat.app.AppCompatActivity;

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

public class MyEventActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,PullToRefreshView.OnRefreshListener {
    private PullToRefreshView pullToRefreshView;
    private SearchMyEventsReceiver searchMyEventsReceiver;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSocketClientService;
    private List<Event> mEvents = new ArrayList<>();
    private EventListAdapter eventListAdapter;


 private boolean isBound = false;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSocketClientService = binder.getService();
            jWebSocketClientService.sendMsg(buildGetMyEventsMsg());
            initEventsList();
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
        setContentView(R.layout.activity_my_event);
        //注册组件
        doRegisterWeight();
        //绑定服务
        bindService();
        //注册广播
        doRegisterReceiver();
        //初始化列表视图
        initEventsList();
    }

    private void bindService() {
        if (isBound) {

        } else {
            Intent bindIntent = new Intent(MyEventActivity.this, JWebSocketClientService.class);
            bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onDestroy() {
        if (isBound) {
            Log.e("unbinding service", serviceConnection.toString());
            unbindService(serviceConnection);
        }
        //解绑广播接收器
        unregisterReceiver(searchMyEventsReceiver);
        super.onDestroy();
    }

private ListView mEventsLs;

    private void doRegisterWeight() {
        mEventsLs = findViewById(R.id.my_event_ls);
        mEventsLs.setOnItemClickListener(this);
        pullToRefreshView = findViewById(R.id.my_event_pull_to_refresh);
        pullToRefreshView.setOnRefreshListener(this);


    }

    private void initEventsList(){
        eventListAdapter = new EventListAdapter(this, mEvents);
        mEventsLs.setAdapter(eventListAdapter);
        mEventsLs.setSelection(mEvents.size());


    }


    private String buildGetMyEventsMsg() {

        Msg msg = new Msg();
        msg.setFromId(BasicInfo.getUserInfo().getUserid());
        msg.setOperation("search_my_event");

        return JsonTransfer.MsgToJson(msg);
    }
    /**
     * 动态注册广播
     */
    private void doRegisterReceiver() {
        searchMyEventsReceiver = new SearchMyEventsReceiver();
        IntentFilter filter = new IntentFilter("com.zhenlong.fitness.search_my_event");
        registerReceiver(searchMyEventsReceiver, filter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Event selectedEvent = (Event) mEventsLs.getItemAtPosition(position);
        Intent intent = new Intent(MyEventActivity.this, EventOnActivity.class);
        intent.putExtra("selected_event", selectedEvent);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        pullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                String query = buildGetMyEventsMsg();
                jWebSocketClientService.sendMsg(query);
                pullToRefreshView.setRefreshing(false);
            }
        }, 100);
    }

    private class SearchMyEventsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Msg msg = (Msg) intent.getExtras().get("search_my_event_results");
            mEvents = msg.getEvents();
            Log.e("search_my_event",mEvents.size()+"项");
            initEventsList();
        }
    }
}
