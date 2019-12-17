package com.zhenlong.Fitness.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.zhenlong.Fitness.Adapter.MsgAdapter;
import com.zhenlong.Fitness.Bean.BasicInfo;
import com.zhenlong.Fitness.Bean.Msg;
import com.zhenlong.Fitness.Bean.User;
import com.zhenlong.Fitness.R;
import com.zhenlong.Fitness.Service.JWebSocketClientService;
import com.zhenlong.Fitness.Util.JsonTransfer;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private List<Msg> msgList = new ArrayList<>();
    private EditText inputText;
    private Button send,imgBtn;
    private ImageButton sendImgBtn, imgImgBtn;
    private RecyclerView recyclerView;
    private MsgAdapter adapter;
    private User userInChatRoom;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSocketClientService;
    private boolean isBound = false;
    private ChatRoomReceiver chatRoomReceiver;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            Log.e("JoinActivity", "服务与活动绑定成功");
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSocketClientService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("JoinActivity", "服务与活动成功断开");
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        userInChatRoom = (User) getIntent().getExtras().get("selected_user");
        bindService();
        doRegisterWeight();
        registerReceiver();
    }

    private void registerReceiver() {
        chatRoomReceiver = new ChatRoomReceiver();
        IntentFilter filter = new IntentFilter("com.zhenlong.fitness.chat_room");
        registerReceiver(chatRoomReceiver, filter);
        Log.e("EventOnActivity正在注册接收器", chatRoomReceiver.toString());
    }

    @Override
    protected void onDestroy() {
        if (isBound) {
            Log.e("unbinding service", serviceConnection.toString());
            unbindService(serviceConnection);
        }
        unregisterReceiver(chatRoomReceiver);
        super.onDestroy();
    }

    private void bindService() {
        if (isBound) {

        } else {
            Intent bindIntent = new Intent(ChatRoomActivity.this, JWebSocketClientService.class);
            bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
        }
    }

    private void doRegisterWeight() {
        sendImgBtn =  findViewById(R.id.chat_room_img);
        imgImgBtn = findViewById(R.id.chat_room_send);
        imgImgBtn.setOnClickListener(this);
        sendImgBtn.setOnClickListener(this);
        inputText = findViewById(R.id.chat_room_input_text);
        recyclerView = findViewById(R.id.chat_room_recycle_view);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutmanager);
        adapter = new MsgAdapter(msgList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chat_room_send:
                String shortMsg = inputText.getText().toString();
                Msg sentMsg = buildShortMsg(shortMsg,null);
                msgList.add(sentMsg);
                jWebSocketClientService.sendMsg(JsonTransfer.MsgToJson(sentMsg));
                adapter.notifyItemInserted(msgList.size()-1);
                recyclerView.scrollToPosition(msgList.size()-1);
                inputText.setText("");
                break;
            case R.id.chat_room_img:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                break;
        }

    }

    private Msg buildShortMsg(String shortMsg, Bitmap map) {
        Msg sentMsg = new Msg();
        sentMsg.setFromId(BasicInfo.getUserInfo().getUserid());
        sentMsg.setShortMessage(shortMsg);
        sentMsg.setToId(userInChatRoom.getUserid());
        sentMsg.setOperation("send_msg_to_user");
        sentMsg.setType(Msg.TYPE_SENT);
        if(map!=null){
            sentMsg.setImg(getBitmapData(map));
        }else {
            sentMsg.setImg(null);
        }
        return sentMsg;
    }

    private Handler mHandler = null;

    private class ChatRoomReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Msg receivedmMsg = (Msg) intent.getExtras().get("chat_msg");
            receivedmMsg.setType(Msg.TYPE_RECEIVED);
            msgList.add(receivedmMsg);
            adapter.notifyItemInserted(msgList.size()-1);
            recyclerView.scrollToPosition(msgList.size()-1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Msg imgquery = buildShortMsg(null, imageBitmap);
            msgList.add(imgquery);
            jWebSocketClientService.sendMsg(JsonTransfer.MsgToJson(imgquery));
            adapter.notifyItemInserted(msgList.size()-1);
            recyclerView.scrollToPosition(msgList.size()-1);
            inputText.setText("");
        }
    }

    private byte[] getBitmapData(Bitmap photo) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }
}
