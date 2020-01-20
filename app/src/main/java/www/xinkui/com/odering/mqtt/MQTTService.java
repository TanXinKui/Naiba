package www.xinkui.com.odering.mqtt;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import www.xinkui.com.odering.R;
import www.xinkui.com.odering.util.Util;
/**
*@author T O N X O K
*@date 2020/1/20 17:44
*/
public class MQTTService extends Service implements MQTTListener{
    private static final int MESSAGE_CHECK=0;
    private static List<MQTTListener> mMqttListenerList=new ArrayList<>();
    private Timer timer=new Timer(true);
    private  Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==MESSAGE_CHECK){
                if(mqttConfig!=null&&!mqttConfig.getConnect()){
                    mqttConfig.connectMQTT();
                }
            }
        }
    };
    private class CheckMqttThread  extends TimerTask {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(MESSAGE_CHECK);
        }
    }
    private CheckMqttThread mqttThread;
    private static MQTTConfig mqttConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        mqttConfig=new MQTTConfig(this);
        mqttConfig.connectMQTT();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mqttThread!=null){
            mqttThread = new CheckMqttThread();
            timer.scheduleAtFixedRate(mqttThread,2000,10000);
        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            startForeground(1,getNotification());
        }
        return Service.START_STICKY;

    }
    private Notification getNotification(){
        Notification notification=null;
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel("mqtt","mqttTest",NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
            notification =new Notification.Builder(getApplicationContext(),"mqtt").build();
        }
        else {
            Notification.Builder builder=new Notification.Builder(this);
            builder.setSmallIcon(R.drawable.applogo);
            builder.setContentTitle("mqttTst");
            builder.setContentText("mqtt context");
            notification = builder.build();
        }
        return notification;
    }
    public static void addMqttListener(MQTTListener listener){
        if (!mMqttListenerList.contains(listener)) {
            mMqttListenerList.add(listener);
        }
    }

    public static void removeMqttListener(MQTTListener listener){
        mMqttListenerList.remove(listener);
    }

    public static MQTTConfig getMqttConfig() {
        return mqttConfig;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected() {
        Util.Loge("connected mqtt service");
        if(mqttConfig!=null){
            mqttConfig.subscribeTopic(Util.MQTT_TOPIC,0);
        }
        for(MQTTListener mqttListener:mMqttListenerList){
            mqttListener.onConnected();
        }
    }

    @Override
    public void onLost() {
        Util.Loge("lost mqtt service");
        if(mqttConfig!=null) {
            mqttConfig.reconnect();
        }
        for(MQTTListener mqttListener:mMqttListenerList){
            mqttListener.onLost();
        }
    }

    @Override
    public void onFailed() {
        Util.Loge("failed mqtt service");
        for(MQTTListener mqttListener:mMqttListenerList){
            mqttListener.onFailed();
        }
    }

    @Override
    public void onReceived(String topic, String message) {
        Util.Loge("received mqtt service,topic:"+topic+" message:"+message);
        for(MQTTListener mqttListener:mMqttListenerList){
            mqttListener.onReceived(topic,message);
        }
    }

    @Override
    public void onSentSuccessfully() {
        Util.Loge("sendSuccessfully mqtt service");
        for(MQTTListener mqttListener:mMqttListenerList){
            mqttListener.onSentSuccessfully();
        }
    }
}
