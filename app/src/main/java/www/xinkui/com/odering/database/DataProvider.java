package www.xinkui.com.odering.database;

import android.util.Log;

import java.util.ArrayList;

import io.reactivex.annotations.Nullable;
import www.xinkui.com.odering.util.Util;

/**
 * @author TONXOK
 * @description 用于存储点餐的信息
 * @time 2019/5/6 20:25
 */
public class DataProvider {
    @Nullable
    private static volatile DataProvider INSTANCE;
    private static volatile ArrayList<Integer> data = new ArrayList<>();
    private static String username = null;

    //int[] ItemTitle2 = new int[]{13, 11, 20, 14, 15,13, 10, 11, 14};

    private DataProvider() {
    }

    public static synchronized DataProvider getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataProvider();
        }
        return INSTANCE;
    }

    public ArrayList<Integer> getData() {
        return data;
    }

    public synchronized void setData(ArrayList<Integer> newData) {
        data = null;
        data = newData;
    }

    public String getUsername() {
        return username;
    }

    public synchronized void setUsername(String username1) {
        username = username1;
    }

    public synchronized void initData() {
        data.clear();
        for (int i = 0; i < Util.PLATES; i++) {
            data.add(0);
        }
        Log.v(this.getClass().getName(), "DataProvider initiate Data...completed!"+data.size());
    }

    public synchronized void setSpecificPlate(int index, int num) {
        data.set(index, num);
        countTotalPay();
    }

    public synchronized void subtractSpecificPlate(int index) {
        int subtractOrigin = data.get(index) - 1;
        data.set(index, subtractOrigin >= 0 ? subtractOrigin : 0);
    }

    public synchronized void addSpecificPlate(int index) {
        int addOrigin = data.get(index) + 1;
        data.set(index, addOrigin);
        countTotalPay();
    }

    private synchronized void setTotalPay(int totalPay) {
        data.set(Util.PLATES - 1, totalPay);
        //countTotalPay();这个错误不删，留给自己反省。。。
    }

    public synchronized void countTotalPay() {
        Log.v(this.getClass().getName(), "countTotalPay...."+data.size());
        int total = data.get(0) * 13 + data.get(1) * 11 + data.get(2) * 20 + data.get(3) * 14 + data.get(4) * 15 + data.get(5) * 13
                + data.get(6) * 10 + data.get(7) * 11 + data.get(8) * 14;
        setTotalPay(total);

    }

    public synchronized int getTotalPay() {
        return data.get(Util.PLATES - 1);
    }

    public synchronized void clearPlates() {
        for (int i = 0; i < Util.PLATES; i++) {
            data.set(i, 0);
        }
        setTotalPay(0);
    }
    public void showCurrentData(){
        Log.v("tanxinkui",data.size()+"sizeOfData");
        if(data.size()>0) {
            for (int i = 0; i < data.size(); i++) {
                Log.v("tanxinkui", data.get(i) + "Data"+i);
            }
        }
    }
}
