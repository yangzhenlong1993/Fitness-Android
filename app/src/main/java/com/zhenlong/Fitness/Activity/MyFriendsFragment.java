package com.zhenlong.Fitness.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import com.yalantis.phoenix.PullToRefreshView;
import com.zhenlong.Fitness.Adapter.UserListAdapter;
import com.zhenlong.Fitness.Bean.BasicInfo;
import com.zhenlong.Fitness.Bean.Msg;
import com.zhenlong.Fitness.Bean.User;
import com.zhenlong.Fitness.R;
import com.zhenlong.Fitness.Service.JWebSocketClientService;
import com.zhenlong.Fitness.Util.JsonTransfer;

import java.util.ArrayList;
import java.util.List;

public class MyFriendsFragment extends Fragment implements AdapterView.OnItemClickListener, PullToRefreshView.OnRefreshListener {
    private PullToRefreshView pullToRefreshView;
    private JWebSocketClientService jWebSocketClientService;
    private ListView friendsLs;
    private List<User> friends = new ArrayList<>();
    private Animation animation;
    private LayoutAnimationController controller;
    private UserListAdapter userListAdapter;
    private SearchFriendsReceiver searchFriendsReceiver;
    public MyFriendsFragment(JWebSocketClientService jWebSocketClientService) {
        this.jWebSocketClientService = jWebSocketClientService;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_friends, container, false);
        friendsLs = view.findViewById(R.id.my_friends_ls);
        animation = AnimationUtils.loadAnimation(getActivity(),R.anim.anim_item);
        controller = new LayoutAnimationController(animation);
        controller.setDelay(0.5f);
        friendsLs.setLayoutAnimation(controller);
        friendsLs.setOnItemClickListener(this);
        pullToRefreshView = view.findViewById(R.id.my_friends_pull_to_refresh);
        pullToRefreshView.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        doRegisterReceiver();
        String query =  buildSearchMyFriendsQuery();
        jWebSocketClientService.sendMsg(query);
        initList();
    }

    private void doRegisterReceiver() {
        searchFriendsReceiver = new SearchFriendsReceiver();
        IntentFilter filter = new IntentFilter("com.zhenlong.fitness.my_friends");
        getActivity().registerReceiver(searchFriendsReceiver, filter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User selectedUser = (User) friendsLs.getItemAtPosition(position);
        Intent intent  = new Intent(getActivity(), ChatRoomActivity.class);
        intent.putExtra("selected_user", selectedUser);
        startActivity(intent);
    }

   private void initList(){
       userListAdapter = new UserListAdapter(getActivity(),friends);
       friendsLs.setAdapter(userListAdapter);
       friendsLs.setSelection(friends.size());
    }

    @Override
    public void onRefresh() {
        pullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
             String query =  buildSearchMyFriendsQuery();
                jWebSocketClientService.sendMsg(query);
                pullToRefreshView.setRefreshing(false);
            }
        }, 100);
    }

    private String buildSearchMyFriendsQuery() {
        Msg msg = new Msg();
        msg.setOperation("search_my_friend");
        msg.setFromId(BasicInfo.getUserInfo().getUserid());
        return JsonTransfer.MsgToJson(msg);
    }

    private class SearchFriendsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Msg msg = (Msg) intent.getExtras().get("search_friends_results");
                friends = null;
                friends = msg.getUsers();
                initList();
        }
    }
}
