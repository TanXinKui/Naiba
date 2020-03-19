package www.xinkui.com.odering.util;

import android.util.Log;

/**
 * @author TONXOK
 * @description 用于存储数据库的相关信息
 * @time 2019/4/22 16:33
 */
public class Util {
    /**
    *数据库的账户名和密码
    *by txk
    */
    /**
     * 菜的份数
     * */
    public static int PLATES=10;
    /**
     * 按两下返回键的差值小于两秒就退出程序
     * */
    public static int INTERVAL=2000;
    /**
     * MQTT basic config parameters
     * */
    public static final String MQTT_IP ="139.199.87.26";
    public static final String MQTT_IP_PORT ="1883";
    public static final String MQTT_TOPIC ="Project/Naiba";
    public static final String MQTT_NAME ="admin";
    public static final String MQTT_USER_ID ="client2015191036";
    public static final String MQTT_PASSWORD ="admin";
    /**
     * MQTT STATUS CODE
     * */
    public final static int MQTT_STATE_CONNECTED=1;
    public final static int MQTT_STATE_LOST=2;
    public final static int MQTT_STATE_FAIL=3;
    public final static int MQTT_STATE_RECEIVE=4;
    public final static int MQTT_STATE_SEND_SUCC=5;
    private static final String tag="tonxok";
    private static final boolean DEBUG_MODE = true;
    public static void Loge(String msg){
        if(DEBUG_MODE){
            Log.e(tag,msg);
        }
    }
}
