package com.zhenlong.Fitness.Util;

import android.content.Context;
import android.widget.Toast;

public class ShowToast {
    public static void show(Context ctx, String content){
        Toast.makeText(ctx,content,Toast.LENGTH_LONG).show();
    }
}
