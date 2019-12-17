package com.zhenlong.Fitness.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zhenlong.Fitness.Bean.BasicInfo;
import com.zhenlong.Fitness.Bean.Msg;
import com.zhenlong.Fitness.R;
import com.zhenlong.Fitness.Service.JWebSocketClientService;
import com.zhenlong.Fitness.Util.JsonTransfer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MeFragment extends Fragment{
    private TextView usernameTV,birthdayTV, heightTV, weightTV, genderTV;
    private ImageView levelImg;
    private JWebSocketClientService jWebSocketClientService;
    private MyProfileReceiver myProfileReceiver;

    public MeFragment(JWebSocketClientService jWebSocketClientService){
        this.jWebSocketClientService = jWebSocketClientService;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me,container, false);
        usernameTV = view.findViewById(R.id.me_username);
        levelImg = view.findViewById(R.id.me_level);
        birthdayTV  = view.findViewById(R.id.me_birthday);
        heightTV = view.findViewById(R.id.me_height);
        weightTV = view.findViewById(R.id.me_weight);
        genderTV = view.findViewById(R.id.me_gender);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        doRegisterReceiver();
        String query = buildMyProfileMsg();
        jWebSocketClientService.sendMsg(query);

    }

    private String buildMyProfileMsg() {
        Msg msg = new Msg();
        msg.setFromId(BasicInfo.getUserInfo().getUserid());
        msg.setOperation("my_profile");
        return JsonTransfer.MsgToJson(msg);
    }

    private void doRegisterReceiver() {
        myProfileReceiver = new MyProfileReceiver();
        IntentFilter filter = new IntentFilter("com.zhenlong.fitness.my_profile");
        getActivity().registerReceiver(myProfileReceiver, filter);
    }

    private class MyProfileReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Msg msg = (Msg) intent.getExtras().get("my_profile");
            usernameTV.setText(msg.getUserInfo().getUsername().toString());
            switch (msg.getUserInfo().getLevel()){
                case 1:
                    levelImg.setImageResource(R.drawable.profile_level_one);

                    break;
                case 2:
                    levelImg.setImageResource(R.drawable.profile_level_two);
                    break;
                case 3:
                    levelImg.setImageResource(R.drawable.profile_level_three);
                    break;
                case 4:
                    levelImg.setImageResource(R.drawable.profile_level_four);
                    break;
                case 5:
                    levelImg.setImageResource(R.drawable.profile_level_five);
                    break;
            }
            if(msg.getUserInfo().getGender().intValue()==0){
                genderTV.setText("Male");
            }else{
                genderTV.setText("Female");
            }
            birthdayTV.setText(getBirthday(msg.getUserInfo().getBirthday()));
            heightTV.setText(msg.getUserInfo().getHeight().toString());
            weightTV.setText(msg.getUserInfo().getWeight().toString());

        }
    }
    private String getBirthday(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = sdf.format(date);
        return startTime;
    }
}
