package com.zhenlong.Fitness.Service;

import com.zhenlong.Fitness.Bean.Msg;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * LoginService :check valid user
 *
 * SignUpService register user info
 *
 */
public class LoginAndSignUpService {

    private final ExecutorService pool = Executors.newFixedThreadPool(2);

    //返回登陆结果
    public Msg getLoginResult(Msg msg){
        LoginCallable loginCallable  = new LoginCallable(msg);
        Future<Msg> loginFuture = pool.submit(loginCallable);
        Msg result = null;
        try {
            result = loginFuture.get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();

        }
       return result;
    }

    public Msg getSignUpResult(Msg msg){
        SignUpCallable signUpCallable = new SignUpCallable(msg);
        Future<Msg> signUpFuture = pool.submit(signUpCallable);
        Msg result = null;
        try {
            result = signUpFuture.get();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

        }
        return result;
    }

}
