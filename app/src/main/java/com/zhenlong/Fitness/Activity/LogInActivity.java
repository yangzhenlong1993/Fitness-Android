package com.zhenlong.Fitness.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.zhenlong.Fitness.Bean.BasicInfo;
import com.zhenlong.Fitness.Bean.Msg;
import com.zhenlong.Fitness.Bean.User;
import com.zhenlong.Fitness.R;
import com.zhenlong.Fitness.Service.LoginAndSignUpService;
import com.zhenlong.Fitness.Util.ShowToast;
import com.zhenlong.Fitness.Weight.CustomVideoView;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loginBtn, signUpBtn;
    private EditText usernameET, passwordET;
    private CustomVideoView customVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        //注册按钮监听事件
        doRegisterOnClickListener();
    }

    private void doRegisterOnClickListener() {
        customVideoView = findViewById(R.id.log_in_video);
        loginBtn = findViewById(R.id.log_in_login_btn);
        signUpBtn = findViewById(R.id.log_in_signup_btn);
        usernameET = findViewById(R.id.log_in_username_et);
        passwordET = findViewById(R.id.log_in_password_et);
        loginBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        customVideoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video));
        customVideoView.start();
        customVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                customVideoView.start();
            }
        });
    }

    @Override
    protected void onRestart() {
        doRegisterOnClickListener();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        customVideoView.stopPlayback();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.log_in_login_btn:
                String username = usernameET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                boolean result = buildLoginMsg(username, password);

                if (result) {
                    ShowToast.show(this, "Login Successfully");
                    startMenuActivity();
                } else {
                    ShowToast.show(this, "Login Failed");
                }
                break;
            case R.id.log_in_signup_btn:
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }

    //如果执行成功开始执行跳转
    private void startMenuActivity() {
        Intent intent = new Intent(LogInActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean buildLoginMsg(String username, String password) {
        User loginUser = new User();
        loginUser.setUsername(username);
        loginUser.setPassword(password);
        Msg msg = new Msg();
        msg.setUserInfo(loginUser);
        msg.setOperation("login");
        msg = new LoginAndSignUpService().getLoginResult(msg);
        if(msg.getCode() == Msg.SELECT_SUCCESS){

            BasicInfo.setUserInfo(msg.getUserInfo());
            return true;
        } else{
            return false;
        }

    }
}
