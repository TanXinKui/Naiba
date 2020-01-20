package www.xinkui.com.odering.mqtt;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class MQTTState {
    private static String imei;
    private static Context context;
    private static MQTTState instance = null;
    private static Handler mHandler=new Handler();

    public static void setApplication(Context application) {
        MQTTState.context = application;
    }

    private MQTTState() {

    }
    public void onTerminate() {
        mHandler.removeCallbacksAndMessages(null);
    }
    public static MQTTState getInstance() {
        if(instance ==null){
            synchronized (MQTTState.class){
                if(instance == null){
                    instance =new MQTTState();
                }
            }
        }
        return instance;
    }

    public static String getImei(){
        if(!TextUtils.isEmpty(imei)){
            return imei;
        }
        TelephonyManager telephonyManager=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)== PackageManager.PERMISSION_GRANTED){
                if(Build.VERSION.SDK_INT>=26){
                    imei= telephonyManager.getImei();
                    return imei;
                }
                if(Build.VERSION.SDK_INT>=23){
                    int count=telephonyManager.getPhoneCount();
                    if(count>1){
                        imei=telephonyManager.getDeviceId(0);
                        return imei;
                    }else {
                        imei=telephonyManager.getDeviceId();
                        return imei;
                    }
                }else {
                    imei=telephonyManager.getDeviceId();
                    return imei;
                }

            }else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
