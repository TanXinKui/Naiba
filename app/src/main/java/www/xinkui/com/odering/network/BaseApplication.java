package www.xinkui.com.odering.network;

import android.app.Application;
/**
*@description
*@author TONXOK
*@time 2019/5/3 15:41
*/
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetWorkManager.getInstance().init();
    }
}
