package www.xinkui.com.odering.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import www.xinkui.com.odering.R;
import www.xinkui.com.odering.bean.ComInf;
import www.xinkui.com.odering.bean.Comment;
import www.xinkui.com.odering.bean.User;
import www.xinkui.com.odering.network.NetWorkManager;
import www.xinkui.com.odering.network.exception.ApiException;
import www.xinkui.com.odering.network.response.ResponseTransformer;
import www.xinkui.com.odering.network.schedulers.SchedulerProvider;
import www.xinkui.com.odering.util.Util;

import static java.lang.Thread.sleep;

/**
 * Created by lenovo on 2018/1/14.
 */

/**
 * @author TONXOK
 * @description
 * @time 2019/4/28 10:31
 */
public class CommentList extends Activity {
    private long firstTime = 0;
    Boolean finishState = false, finishState1 = false;
    SimpleAdapter simpleA;
    ComInf comInf2 = new ComInf();
    Button backbtn1, refreshbtn;
    SchedulerProvider schedulerProvider = SchedulerProvider.getInstance();
    private ArrayList<Comment> ctList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forcommen);
        backbtn1 = (Button) findViewById(R.id.backbtn1);
        refreshbtn = (Button) findViewById(R.id.searchbtn1);
        backbtn1.setOnClickListener(v -> {
                    Toast.makeText(CommentList.this, "正在刷新数据！", Toast.LENGTH_SHORT).show();
                    finish();
                }
        );
        refreshbtn.setOnClickListener(v -> {
                    refreshbtn.setText("刷新中...");
                    sqlSearch();
                    Log.v("yzy", "个数" + comInf2.getCount1());
                }
        );
        sqlSearch();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > Util.INTERVAL) {
                Toast.makeText(CommentList.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                this.finish();
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showList() {
        ListView list = (ListView) findViewById(R.id.list_commen);
        int count1 = comInf2.getCount1();
        ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
        for (int j = 0; j < count1; j++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("st1", ctList.get(j).getComment());
            map.put("st2", ctList.get(j).getUsername());
            map.put("st3", ctList.get(j).getTime());
            mylist.add(map);
        }
        simpleA = new SimpleAdapter(
                CommentList.this,
                mylist,
                R.layout.forlistcomment,
                new String[]{"st1", "st2", "st3"},
                new int[]{R.id.fc2, R.id.fc3, R.id.fc4}
        );
        if (count1 < 21) {
            list.setAdapter(simpleA);
        }
        refreshbtn.setText("点击刷新");
    }

    public void sqlSearch() {
        NetWorkManager.getRequest().getComment()
                .compose(ResponseTransformer.handleResult())
                .compose(schedulerProvider.applySchedulers())
                .subscribe(new Observer<ArrayList<Comment>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<Comment> comments) {
                        ctList = comments;
                        finishState = true;
                        comInf2.setCount1(comments.size());
                        showList();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ApiException ex = (ApiException) e;
                        Log.v(this.getClass().getName(), ex.getDisplayMessage());
                        Toast.makeText(CommentList.this, this.getClass().getName() + ex.getDisplayMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(CommentList.this, "刷新成功！", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}


