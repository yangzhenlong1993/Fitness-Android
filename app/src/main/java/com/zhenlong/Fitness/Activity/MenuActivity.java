package com.zhenlong.Fitness.Activity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.zhenlong.Fitness.Bean.BasicInfo;
import com.zhenlong.Fitness.R;
import com.zhenlong.Fitness.Service.JWebSocketClient;
import com.zhenlong.Fitness.Service.JWebSocketClientService;


public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_CODE = 101;
    private JWebSocketClient client;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSocketClientService;
    private boolean mBound = false;
    private LinearLayout linearHome, linearSearch, linearFriends, linearMe;
    private ImageButton imgHome, imgSearch, imgFriends, imgMe;
    private FragmentTransaction ftr;
    private Fragment homepageFrag, searchFrag, myFriendsFrag, meFrag;
    private TextView homeTV, searchTV, friendsTV, meTV;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("MenuActivity", "服务与活动绑定成功");
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSocketClientService = binder.getService();
            client = jWebSocketClientService.client;
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("MenuActivity", "服务与活动成功断开");
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //开启service，只需开启一次
        startJWebSClientService();
        //绑定service
        bindService();
        //注册按钮监听事件
        doRegisterOnClickListener();
        setSelected(0);
        //启用notification channel
        createNotificationChannel();
        //检测是否启用位置服务
        checkLocationService();

    }

    private void setSelected(int i) {
        FragmentManager fm = getSupportFragmentManager();
        ftr = fm.beginTransaction();
        hideTransaction(ftr);

        switch (i) {
            case 0:
                if (homepageFrag == null) {
                    homepageFrag = new HomePageFragment();
                    ftr.add(R.id.menu_fragment, homepageFrag);
                }
                ftr.show(homepageFrag);
                imgHome.setImageResource(R.drawable.baseline_home_red_18dp);
                homeTV.setTextSize(10);
                break;
            case 1:
                if (searchFrag == null) {
                    searchFrag = new SearchFragment(jWebSocketClientService);
                    ftr.add(R.id.menu_fragment, searchFrag);
                }
                ftr.show(searchFrag);
                imgSearch.setImageResource(R.drawable.baseline_search_red_18dp);
                searchTV.setTextSize(10);
                break;
            case 2:
                if (myFriendsFrag == null) {
                    myFriendsFrag = new MyFriendsFragment(jWebSocketClientService);
                    ftr.add(R.id.menu_fragment, myFriendsFrag);
                }
                ftr.show(myFriendsFrag);
                imgFriends.setImageResource(R.drawable.baseline_supervisor_account_red_18dp);
                friendsTV.setTextSize(10);
                break;
            case 3:
                if(meFrag ==null){
                    meFrag = new MeFragment(jWebSocketClientService);
                    ftr.add(R.id.menu_fragment,meFrag);
                }
                ftr.show(meFrag);
                imgMe.setImageResource(R.drawable.baseline_account_circle_red_18dp);
                meTV.setTextSize(10);
                break;
        }
        ftr.commit();
    }


    private void hideTransaction(FragmentTransaction ftr) {
        if (homepageFrag != null) {
            ftr.hide(homepageFrag);
        }
        if (searchFrag != null) {
            ftr.hide(searchFrag);
        }
        if (myFriendsFrag != null) {
            ftr.hide(myFriendsFrag);
        }
        if(meFrag!=null){
            ftr.hide(meFrag);
        }
    }


    private void checkLocationService() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED & ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }
    }


    @Override
    protected void onDestroy() {
        if (mBound) {
            Log.e("unbinding service", serviceConnection.toString());
            unbindService(serviceConnection);
        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        moveTaskToBack(true);
    }


    private void doRegisterOnClickListener() {
        linearHome = findViewById(R.id.linear_home);
        linearSearch = findViewById(R.id.linear_search);
        linearFriends = findViewById(R.id.linear_friends);
        linearMe = findViewById(R.id.linear_me);
        linearHome.setOnClickListener(this);
        linearSearch.setOnClickListener(this);
        linearFriends.setOnClickListener(this);
        linearMe.setOnClickListener(this);

        imgHome = findViewById(R.id.img_home);
        imgSearch = findViewById(R.id.img_search);
        imgFriends = findViewById(R.id.img_friends);
        imgMe = findViewById(R.id.img_me);

        homeTV = findViewById(R.id.menu_button_tv_home);
        searchTV = findViewById(R.id.menu_button_tv_search);
        friendsTV = findViewById(R.id.menu_button_tv_friends);
        meTV = findViewById(R.id.menu_button_tv_me);

    }


    private void startJWebSClientService() {
        Intent intent = new Intent(MenuActivity.this, JWebSocketClientService.class);
        intent.putExtra("uid", BasicInfo.getUserInfo().getUserid());
        startService(intent);
    }

    private void bindService() {
        if (mBound) {

        } else {
            Intent bindIntent = new Intent(MenuActivity.this, JWebSocketClientService.class);
            bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
        }

    }

    @Override
    public void onClick(View view) {
        resetImg();
        switch (view.getId()) {
            case R.id.linear_home:
                setSelected(0);
                break;
            case R.id.linear_search:
                setSelected(1);
                break;
            case R.id.linear_friends:
                setSelected(2);
                break;
            case R.id.linear_me:
                setSelected(3);
                break;
        }

    }

    private void resetImg() {
        imgHome.setImageResource(R.drawable.baseline_home_black_18dp);
        imgSearch.setImageResource(R.drawable.baseline_search_black_18dp);
        imgFriends.setImageResource(R.drawable.baseline_supervisor_account_black_18dp);
        imgMe.setImageResource(R.drawable.baseline_account_circle_black_18dp);

        homeTV.setTextSize(0);
        searchTV.setTextSize(0);
        friendsTV.setTextSize(0);
        meTV.setTextSize(0);
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "event channel";
            String description = "this is event notification channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID_EVENT", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
