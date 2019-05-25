package www.xinkui.com.odering.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import www.xinkui.com.odering.bean.DishState;
import www.xinkui.com.odering.bean.SubInf;
import www.xinkui.com.odering.network.NetWorkManager;
import www.xinkui.com.odering.network.response.ResponseTransformer;
import www.xinkui.com.odering.network.schedulers.SchedulerProvider;

/**
 * Created by lenovo on 2018/2/6.
 */

/**
 * @author TONXOK
 * @description
 * @time 2019/4/28 10:25
 */
public class SqlQuery extends Service {
    private static final String TAG = "MyService";
    Boolean finishState = false;
    int[] paystate = new int[6];
    private ArrayList<DishState> dishStatesList = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "--------->onCreate: ");
    }

    public ArrayList<DishState> getDishStatesList() {
        return dishStatesList;
    }

    public void setDishStatesList(ArrayList<DishState> dishStatesList) {
        this.dishStatesList.clear();
        this.dishStatesList = dishStatesList;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        myTimer();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (finishState) {
                    finishState = false;
                    myTimer();
                }//-------设定要指定任务--------
            }
        }, 1000, 1000);
        SubInf menu2 = new SubInf();
        menu2.setMenu(111, 2, 3, 40, 5, 60, 77, 80, 90);
        int s[] = menu2.getMenu();
        for (int i = 1; i < s.length; i++) {
            Log.v("mytxk1", "" + s[i]);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "--------->onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "--------->onDestroy: ");
        super.onDestroy();
    }

    //返回有无订单状态
    public void setPaystate(int[] paystate) {
        this.paystate = paystate;
    }

    public int[] getPaystate() {
        return paystate;
    }


    /**
     * 设置定时器
     * */
    public void myTimer() {

        NetWorkManager.getRequest().getDishState()
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Observer<ArrayList<DishState>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<DishState> dishStates) {
                        setDishStatesList(dishStates);
                        Log.v("yzy", "success to connect!");
                        finishState = true;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
