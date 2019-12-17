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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.zhenlong.Fitness.Adapter.EventListAdapter;
import com.zhenlong.Fitness.Bean.BasicInfo;
import com.zhenlong.Fitness.Bean.Event;
import com.zhenlong.Fitness.Bean.Msg;
import com.zhenlong.Fitness.R;
import com.zhenlong.Fitness.Service.JWebSocketClientService;
import com.zhenlong.Fitness.Util.JsonTransfer;

import java.util.ArrayList;
import java.util.List;

public class SearchEventActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSocketClientService;
    private boolean isBound = false;
    private SearchEventsReceiver searchEventsReceiver;
    private List<Event> events = new ArrayList<>();
    private EventListAdapter eventListAdapter;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            Log.e("SearchEventActivity", "服务与活动绑定成功");
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSocketClientService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("SearchEventActivity", "服务与活动成功断开");
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_event);
        doRegisterWeight();
        //绑定service
        bindService();
        //注册广播
        doRegisterReceiver();
        //初始化listview
        initEventsList();

    }

    private void initEventsList() {
        eventListAdapter = new EventListAdapter(this, events);
        eventListView.setAdapter(eventListAdapter);
        eventListView.setSelection(events.size());
    }

    private void bindService() {
        if (isBound) {

        } else {
            Intent bindIntent = new Intent(SearchEventActivity.this, JWebSocketClientService.class);
            bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onDestroy() {
        //解绑服务链接
        if (isBound) {
            Log.e("unbinding service", serviceConnection.toString());
            unbindService(serviceConnection);
        }
        //解绑广播接收器
        unregisterReceiver(searchEventsReceiver);
        super.onDestroy();
    }

    private EditText searchEventET;
    private ListView eventListView;

    private void doRegisterWeight() {
        Button searchEventBtn = findViewById(R.id.search_event_search_btn);
        searchEventBtn.setOnClickListener(SearchEventActivity.this);
        searchEventET = findViewById(R.id.search_event_keyword_et);
        eventListView = findViewById(R.id.search_event_result_ls);
        eventListView.setOnItemClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String keyword = searchEventET.getText().toString().trim();
        String query = buildSearchEventMsg(keyword);
        jWebSocketClientService.sendMsg(query);
    }

    private String buildSearchEventMsg(String keyword) {

        Msg msg = new Msg();
        msg.setFromId(BasicInfo.getUserInfo().getUserid());
        msg.setOperation("search_event");

        Event event = new Event();
        event.setTitle(keyword);
        msg.setEvent(event);

        return JsonTransfer.MsgToJson(msg);
    }

    /**
     * 动态注册广播
     */
    private void doRegisterReceiver() {
        searchEventsReceiver = new SearchEventsReceiver();
        IntentFilter filter = new IntentFilter("com.zhenlong.fitness.search_event");
        registerReceiver(searchEventsReceiver, filter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Event selectedEvent = (Event) eventListView.getItemAtPosition(position);
        Intent intent = new Intent(SearchEventActivity.this, JoinActivity.class);
        intent.putExtra("selected_event", selectedEvent);
        startActivity(intent);
    }


    private class SearchEventsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Msg msg = (Msg) intent.getExtras().get("search_event_results");
            events = msg.getEvents();
            initEventsList();
        }
    }
}
