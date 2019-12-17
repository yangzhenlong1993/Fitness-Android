package com.zhenlong.Fitness.Activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.zhenlong.Fitness.Adapter.EventListAdapter;
import com.zhenlong.Fitness.Adapter.UserListAdapter;
import com.zhenlong.Fitness.Bean.BasicInfo;
import com.zhenlong.Fitness.Bean.Event;
import com.zhenlong.Fitness.Bean.Msg;
import com.zhenlong.Fitness.Bean.User;
import com.zhenlong.Fitness.R;
import com.zhenlong.Fitness.Service.JWebSocketClientService;
import com.zhenlong.Fitness.Util.JsonTransfer;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements FloatingSearchView.OnMenuItemClickListener, FloatingSearchView.OnQueryChangeListener, AdapterView.OnItemClickListener {
    private ListView listView;
    private FloatingSearchView floatingSearchView;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSocketClientService;
    private boolean isBound = false;
    private SearchEventReceiver searchEventsReceiver;
    private List<Event> events = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private EventListAdapter eventListAdapter;
    private UserListAdapter userListAdapter;

    private Animation animation;

    private LayoutAnimationController controller;

    public SearchFragment(JWebSocketClientService jWebSocketClientService) {
        this.jWebSocketClientService = jWebSocketClientService;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        listView = view.findViewById(R.id.search_event_result_list_view);
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_item);
        controller = new LayoutAnimationController(animation);
        controller.setDelay(0.5f);
        listView.setOnItemClickListener(this);

        floatingSearchView = view.findViewById(R.id.search_event_keyword_fsv);
        floatingSearchView.setOnMenuItemClickListener(this);
        floatingSearchView.setOnQueryChangeListener(this);
        return view;
    }

    private void initEventListView() {
        listView.setLayoutAnimation(controller);
        eventListAdapter = new EventListAdapter(getActivity(), events);
        listView.setAdapter(eventListAdapter);
        listView.setSelection(events.size());
    }

    private void initUserListView() {
        listView.setLayoutAnimation(controller);
        userListAdapter = new UserListAdapter(getActivity(),users);
        listView.setAdapter(userListAdapter);
        listView.setSelection(users.size());
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //绑定service
        doRegisterReceiver();
        //初始化listview


    }


    private void doRegisterReceiver() {
        searchEventsReceiver = new SearchEventReceiver();
        IntentFilter filter = new IntentFilter("com.zhenlong.fitness.search_event");
        getActivity().registerReceiver(searchEventsReceiver, filter);
    }

    private static int currentOperation =0;
    @Override
    public void onActionMenuItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_events:
                floatingSearchView.setSearchHint("Searching Events...");
                currentOperation=0;
                break;
            case R.id.action_friends:
                floatingSearchView.setSearchHint("Searching Friends...");
                currentOperation = 1;
                break;
        }
    }


    @Override
    public void onSearchTextChanged(String oldQuery, String newQuery) {
        if(currentOperation==0){
            jWebSocketClientService.sendMsg(buildSearchEventMsg(newQuery));
        }
        else if(currentOperation==1){
            jWebSocketClientService.sendMsg(buildSearchUserMsg(newQuery));
        }

    }

    private String buildSearchUserMsg(String keyword) {
        Msg msg =new Msg();
        msg.setFromId(BasicInfo.getUserInfo().getUserid());
        User user = new User();
        user.setUsername(keyword);
        msg.setUserInfo(user);
        msg.setOperation("search_users");
        return JsonTransfer.MsgToJson(msg);
    }

    private String buildSearchEventMsg(String keyword) {
        Msg msg =new Msg();
        msg.setFromId(BasicInfo.getUserInfo().getUserid());
        Event event = new Event();
        event.setTitle(keyword);
        msg.setEvent(event);
        msg.setOperation("search_event");
        return JsonTransfer.MsgToJson(msg);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getAdapter() instanceof EventListAdapter){
            Event selectedEvent = (Event) listView.getItemAtPosition(position);
            Intent intent = new Intent(getActivity(), JoinActivity.class);
            intent.putExtra("selected_event", selectedEvent);
            startActivity(intent);
        } else  if(parent.getAdapter() instanceof UserListAdapter){
            User selectedUser = (User) listView.getItemAtPosition(position);
            String query = buildAddFriend(selectedUser);
            showNormalDialog(query);
        }

    }

    private String buildAddFriend(User selectedUser) {
        Msg msg = new Msg();
        msg.setOperation("add_friend");
        msg.setFromId(BasicInfo.getUserInfo().getUserid());
        msg.setToId(selectedUser.getUserid());
        return JsonTransfer.MsgToJson(msg);
    }

    private class SearchEventReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Msg msg = (Msg) intent.getExtras().get("search_event_results");
            if (msg.getOperation().equals("search_event")) {
                events = null;
                events = msg.getEvents();
                initEventListView();
            } else if (msg.getOperation().equals("search_users")) {
                users = null;
                users = msg.getUsers();
               initUserListView();
            }

        }
    }

    private void showNormalDialog(String query){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getActivity());

        normalDialog.setTitle("Add Friends?");
        normalDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        jWebSocketClientService.sendMsg(query);
                    }
                });
        normalDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }
}
