package www.xinkui.com.odering.mqtt;
public interface MQTTListener {
    /**
     * connect the server successfully
     * */
    void onConnected();
    /**
     * lost from server
     * */
    void onLost();
    /**
     * fail to connect the server
     * */
    void onFailed();
    /**
     * receive message from server
     *
     * @param topic the subscribed topic from server
     * @param message the message from topic
     * */
    void onReceived(String topic, String message);
    /**
     * sent message to the server successfully
     * */
    void onSentSuccessfully();
}
