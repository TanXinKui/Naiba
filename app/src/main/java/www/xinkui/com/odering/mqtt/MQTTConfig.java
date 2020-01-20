package www.xinkui.com.odering.mqtt;

import android.os.Message;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import android.os.Handler;
import android.text.TextUtils;

import www.xinkui.com.odering.util.Util;


public class MQTTConfig {
    private MqttAsyncClient mqttClient = null;
    private MQTTListener mqttListener;
    private boolean isConnect = false;
    private int reconnectCount = 0;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.arg1) {
                case Util.MQTT_STATE_CONNECTED:
                    mqttListener.onConnected();
                    reconnectCount = 0;
                    Util.Loge("connected succeed");
                    break;
                case Util.MQTT_STATE_FAIL:
                    mqttListener.onFailed();
                    Util.Loge("connected failed");
                    break;
                case Util.MQTT_STATE_LOST:
                    mqttListener.onLost();
                    Util.Loge("connected lost");
                    break;
                case Util.MQTT_STATE_RECEIVE:
                    MQTTObject mqttObject = (MQTTObject) msg.obj;
                    mqttListener.onReceived(mqttObject.getTopic(), mqttObject.getMessage());
                    Util.Loge("connected received");
                    break;
                case Util.MQTT_STATE_SEND_SUCC:
                    mqttListener.onSentSuccessfully();
                    Util.Loge("connected sent Successfully");
                    break;
                default:
                    break;
            }
            return true;
        }
    });
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            isConnect = true;
            Message message = new Message();
            message.arg1 = Util.MQTT_STATE_CONNECTED;
            mHandler.sendMessage(message);
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            isConnect = false;
            Message message = new Message();
            message.arg1 = Util.MQTT_STATE_FAIL;
            mHandler.sendMessage(message);
        }
    };
    private MqttCallback mqttCallback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
            Message message = new Message();
            message.arg1 = Util.MQTT_STATE_LOST;
            mHandler.sendMessage(message);
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            Message message1 = new Message();
            message1.arg1 = Util.MQTT_STATE_RECEIVE;
            message1.obj = new MQTTObject(topic, new String(message.getPayload()));
            mHandler.sendMessage(message1);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            Message message = new Message();
            message.arg1 = Util.MQTT_STATE_SEND_SUCC;
            mHandler.sendMessage(message);
        }
    };

    public MQTTConfig(MQTTListener mqttListener) {
        this.mqttListener = mqttListener;
    }

    public void connectMQTT() {
        try {
            mqttClient = new MqttAsyncClient("tcp://" + Util.MQTT_IP + ":" + Util.MQTT_IP_PORT,
                    "ClientID" + Util.MQTT_USER_ID, new MemoryPersistence());
            mqttClient.connect(getOption(), null, iMqttActionListener);
            mqttClient.setCallback(mqttCallback);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private MqttConnectOptions getOption() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        if (!TextUtils.isEmpty(Util.MQTT_USER_ID) && !TextUtils.isEmpty(Util.MQTT_PASSWORD)) {
            mqttConnectOptions.setUserName(Util.MQTT_USER_ID);
            mqttConnectOptions.setPassword(Util.MQTT_PASSWORD.toCharArray());
        }
        mqttConnectOptions.setConnectionTimeout(10);
        mqttConnectOptions.setKeepAliveInterval(20);
        return mqttConnectOptions;
    }

    public boolean getConnect() {
        return isConnect;
    }

    public void disConnect() {
        try {
            mqttClient.disconnect();
            mqttClient = null;
            isConnect = false;
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void reconnect(){
        if(reconnectCount<5){
            disConnect();
            connectMQTT();
            reconnectCount++;
        }else {
            Util.Loge("we've tried 5 times but in vain.");
        }
    }

    public void sendMessage(String topic, String msg, int qos) {
        if (!isConnect) {
            Util.Loge("connection is closed");
            return;
        }
        try {
            mqttClient.publish(topic, msg.getBytes(), qos, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribeTopic(String topic, int qos) {
        if (!isConnect) {
            Util.Loge("connection is closed");
            return;
        }
        try {
            mqttClient.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribeTopics(String[] topic, int[] qos) {
        if (!isConnect) {
            Util.Loge("connection is closed");
            return;
        }
        try {
            mqttClient.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


}
