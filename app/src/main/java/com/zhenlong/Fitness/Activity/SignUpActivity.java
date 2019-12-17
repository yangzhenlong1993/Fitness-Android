package com.zhenlong.Fitness.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.zhenlong.Fitness.Bean.Msg;
import com.zhenlong.Fitness.Bean.User;
import com.zhenlong.Fitness.R;
import com.zhenlong.Fitness.Service.LoginAndSignUpService;
import com.zhenlong.Fitness.Util.ShowToast;
import com.zhenlong.Fitness.Weight.CustomVideoView;

import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameET, passwordET, heightET, weightET;
    private Spinner genderSpinner;
    private static final String [] genders = {"male","female"};
    private ArrayAdapter<String> genderAdapter;
    private Integer gender, height, weight;
    private String username,password;
    private Button signupBtn;
    private CustomVideoView customVideoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initWeight();
    }

    private void initWeight(){
        customVideoView = findViewById(R.id.sign_up_video);
        usernameET  = findViewById(R.id.sign_up_username_et);
        passwordET = findViewById(R.id.sign_up_password_et);
        genderSpinner = findViewById(R.id.sign_up_gender);
        heightET = findViewById(R.id.sign_up_height_et);
        weightET = findViewById(R.id.sign_up_weight_et);
        genderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genders);
        genderSpinner.setVisibility(View.VISIBLE);
        genderSpinner.setAdapter(genderAdapter);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(genders[position].equals("male")){
                    gender = 0;
                } else {
                    gender = 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        signupBtn = findViewById(R.id.sign_up_signup_btn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameET.getText().toString().trim();
                password  = passwordET.getText().toString().trim();
                height = Integer.parseInt(heightET.getText().toString().trim());
                weight = Integer.parseInt(weightET.getText().toString().trim());
                buildSignUpMsg();
            }
        });
        customVideoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video));
        customVideoView.start();
        customVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                customVideoView.start();
            }
        });

    }

    private boolean buildSignUpMsg() {
        User signupUser = new User();
        signupUser.setUsername(username);
        signupUser.setPassword(password);
        signupUser.setGender(gender);
        signupUser.setBirthday(new Date());
        signupUser.setHeight(height);
        signupUser.setWeight(weight);
        signupUser.setLevel(1);
        Msg msg = new Msg();
        msg.setUserInfo(signupUser);
        msg.setOperation("signup");
        msg = new LoginAndSignUpService().getSignUpResult(msg);
        if(msg.getCode() == Msg.INSERT_SUCCESS){
            finish();
            return true;
        } else{
            ShowToast.show(this,"already existed username");
            return false;
        }

    }
}
