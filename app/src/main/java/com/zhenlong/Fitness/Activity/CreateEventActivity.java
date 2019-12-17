package com.zhenlong.Fitness.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zhenlong.Fitness.Bean.BasicInfo;
import com.zhenlong.Fitness.Bean.Event;
import com.zhenlong.Fitness.Bean.Msg;
import com.zhenlong.Fitness.Bean.User;
import com.zhenlong.Fitness.R;
import com.zhenlong.Fitness.Service.JWebSocketClientService;
import com.zhenlong.Fitness.Util.DateTransfer;
import com.zhenlong.Fitness.Util.JsonTransfer;
import com.zhenlong.Fitness.Util.ShowToast;

import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {

    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSocketClientService;
    private boolean isBound = false;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSocketClientService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        //注册按钮监听事件
        doRegisterWeight();
        //绑定service
        bindService();


    }

    private void bindService() {
        if (isBound) {

        } else {
            Intent bindIntent = new Intent(CreateEventActivity.this, JWebSocketClientService.class);
            bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
        }

    }

    @Override
    protected void onDestroy() {
        if (isBound) {
            Log.e("unbinding service", serviceConnection.toString());
            unbindService(serviceConnection);
        }
        super.onDestroy();
    }

    private EditText titleET;
    private Button confirmBtn, chooseLocBtn, dateBtn, timeBtn;
    private Spinner intervalSpinner, countSpinner, categorySpinner, durationSpinner;
    private static final String [] categories = {"run","climb","swim","cycling"};
    private static final Integer [] intervals = {24,48,72};
    private static final Integer [] durations = {60,120,180};
    private static final Integer [] counts = {1,2,3,4,5,6,7,8,9,10,11,12};
    private ArrayAdapter<String> categoriesAdapter;
    private ArrayAdapter<Integer> intervalAdapter, durationAdapter, countAdapter;
    private String category;
    private Integer interval,duration, count=1;
    private ImageView categoryImg;

    private void doRegisterWeight() {
        confirmBtn = findViewById(R.id.create_event_confirm_btn);
        chooseLocBtn = findViewById(R.id.create_event_choose_location);
        dateBtn = findViewById(R.id.create_event_date_picker);
        timeBtn = findViewById(R.id.create_event_time_picker);
        dateBtn.setOnClickListener(this);
        timeBtn.setOnClickListener(this);
        chooseLocBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(CreateEventActivity.this);
        titleET = findViewById(R.id.create_event_title_et);
        categoryImg = findViewById(R.id.create_event_category_img);

        intervalSpinner  =  findViewById(R.id.create_event_interval_spinner);
        countSpinner = findViewById(R.id.create_event_count_spinner);
        categorySpinner = findViewById(R.id.create_event_category_spinner);
        durationSpinner = findViewById(R.id.create_event_duration_spinner);

        categoriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        categoriesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        intervalAdapter= new ArrayAdapter<Integer>(this, R.layout.support_simple_spinner_dropdown_item, intervals);
        intervalAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        durationAdapter = new ArrayAdapter<Integer>(this, R.layout.support_simple_spinner_dropdown_item, durations);
        durationAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        countAdapter = new ArrayAdapter<Integer>(this, R.layout.support_simple_spinner_dropdown_item,counts);
        countAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        categorySpinner.setAdapter(categoriesAdapter);
        categorySpinner.setVisibility(View.VISIBLE);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = categories[position];
                switch (category){
                    case "run":
                        categoryImg.setImageResource(R.drawable.running);
                        break;
                    case "climb":
                        categoryImg.setImageResource(R.drawable.climb);
                        break;
                    case "swim":
                        categoryImg.setImageResource(R.drawable.swim);
                        break;
                    case "cycling":
                        categoryImg.setImageResource(R.drawable.cycling);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        intervalSpinner.setAdapter(intervalAdapter);
        intervalSpinner.setVisibility(View.VISIBLE);
        intervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                interval = intervals[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        durationSpinner.setAdapter(durationAdapter);
        durationSpinner.setVisibility(View.VISIBLE);
        durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                duration = durations[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        countSpinner.setAdapter(countAdapter);
        countSpinner.setVisibility(View.VISIBLE);
        countSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                count = counts[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private StringBuilder dateTime = new StringBuilder();

    @Override
    public void onClick(View v) {
        final Calendar calendar = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.create_event_confirm_btn:
                String query = buildCreateEventMsg();
                jWebSocketClientService.sendMsg(query);
                finish();
                break;
            case R.id.create_event_choose_location:
                Intent intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.create_event_date_picker:
                dateTime.setLength(0);
                DatePickerDialog dialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month = month+1;
                                dateTime.append(year + "-" + month + "-" + dayOfMonth + " ");
                                dateBtn.setText("the date is " + dateTime.toString());
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;
            case R.id.create_event_time_picker:
                if(dateTime.length()==0){
                    ShowToast.show(this,"please select date first");
                }
                else{
                    new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                            dateTime.append(hour + ":" + minute + ":00");
                            timeBtn.setText("the time is "+hour+":"+minute);
                        }
                    }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false).show();
                }


                break;
        }

    }

    private String buildCreateEventMsg() {
        String title = titleET.getText().toString();


        Event event = new Event();
        event.setTitle(title);
        event.setLocationname("melboune");
        event.setLatitude(latitude);
        event.setLongtitude(longitude);
        event.setStarttime(DateTransfer.stringToDate(dateTime.toString()));
        event.setDoneornot(0);
        event.setEventinterval(interval);
        event.setEventcount(count);
        event.setCategory(category);
        event.setDuration(1);
        Msg msg = new Msg();
        User user = BasicInfo.getUserInfo();
        msg.setFromId(user.getUserid());
        msg.setOperation("create_event");
        msg.setEvent(event);
        dateTime.setLength(0);

        return JsonTransfer.MsgToJson(msg);
    }

    private double longitude = 0, latitude = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 1) {
                longitude = data.getDoubleExtra("longitude", 0);
                latitude = data.getDoubleExtra("latitude", 0);
                chooseLocBtn.setText("the chosen location is" + latitude + " " + longitude);
            }

        }
    }

}
