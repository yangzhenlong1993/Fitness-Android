package com.zhenlong.Fitness.Bean;

import android.util.Log;

public class BasicInfo  {
    private  static User currentUserInfo;

    public static User getUserInfo() {
        return currentUserInfo;
    }

    public static void setUserInfo(User userInfo) {
        currentUserInfo = userInfo;
        Log.e("本机用户基本信息以设置为", currentUserInfo.getUserid()+"");
    }
}
