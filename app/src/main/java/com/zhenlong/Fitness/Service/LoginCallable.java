package com.zhenlong.Fitness.Service;

import com.zhenlong.Fitness.Bean.Msg;

import java.util.concurrent.Callable;

class LoginCallable implements Callable<Msg> {

    private Msg msg;

    public LoginCallable(Msg msg) {
        this.msg = msg;
    }

    @Override
    public Msg call() {
        msg = HttpRequest.buildURLConnection(msg);
       return msg;

    }
}
