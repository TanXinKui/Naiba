package www.xinkui.com.odering.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import www.xinkui.com.odering.R;
import www.xinkui.com.odering.bean.Comment;
import www.xinkui.com.odering.bean.DishState;
import www.xinkui.com.odering.bean.Menu;
import www.xinkui.com.odering.bean.Transcation;
import www.xinkui.com.odering.database.DataProvider;
import www.xinkui.com.odering.network.NetWorkManager;
import www.xinkui.com.odering.network.response.ResponseTransformer;
import www.xinkui.com.odering.network.schedulers.SchedulerProvider;
import www.xinkui.com.odering.service.SqlQuery;
import www.xinkui.com.odering.util.Util;

import static java.lang.Thread.sleep;

/**
 * Created by lenovo on 2017/11/2.
 */

/**
 * @author TONXOK
 * @description
 * @time 2019/4/28 10:56
 */
public class OrderList extends Activity {
    String vendorphone;
    private long firstTime = 0;
    int balance, clientNum0, currentDesk, remainbalance;
    Button confirmback, vendorPhonebtn;
    RelativeLayout commentRl;
    TextView paystate;
    TextView clientNum1;
    TextView desknum3;
    Timer timer;
    Boolean finishState = false;
    Handler myhandler1;
    Runnable updateThread;

