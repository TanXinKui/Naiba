package www.xinkui.com.odering.bean;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by lenovo on 2018/2/10.
 */
/**
*@description
*@author TONXOK
*@time 2019/4/28 10:25
*/
public class ComInf extends Activity {
    private int count1 = 21;
    String[] st1 = new String[count1];
    String[] st2 = new String[count1];
    String[] st3 = new String[count1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setCount1(int i) {
        this.count1 = i;
    }

    public int getCount1() {
        return count1;
    }

    public String[] getSt1() {
        return st1;
    }

    public void setSt1(String st[]) {
        this.st1 = st;
    }

    public String[] getSt2() {
        return st2;
    }

    public void setSt2(String st[]) {
        this.st2 = st;
    }

    public String[] getSt3() {
        return st3;
    }

    public void setSt3(String st[]) {
        this.st3 = st;
    }
}
