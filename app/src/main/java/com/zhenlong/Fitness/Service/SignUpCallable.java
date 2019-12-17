package com.zhenlong.Fitness.Service;

import com.zhenlong.Fitness.Bean.Msg;

import java.util.concurrent.Callable;

public class SignUpCallable implements Callable<Msg> {
    private Msg msg;

    public SignUpCallable(Msg msg){
        this.msg=msg;
    }
    @Override
    public Msg call() throws Exception {
        msg = HttpRequest.buildURLConnection(msg);
        return msg;
    }
}
