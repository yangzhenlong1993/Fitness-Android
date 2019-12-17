package com.zhenlong.Fitness.Service;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.zhenlong.Fitness.Activity.EventOnActivity;
import com.zhenlong.Fitness.Bean.Msg;
import com.zhenlong.Fitness.R;
import com.zhenlong.Fitness.Util.JsonTransfer;
import com.zhenlong.Fitness.Util.ShowToast;

import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class JWebSocketClientService extends Service {
    private int uid;
    public JWebSocketClient client;
    private JWebSocketClientBinder mBinder = new JWebSocketClientBinder();
    private final static int GRAY_SERVICE_ID = 1001;


    public JWebSocketClientService() {
    }


    //灰色唤醒方式
    public static class GrayInnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(GRAY_SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    PowerManager.WakeLock wakeLock;

    //获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
    @SuppressLint("InvalidWakeLockTag")
    private void acquireWakeLock() {
        if (null == wakeLock) {
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "PostLocationService");
            if (null != wakeLock) {
                wakeLock.acquire();
            }
        }
    }

    public class JWebSocketClientBinder extends Binder {
        public JWebSocketClientService getService() {
            return JWebSocketClientService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        Log.e("web socket service", "open");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        uid = intent.getIntExtra("uid", -1);

        initSocketClient();
        sHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测

        acquireWakeLock();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("web socket service", "is shut down");
        closeConnect();
        super.onDestroy();
    }

    //初始化websocket链接，并在此分发消息
    private void initSocketClient() {

        URI uri = URI.create("ws://172.20.10.5:8080/mobile_project/websocket?uid=" + uid);
        client = new JWebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                Log.e("JWebSocketClientService", "recieved message in Socket：" + message);

              final  Msg msgFromServer = JsonTransfer.JsonToMsg(message);

                Intent broadcastIntent = new Intent();
                int code = msgFromServer.getCode();

                switch (msgFromServer.getOperation()) {
                    case "popular_events":
                        broadcastIntent.setAction("com.zhenlong.fitness.popular_events");
                        broadcastIntent.putExtra("popular_events", msgFromServer);
                        sendBroadcast(broadcastIntent);
                        break;
                    case "nearby_events":
                        broadcastIntent.setAction("com.zhenlong.fitness.nearby_event");
                        broadcastIntent.putExtra("nearby_event", msgFromServer);
                        sendBroadcast(broadcastIntent);
                        break;
                    case "my_profile":
                        broadcastIntent.setAction("com.zhenlong.fitness.my_profile");
                        broadcastIntent.putExtra("my_profile", msgFromServer);
                        sendBroadcast(broadcastIntent);
                        break;
                    case "notify_notification":
                        checkLockAndShowNotification(msgFromServer);
                        break;
                    case "start_notification":
                    case "end_notification":
                        checkLockAndShowNotification(msgFromServer);
                        broadcastIntent.setAction("com.zhenlong.fitness.event_on");
                        broadcastIntent.putExtra("event_on_msg", msgFromServer);
                        sendBroadcast(broadcastIntent);
                        break;
                    case "search_event":
                    case "search_users":

                        broadcastIntent.setAction("com.zhenlong.fitness.search_event");
                        broadcastIntent.putExtra("search_event_results", msgFromServer);
                        sendBroadcast(broadcastIntent);
                        break;
                    case "search_my_event":

                        broadcastIntent.setAction("com.zhenlong.fitness.search_my_event");
                        broadcastIntent.putExtra("search_my_event_results", msgFromServer);
                        sendBroadcast(broadcastIntent);
                        break;
                    case "join_event":
                        if (code == Msg.INSERT_SUCCESS) {
                            handleToast(code, "join event successfully");
                        } else {
                            handleToast(code, "cannot join the same event twice");
                        }
                        break;
                    case "start_event":

                                broadcastIntent.setAction("com.zhenlong.fitness.event_on");
                                broadcastIntent.putExtra("event_on_msg", msgFromServer);
                                sendBroadcast(broadcastIntent);
                        break;
                    case "search_my_friend":

                        broadcastIntent.setAction("com.zhenlong.fitness.my_friends");
                        broadcastIntent.putExtra("search_friends_results", msgFromServer);
                        sendBroadcast(broadcastIntent);
                        break;
                    case "send_msg_to_user":
                        broadcastIntent.setAction("com.zhenlong.fitness.chat_room");
                        broadcastIntent.putExtra("chat_msg", msgFromServer);
                        sendBroadcast(broadcastIntent);
                        break;
                }
            }


            @Override
            public void onOpen(ServerHandshake handshakedata) {
                super.onOpen(handshakedata);
            }
        };
        connect();
    }

    private void handleToast(int code, String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        ;
        handler.post(new Runnable() {
            @Override
            public void run() {
                ShowToast.show(getApplication(), message);
            }
        });

    }


    /**
     * 检查锁屏状态，如果锁屏先点亮屏幕
     *
     * @param message
     */
    private void checkLockAndShowNotification(Msg message) {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguardManager.isKeyguardLocked()) {//是否锁屏
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!powerManager.isInteractive()) {
                PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, "TestWebSocketService:bright");
                wakeLock.acquire();//点亮屏幕
                wakeLock.release();//任务结束后释放
            }
            sendNotification(message);
        } else {
            sendNotification(message);
        }
    }

    private void sendNotification(Msg message) {
        int eventId = message.getEvent().getEventid();
        Intent intent = new Intent();

        intent.putExtra("selected_event", message.getEvent());
        intent.putExtra("msg_operation", message.getOperation());
        intent.setClass(this, EventOnActivity.class);//必须在传值之后，非常重要，否则stack不生效
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(eventId, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID_EVENT")
                .setContentTitle(message.getOperation())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(eventId, builder.build());

//        startForeground(0, builder.build());//将发送notification的服务设置为前台
    }

    /**
     * 连接websocket
     */
    private void connect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
                    client.connectBlocking();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 发送消息
     *
     * @param msg
     */
    public void sendMsg(String msg) {
        if (client != null) {
            Log.e("JWebSocketClientService", "发送的消息：" + msg);
            client.send(msg);
        }
    }

    /**
     * 断开连接
     */
    private void closeConnect() {
        try {
            if (null != client) {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client = null;
        }
    }


    // ---------------------------------------------------web socket 心跳检测
    private static final long HEART_BEAT_RATE = 3 * 1000;//每隔3秒进行一次对长链接的心跳检测
    private Handler sHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (client != null) {
                if (client.isClosed()) {
                    reconnectWs();
                }
            } else {
                //若client已空，重新初始化链接
                client = null;
                initSocketClient();
            }
            sHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    private void reconnectWs() {
        sHandler.removeCallbacks(heartBeatRunnable);
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.e("JWebSocketClientService", "开启重连");
                    client.reconnectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}