    //选中的餐桌号
    TextView desknumSelected;
    int currentState;
    Spinner sp1, sp2;
    ArrayAdapter<String> ad1;
    ArrayList<String> desknum;
    SqlQuery sql1;
    Handler myHandler;
    private Button btn1;
    private Button btn3;
    private TextView sumup3;
    private RelativeLayout relativeLayout_comment;
    private Button sumb1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentDesk = -1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderlist);
        initiateRunnable();
        initViews();
        sqlsearch();
    }

    private void initViews() {
        desknumSelected = (TextView) findViewById(R.id.desknum);
        vendorPhonebtn = (Button) findViewById(R.id.vendorPhone);
        vendorPhonebtn.setVisibility(View.INVISIBLE);
        commentRl = (RelativeLayout) findViewById(R.id.commem1);
        commentRl.setVisibility(View.INVISIBLE);
        btn1 = (Button) this.findViewById(R.id.orderlist_bottom_left);
        btn3 = (Button) this.findViewById(R.id.orderlist_bottom_right);
        vendorPhonebtn.setOnClickListener(v -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + vendorphone));
            dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(dialIntent);
        });
        btn1.setOnClickListener(v -> {
                    if (currentState == -1) {
                        Toast.makeText(OrderList.this, "当前订单正在运行中，请勿修改！", Toast.LENGTH_SHORT).show();
                    } else {
                        timer.cancel();
                        Intent intent = new Intent(OrderList.this, Main.class);
                        startActivity(intent);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        overridePendingTransition(0, 0);
                    }

                }
        );
        btn3.setOnClickListener(v -> {
                    if (currentState == -1) {
                        Toast.makeText(OrderList.this, "当前订单正在运行中，请保持在此页面！", Toast.LENGTH_SHORT).show();
                    } else {
                        timer.cancel();
                        Intent intent = new Intent(OrderList.this, ClientInfo.class);
                        startActivity(intent);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        overridePendingTransition(0, 0);
                    }
                }
        );
        myhandler1.post(updateThread);
        myHandler = new Handler();
        paystate = (TextView) findViewById(R.id.paystate);
        //启动service后台刷新
        sql1 = new SqlQuery();
        sql1.onStart(new Intent(), 1);
        sumup3 = (TextView) findViewById(R.id.sumup3);
        desknum3 = (TextView) findViewById(R.id.desknum);
        confirmback = (Button) findViewById(R.id.confirmback);
        relativeLayout_comment = (RelativeLayout) findViewById(R.id.commem1);
        clientNum1 = (TextView) findViewById(R.id.clientNum);
        sumb1 = (Button) findViewById(R.id.sumb);
        sp1 = (Spinner) findViewById(R.id.sp1);
        sp2 = (Spinner) findViewById(R.id.sp2);
        desknum = new ArrayList<String>();
        desknum.add(" ");
        desknum.add("1");
        desknum.add("2");
        desknum.add("3");
        desknum.add("4");
        desknum.add("5");
        desknum.add("6");
        ad1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, desknum);
        sp1.setAdapter(ad1);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                desknum3.setText(String.valueOf(i));
                currentDesk = i - 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final ArrayList<String> clientNum = new ArrayList<String>();
        clientNum.add("");
        clientNum.add("1");
        clientNum.add("2");
        clientNum.add("3");
        clientNum.add("4");
        clientNum.add("5");
        clientNum.add("6");
        clientNum.add("7");
        clientNum.add("8");
        ArrayAdapter<String> ad2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, clientNum);
        sp2.setAdapter(ad2);
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                clientNum1.setText("" + i);
                clientNum0 = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sumb1.setOnClickListener(v -> {
                    Toast.makeText(OrderList.this, "评论已经发送", Toast.LENGTH_SHORT).show();
                    sumbcomment();
                    commentRl.setVisibility(View.INVISIBLE);
                }
        );
        String[] ItemTitle = new String[]{"黑椒牛肉饼条饭+营养滋补炖汤", "香菇滑鸡饭+营养滋补炖汤", "NB照烧鸡腿饭+营养滋补炖汤", "奥尔良鸡排饭+营养滋补炖汤", "椒盐排骨饭+营养滋补炖汤",
                "黑椒鸡排焗饭", "黑椒牛肉焗饭", "咖喱海鲜焗饭", "咖喱鸡排焗饭"};
        int[] ItemTitle2 = new int[]{13, 11, 20, 14, 15, 13, 10, 11, 14};
        sumup3.setText("" + DataProvider.getInstance().getTotalPay());
        balance = DataProvider.getInstance().getTotalPay();
        if (DataProvider.getInstance().getTotalPay() != 0) {
            RelativeLayout l1 = (RelativeLayout) findViewById(R.id.orderlist_end2);
            EditText comm = (EditText) findViewById(R.id.commen2);
            Button sumb = (Button) findViewById(R.id.sumb);
            l1.setVisibility(View.VISIBLE);
            sumb.setVisibility(View.VISIBLE);
            comm.setVisibility(View.VISIBLE);
        } else {
            EditText comm = (EditText) findViewById(R.id.commen2);
            Button sumb = (Button) findViewById(R.id.sumb);
            RelativeLayout l1 = (RelativeLayout) findViewById(R.id.orderlist_end2);
            l1.setVisibility(View.INVISIBLE);
            sumb.setVisibility(View.INVISIBLE);
            comm.setVisibility(View.INVISIBLE);
        }

        ListView list = (ListView) findViewById(R.id.list_oder);
        ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
        for (int j = 0; j < ItemTitle.length; j++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            if (DataProvider.getInstance().getData().get(j) != 0) {
                map.put("ItemTitle", ItemTitle[j]);
                map.put("ItemTitle1", "x" + DataProvider.getInstance().getData().get(j));
                map.put("ItemTitle2", "￥" + ItemTitle2[j]);
                mylist.add(map);
            }
        }
        SimpleAdapter simpleA = new SimpleAdapter(
                this,
                mylist,
                R.layout.forlistoder,
                new String[]{"ItemTitle", "ItemTitle1", "ItemTitle2"},
                new int[]{R.id.foro1, R.id.foro2, R.id.foro3}
        );
        list.setAdapter(simpleA);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (currentDesk != -1) {
                    int realCurrentDesk = currentDesk + 1;
                    if (sql1.getDishStatesList().get(currentDesk).getConfirmState() == 1 &&
                            sql1.getDishStatesList().get(currentDesk).getState() == 1) {
                        try {
                            setSate();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        myshow(realCurrentDesk);
                    } else if (sql1.getDishStatesList().get(currentDesk).getConfirmState() == -1 &&
                            sql1.getDishStatesList().get(currentDesk).getState() == 1) {
                        try {
                            setSate1();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        myshow(realCurrentDesk);
                    } else if (sql1.getDishStatesList().get(currentDesk).getConfirmState() == 2 &&
                            sql1.getDishStatesList().get(currentDesk).getState() == 1) {
                        try {
                            setSate2();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        myshow(realCurrentDesk);
                    }
                }
            }
        }, 500, 500);
    }

    private void initiateRunnable() {
        updateThread = new Runnable() {
            @Override
            public void run() {
                Message msg = myhandler1.obtainMessage();
                msg.arg1 = 0;
                if (finishState) {
                    myhandler1.removeCallbacks(updateThread);
                } else {
                    myhandler1.sendMessage(msg);
                }
            }
        };
        myhandler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                myhandler1.post(updateThread);
            }
        };
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > Util.INTERVAL) {
                Toast.makeText(OrderList.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
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

    public void setSate() {
        Runnable myrunnable = new Runnable() {
            @Override
            public void run() {
                TextView paystate1 = (TextView) findViewById(R.id.paystate);
                paystate1.setText("商家已接单");
            }
        };
        myHandler.post(myrunnable);
    }

    public void setSate1() {
        Runnable myrunnable = new Runnable() {
            @Override
            public void run() {
                TextView paystate1 = (TextView) findViewById(R.id.paystate);
                paystate1.setText("商家拒绝接单");
                currentState = 0;
                sp1.setVisibility(View.VISIBLE);
                sp2.setVisibility(View.VISIBLE);
                clientNum1.setVisibility(View.INVISIBLE);
                desknum3.setVisibility(View.INVISIBLE);
                confirmback.setVisibility(View.VISIBLE);
                vendorPhonebtn.setVisibility(View.INVISIBLE);
            }
        };
        myHandler.post(myrunnable);
    }

    public void setSate2() {
        Runnable myrunnable = new Runnable() {
            @Override
            public void run() {
                TextView paystate1 = (TextView) findViewById(R.id.paystate);
                paystate1.setText("订单已完成,可继续订单或评论");
                currentState = 0;
                sp1.setVisibility(View.VISIBLE);
                sp2.setVisibility(View.VISIBLE);
                clientNum1.setVisibility(View.INVISIBLE);
                desknum3.setVisibility(View.INVISIBLE);
                //confirmback.setVisibility(View.VISIBLE);
                vendorPhonebtn.setVisibility(View.INVISIBLE);
                commentRl.setVisibility(View.VISIBLE);
            }
        };
        myHandler.post(myrunnable);
    }

    public void myshow(final int desknum) {
        //设置noticification
        Intent i = new Intent();
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        Notification notification = new Notification();
        Notification.Builder builder = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("一条未读信息")
                .setContentText("商家回复！")
                .setContentIntent(pi)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setWhen(System.currentTimeMillis())
                .setOngoing(true);
        notification = builder.getNotification();
        notification.defaults = Notification.DEFAULT_SOUND;
        NotificationManager noma = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        noma.notify(0, notification);
        setStateManage(desknum);
    }

    private void setStateManage(int deskNum) {
        NetWorkManager.getRequest().setStateManage(deskNum,2)
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe();
    }

    public void sumbcomment() {
        EditText commen2 = (EditText) findViewById(R.id.commen2);
        Date day = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String mydate = df.format(day);
        Comment comment = new Comment(commen2.getText().toString(), mydate, DataProvider.getInstance().getUsername(), 0);
        NetWorkManager.getRequest().submitComment(comment)
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe();
    }

    /**
     * 提交订单 与btn的onSubcommen联系
     */

    public void onSubcommen(View view) {
        if (finishState) {
            if (remainbalance < balance) {
                Toast.makeText(OrderList.this, "余额不足，请充值！", Toast.LENGTH_SHORT).show();
            } else if (desknumSelected.getText().toString().equals(" ") || desknumSelected.getText().toString().equals("0") || clientNum0 == 0) {
                Toast.makeText(OrderList.this, "请填完整餐桌号和用餐人数！", Toast.LENGTH_SHORT).show();
            } else {
                confirmback.setVisibility(View.INVISIBLE);
                paystate.setText("已支付，等待接单(请保持在当前页面)");
                currentState = -1;
                sp1.setVisibility(View.INVISIBLE);
                sp2.setVisibility(View.INVISIBLE);
                clientNum1.setVisibility(View.VISIBLE);
                desknum3.setVisibility(View.VISIBLE);
                vendorPhonebtn.setVisibility(View.VISIBLE);
                commentRl.setVisibility(View.INVISIBLE);
                int t1 = DataProvider.getInstance().getData().get(0);
                int t2 = DataProvider.getInstance().getData().get(1);
                int t3 = DataProvider.getInstance().getData().get(2);
                int t4 = DataProvider.getInstance().getData().get(3);
                int t5 = DataProvider.getInstance().getData().get(4);
                int t6 = DataProvider.getInstance().getData().get(5);
                int t7 = DataProvider.getInstance().getData().get(6);
                int t8 = DataProvider.getInstance().getData().get(7);
                int t9 = DataProvider.getInstance().getData().get(8);
                Menu menu = new Menu(t1, t2, t3, t4, t5, t6, t7, t8, t9);
                NetWorkManager.getRequest().transcation(new Transcation(menu, balance, DataProvider.getInstance().getUsername(),
                        Integer.valueOf(((TextView) findViewById(R.id.desknum)).getText().toString()), clientNum0))
                        .compose(ResponseTransformer.handleResult())
                        .compose(SchedulerProvider.getInstance().applySchedulers())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(String s) {
                                vendorphone = s;
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        } else {
            Toast.makeText(OrderList.this, "系统繁忙！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取余额，后续才进行相应的支付操作，annotation:如果没有足够的金额不能继续
     */
    public void sqlsearch() {
        NetWorkManager.getRequest().showBalance(DataProvider.getInstance().getUsername())
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Observer<Integer>() {
                               @Override
                               public void onSubscribe(Disposable d) {

                               }

                               @Override
                               public void onNext(Integer integer) {
                                   remainbalance = integer;
                               }

                               @Override
                               public void onError(Throwable e) {

                               }

                               @Override
                               public void onComplete() {
                                   finishState = true;
                               }
                           }
                );
    }
}
