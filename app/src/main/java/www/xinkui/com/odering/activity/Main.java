package www.xinkui.com.odering.activity;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import www.xinkui.com.odering.R;
import www.xinkui.com.odering.bean.Advertisement;
import www.xinkui.com.odering.bean.DishState;
import www.xinkui.com.odering.bean.SellState;
import www.xinkui.com.odering.bean.User;
import www.xinkui.com.odering.database.DataProvider;
import www.xinkui.com.odering.mqtt.MQTTListener;
import www.xinkui.com.odering.mqtt.MQTTService;
import www.xinkui.com.odering.network.NetWorkManager;
import www.xinkui.com.odering.network.exception.ApiException;
import www.xinkui.com.odering.network.response.ResponseTransformer;
import www.xinkui.com.odering.network.schedulers.SchedulerProvider;
import www.xinkui.com.odering.util.Util;


/**
 * @author TONXOK
 * @description 主页
 * @time 2019/4/22 17:04
 */

public class Main extends ListActivity implements MQTTListener {
    //private  TypedArray ar;
    //ListView list;
    /***----------------adView-------------**/
    private ViewPager mViewPager;
    private LinearLayout ll_dotGroup;
    private TextView newsTitle;
    Boolean finishState = false, finishState1 = false;
    Handler myHandler;
    Runnable updateThread;
    // String[] adDescription = new String[]{"", "", "", "", "", ""};
    int totalPage = 0;
    private int[] imgResIds = new int[]{R.drawable.shangjia, R.drawable.ad1, R.drawable.ad2, R.drawable.ad3, R.drawable.ad4};
    /**
     * 获取广告图片的base64流
     * by txk
     */
    String adPic[];
    /**
     * 获取当前状态
     * by txk
     */
    int adPicState[];
    /**
     * 获取当前adName
     * by txk
     */
    String adName[] = new String[]{"", "", "", "", ""};
    /**
     * 存储5张图片
     * by txk
     */
    //private String[] textView = new String[]{"特色菜1", "特色菜2", "特色菜3", "特色菜4", "特色菜5"};
    /**
     * 存储5个目录
     * by txk
     */
    private int curIndex = 0;
    /**
     * 用来记录当前滚动的位置
     * by txk
     */
    PicsAdapter picsAdapter;
    /***----------------adView-------------**/
    private long firstTime = 0;
    Button btn2, btn3, btn4;
    TextView sumUpNum;
    Button toClear, toPay;
    TextView forListTV7;
    int[] dishState;
    int[] pics = new int[]{R.drawable.dish1, R.drawable.dish2, R.drawable.dish3, R.drawable.dish4, R.drawable.dish5, R.drawable.dish6, R.drawable.dish7,
            R.drawable.dish8, R.drawable.dish9};
    String[] ItemTitle = new String[]{"黑椒牛肉饼条饭+营养滋补炖汤", "香菇滑鸡饭+营养滋补炖汤", "NB照烧鸡腿饭+营养滋补炖汤", "奥尔良鸡排饭+营养滋补炖汤", "椒盐排骨饭+营养滋补炖汤",
            "黑椒鸡排焗饭", "黑椒牛肉焗饭", "咖喱海鲜焗饭", "咖喱鸡排焗饭"};
    /* String []ItemTitle6=new String[]{"0","0","0","0","0","0","0","0","0"};*/
    int[] ItemTitle6 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
    private ArrayList<Advertisement> adList = new ArrayList<>();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initiateRunnable();
        dishState = new int[9];
        //设置菜单ListView列表
        ListView list = (ListView) findViewById(android.R.id.list);

        ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
        final String[] ItemTitle1 = new String[]{"月售106份", "月售103份", "月售120份", "月售110份", "月售150份",
                "月售142份", "月售166份", "月售132份", "月售152份"};
        String[] ItemTitle2 = new String[]{"好评率100%", "好评率97%", "好评率98%", "好评率99%", "好评率96%",
                "好评率96%", "好评率97%", "好评率95%", "好评率98%"};
        String[] ItemTitle3 = new String[]{"厨师1", "厨师2", "厨师3", "厨师1", "厨师2",
                "厨师2", "厨师1", "厨师3", "厨师2"};
        String[] ItemTitle4 = new String[]{"老板推荐", "老板推荐", "老板推荐", "老板推荐", "老板推荐",
                "老板推荐", "老板推荐", "老板推荐", "老板推荐"};
        String[] ItemTitle5 = new String[]{"￥13", "￥11", "￥20", "￥14", "￥15",
                "￥13", "￥10", "￥11", "￥14"};
        try {
            for (int i = 0; i < ItemTitle6.length; i++) {
                ItemTitle6[i] = DataProvider.getInstance().getData().get(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("anxin", e.toString());
        }
        sumUpNum = (TextView) findViewById(R.id.sumupnum);
        Log.v(this.getClass().getName(), "Main" + DataProvider.getInstance().getData().size());
        if (DataProvider.getInstance().getData().size() != 0) {
            DataProvider.getInstance().countTotalPay();
            sumUpNum.setText(" " + DataProvider.getInstance().getData().get(Util.PLATES - 1) + " ");
        } else {
            sumUpNum.setText(" " + 0 + " ");
        }
        for (int i = 0; i < ItemTitle.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("forlistiv1", pics[i]);
            map.put("forlisttv1", ItemTitle[i]);
            map.put("forlisttv2", ItemTitle1[i]);
            map.put("forlisttv3", ItemTitle2[i]);
            map.put("forlisttv4", ItemTitle3[i]);
            map.put("forlisttv5", ItemTitle4[i]);
            map.put("forlisttv6", ItemTitle5[i]);
            map.put("forlistib1", R.drawable.addtocar1);
            map.put("forlisttv7", ItemTitle6[i]);
            map.put("forlistib2", R.drawable.addtocar);
            mylist.add(map);
        }
        SimpleAdapter simpleA = new SimpleAdapter(
                this,
                mylist,
                R.layout.forlist,
                new String[]{"forlistiv1", "forlisttv1", "forlisttv2", "forlisttv3", "forlisttv4", "forlisttv5",
                        "forlisttv6", "forlistib1", "forlisttv7", "forlistib2"},
                new int[]{R.id.forlistiv1, R.id.forlisttv1, R.id.forlisttv2, R.id.forlisttv3, R.id.forlisttv4, R.id.forlisttv5,
                        R.id.forlisttv6, R.id.forlistib1, R.id.forlisttv7, R.id.forlistib2}
        );
        list.setAdapter(simpleA);
        //设置页面间的跳转
        btn2 = (Button) this.findViewById(R.id.main_bottom_mid);
        btn3 = (Button) this.findViewById(R.id.main_bottom_right);
        btn4 = (Button) this.findViewById(R.id.main_1);
        btn4.setText("查看最新评论！");
        toClear = (Button) findViewById(R.id.toclear);
        toPay = (Button) findViewById(R.id.topay);
        toClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int k = 0; k < 9; k++) {
                    ItemTitle6[k] = 0;
                }
                DataProvider.getInstance().clearPlates();
                sumUpNum = (TextView) findViewById(R.id.sumupnum);
                sumUpNum.setText(" " + 0 + " ");
                ListView list = (ListView) findViewById(android.R.id.list);
                ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
                String[] ItemTitle1 = new String[]{"月售106份", "月售103份", "月售120份", "月售110份", "月售150份",
                        "月售142份", "月售166份", "月售132份", "月售152份"};
                String[] ItemTitle2 = new String[]{"好评率100%", "好评率97%", "好评率98%", "好评率99%", "好评率96%",
                        "好评率96%", "好评率97%", "好评率95%", "好评率98%"};
                String[] ItemTitle3 = new String[]{"厨师1", "厨师2", "厨师3", "厨师1", "厨师2",
                        "厨师2", "厨师1", "厨师3", "厨师2"};
                String[] ItemTitle4 = new String[]{"老板推荐", "老板推荐", "老板推荐", "老板推荐", "老板推荐",
                        "老板推荐", "老板推荐", "老板推荐", "老板推荐"};
                String[] ItemTitle5 = new String[]{"￥13", "￥11", "￥20", "￥14", "￥15",
                        "￥13", "￥10", "￥11", "￥14"};


                for (int i = 0; i < ItemTitle.length; i++) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("forlistiv1", pics[i]);
                    map.put("forlisttv1", ItemTitle[i]);
                    map.put("forlisttv2", ItemTitle1[i]);
                    map.put("forlisttv3", ItemTitle2[i]);
                    map.put("forlisttv4", ItemTitle3[i]);
                    map.put("forlisttv5", ItemTitle4[i]);
                    map.put("forlisttv6", ItemTitle5[i]);
                    map.put("forlistib1", R.drawable.addtocar1);
                    map.put("forlisttv7", ItemTitle6[i]);
                    map.put("forlistib2", R.drawable.addtocar);
                    mylist.add(map);
                }
                SimpleAdapter simpleA = new SimpleAdapter(
                        Main.this,
                        mylist,
                        R.layout.forlist,
                        new String[]{"forlistiv1", "forlisttv1", "forlisttv2", "forlisttv3", "forlisttv4", "forlisttv5",
                                "forlisttv6", "forlistib1", "forlisttv7", "forlistib2"},
                        new int[]{R.id.forlistiv1, R.id.forlisttv1, R.id.forlisttv2, R.id.forlisttv3, R.id.forlisttv4, R.id.forlisttv5,
                                R.id.forlisttv6, R.id.forlistib1, R.id.forlisttv7, R.id.forlistib2}
                );
                list.setAdapter(simpleA);
            }
        });
        toPay.setOnClickListener(v -> {
                    Intent intent = new Intent(Main.this, OrderList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    if (DataProvider.getInstance().getTotalPay() == 0) {
                        Toast.makeText(Main.this, "请点餐！", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(intent);
                        DataProvider.getInstance().showCurrentData();
                    }
                    overridePendingTransition(0, 0);
                }
        );
        btn4.setOnClickListener(v -> {
                    btn4.setText("正在刷新数据！");
                    Intent intent = new Intent(Main.this, CommentList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
        );
        btn2.setOnClickListener(v -> {
                    Intent intent = new Intent(Main.this, OrderList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    if (DataProvider.getInstance().getTotalPay() == 0) {
                        Toast.makeText(Main.this, "请点餐！", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(intent);
                        DataProvider.getInstance().showCurrentData();
                    }
                    overridePendingTransition(0, 0);
                }
        );
        btn3.setOnClickListener(v -> {
                    Intent intent = new Intent(Main.this, ClientInfo.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
        );
        //搜索菜单信息
        sqlSearch();
        adViewSearch();
        myHandler.post(updateThread);
        //connect to MQTTservice
//        MQTTService.addMqttListener(this);
//        connectMQTT();
    }

    private void connectMQTT(){
        Intent intent = new Intent(Main.this, MQTTService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    public void initiateRunnable() {
        updateThread = new Runnable() {
            @Override
            public void run() {
                Log.d("txk", "the thread is still running!*****");
                Message msg = myHandler.obtainMessage();
                msg.arg1 = 0;
                if (finishState && finishState1) {
                    setViewPager();
                    myHandler.removeCallbacks(updateThread);
                    finishState = false;
                    finishState1 = false;
                } else {
                    myHandler.sendMessage(msg);
                    Log.v("yzy", "I am finding !");
                }
                // myHandler.postDelayed(updateThread, 3000);
            }
        };
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                myHandler.post(updateThread);

            }
        };

    }

    /**
     * 以下方法用于:查询adView相关数据
     * by txk
     */

    public void adViewSearch() {
        NetWorkManager.getRequest().getAdvertisementInfo()
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Observer<ArrayList<Advertisement>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<Advertisement> advertisements) {
                        adList = advertisements;
                        totalPage = adList.size();
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

    private void setViewPager() {

        newsTitle = (TextView) findViewById(R.id.NewsTitle);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        ll_dotGroup = (LinearLayout) findViewById(R.id.dotgroup);

        picsAdapter = new PicsAdapter(); // 创建适配器
        picsAdapter.setData();
        mViewPager.setAdapter(picsAdapter); // 设置适配器
//        mViewPager.setPageTransformer(true, new ZoomOutPageTransforme());
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.addOnPageChangeListener(new MyPageChangeListener()); //设置页面切换监听器
        initPoints(totalPage); // 初始化图片小圆点
        startAutoScroll(); // 开启自动播放
    }

    @Override
    protected void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);
        l.getItemAtPosition(position);
        if (position != 9 && dishState[position] == 0) {
            Toast.makeText(Main.this, "已经售完", Toast.LENGTH_SHORT).show();
        } else if (position != 9 && dishState[position] == 1) {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ImageView img = new ImageView(this);
            img.setImageResource(pics[position]);
            ad.setView(img);
            ad.setMessage(ItemTitle[position]);
            ad.setNegativeButton("-1", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (ItemTitle6[position] > 0) {
                        ItemTitle6[position] -= 1;
                        DataProvider.getInstance().subtractSpecificPlate(position);
                        sumUpNum = (TextView) findViewById(R.id.sumupnum);
                        sumUpNum.setText(" " + DataProvider.getInstance().getTotalPay() + " ");
                        //设置菜单ListView列表
                        ListView list = (ListView) findViewById(android.R.id.list);
                        ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
                        String[] ItemTitle1 = new String[]{"月售106份", "月售103份", "月售120份", "月售110份", "月售150份",
                                "月售142份", "月售166份", "月售132份", "月售152份"};
                        String[] ItemTitle2 = new String[]{"好评率100%", "好评率97%", "好评率98%", "好评率99%", "好评率96%",
                                "好评率96%", "好评率97%", "好评率95%", "好评率98%"};
                        String[] ItemTitle3 = new String[]{"厨师1", "厨师2", "厨师3", "厨师1", "厨师2",
                                "厨师2", "厨师1", "厨师3", "厨师2"};
                        String[] ItemTitle4 = new String[]{"老板推荐", "老板推荐", "老板推荐", "老板推荐", "老板推荐",
                                "老板推荐", "老板推荐", "老板推荐", "老板推荐"};
                        String[] ItemTitle5 = new String[]{"￥13", "￥11", "￥20", "￥14", "￥15",
                                "￥13", "￥10", "￥11", "￥14"};
                        for (int j = 0; j < ItemTitle.length; j++) {
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("forlistiv1", pics[j]);
                            map.put("forlisttv1", ItemTitle[j]);
                            map.put("forlisttv2", ItemTitle1[j]);
                            map.put("forlisttv3", ItemTitle2[j]);
                            map.put("forlisttv4", ItemTitle3[j]);
                            map.put("forlisttv5", ItemTitle4[j]);
                            map.put("forlisttv6", ItemTitle5[j]);
                            map.put("forlistib1", R.drawable.addtocar1);
                            map.put("forlisttv7", ItemTitle6[j]);
                            map.put("forlistib2", R.drawable.addtocar);
                            mylist.add(map);
                        }
                        SimpleAdapter simpleA = new SimpleAdapter(
                                Main.this,
                                mylist,
                                R.layout.forlist,
                                new String[]{"forlistiv1", "forlisttv1", "forlisttv2", "forlisttv3", "forlisttv4", "forlisttv5",
                                        "forlisttv6", "forlistib1", "forlisttv7", "forlistib2"},
                                new int[]{R.id.forlistiv1, R.id.forlisttv1, R.id.forlisttv2, R.id.forlisttv3, R.id.forlisttv4, R.id.forlisttv5,
                                        R.id.forlisttv6, R.id.forlistib1, R.id.forlisttv7, R.id.forlistib2}
                        );
                        list.setAdapter(simpleA);
                    }
                }
            });
            ad.setNeutralButton("清空", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ItemTitle6[position] = 0;
                    DataProvider.getInstance().setSpecificPlate(position, 0);
                    sumUpNum = (TextView) findViewById(R.id.sumupnum);
                    sumUpNum.setText(" " + DataProvider.getInstance().getTotalPay() + " ");
                    //设置菜单ListView列表
                    ListView list = (ListView) findViewById(android.R.id.list);
                    ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
                    String[] ItemTitle1 = new String[]{"月售106份", "月售103份", "月售120份", "月售110份", "月售150份",
                            "月售142份", "月售166份", "月售132份", "月售152份"};
                    String[] ItemTitle2 = new String[]{"好评率100%", "好评率97%", "好评率98%", "好评率99%", "好评率96%",
                            "好评率96%", "好评率97%", "好评率95%", "好评率98%"};
                    String[] ItemTitle3 = new String[]{"厨师1", "厨师2", "厨师3", "厨师1", "厨师2",
                            "厨师2", "厨师1", "厨师3", "厨师2"};
                    String[] ItemTitle4 = new String[]{"老板推荐", "老板推荐", "老板推荐", "老板推荐", "老板推荐",
                            "老板推荐", "老板推荐", "老板推荐", "老板推荐"};
                    String[] ItemTitle5 = new String[]{"￥13", "￥11", "￥20", "￥14", "￥15",
                            "￥13", "￥10", "￥11", "￥14"};
                    for (int j = 0; j < ItemTitle.length; j++) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("forlistiv1", pics[j]);
                        map.put("forlisttv1", ItemTitle[j]);
                        map.put("forlisttv2", ItemTitle1[j]);
                        map.put("forlisttv3", ItemTitle2[j]);
                        map.put("forlisttv4", ItemTitle3[j]);
                        map.put("forlisttv5", ItemTitle4[j]);
                        map.put("forlisttv6", ItemTitle5[j]);
                        map.put("forlistib1", R.drawable.addtocar1);
                        map.put("forlisttv7", ItemTitle6[j]);
                        map.put("forlistib2", R.drawable.addtocar);
                        mylist.add(map);
                    }
                    SimpleAdapter simpleA = new SimpleAdapter(
                            Main.this,
                            mylist,
                            R.layout.forlist,
                            new String[]{"forlistiv1", "forlisttv1", "forlisttv2", "forlisttv3", "forlisttv4", "forlisttv5",
                                    "forlisttv6", "forlistib1", "forlisttv7", "forlistib2"},
                            new int[]{R.id.forlistiv1, R.id.forlisttv1, R.id.forlisttv2, R.id.forlisttv3, R.id.forlisttv4, R.id.forlisttv5,
                                    R.id.forlisttv6, R.id.forlistib1, R.id.forlisttv7, R.id.forlistib2}
                    );
                    list.setAdapter(simpleA);
                }
            });
            ad.setPositiveButton("+1", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    // TODO Auto-generated method stub
                    ItemTitle6[position] += 1;
                    DataProvider.getInstance().addSpecificPlate(position);
                    sumUpNum = (TextView) findViewById(R.id.sumupnum);
                    sumUpNum.setText(" " + DataProvider.getInstance().getTotalPay() + " ");
                    //设置菜单ListView列表
                    ListView list = (ListView) findViewById(android.R.id.list);
                    ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
                    String[] ItemTitle1 = new String[]{"月售106份", "月售103份", "月售120份", "月售110份", "月售150份",
                            "月售142份", "月售166份", "月售132份", "月售152份"};
                    String[] ItemTitle2 = new String[]{"好评率100%", "好评率97%", "好评率98%", "好评率99%", "好评率96%",
                            "好评率96%", "好评率97%", "好评率95%", "好评率98%"};
                    String[] ItemTitle3 = new String[]{"厨师1", "厨师2", "厨师3", "厨师1", "厨师2",
                            "厨师2", "厨师1", "厨师3", "厨师2"};
                    String[] ItemTitle4 = new String[]{"老板推荐", "老板推荐", "老板推荐", "老板推荐", "老板推荐",
                            "老板推荐", "老板推荐", "老板推荐", "老板推荐"};
                    String[] ItemTitle5 = new String[]{"￥13", "￥11", "￥20", "￥14", "￥15",
                            "￥13", "￥10", "￥11", "￥14"};
                    for (int j = 0; j < ItemTitle.length; j++) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("forlistiv1", pics[j]);
                        map.put("forlisttv1", ItemTitle[j]);
                        map.put("forlisttv2", ItemTitle1[j]);
                        map.put("forlisttv3", ItemTitle2[j]);
                        map.put("forlisttv4", ItemTitle3[j]);
                        map.put("forlisttv5", ItemTitle4[j]);
                        map.put("forlisttv6", ItemTitle5[j]);
                        map.put("forlistib1", R.drawable.addtocar1);
                        map.put("forlisttv7", ItemTitle6[j]);
                        map.put("forlistib2", R.drawable.addtocar);
                        mylist.add(map);
                    }
                    SimpleAdapter simpleA = new SimpleAdapter(
                            Main.this,
                            mylist,
                            R.layout.forlist,
                            new String[]{"forlistiv1", "forlisttv1", "forlisttv2", "forlisttv3", "forlisttv4", "forlisttv5",
                                    "forlisttv6", "forlistib1", "forlisttv7", "forlistib2"},
                            new int[]{R.id.forlistiv1, R.id.forlisttv1, R.id.forlisttv2, R.id.forlisttv3, R.id.forlisttv4, R.id.forlisttv5,
                                    R.id.forlisttv6, R.id.forlistib1, R.id.forlisttv7, R.id.forlistib2}
                    );
                    list.setAdapter(simpleA);
                }
            });
            ad.create();
            ad.show();//显示对话框
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > Util.INTERVAL) {
                Toast.makeText(Main.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
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


    public void sqlSearch() {

        NetWorkManager.getRequest().getSellState()
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Observer<ArrayList<SellState>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<SellState> sellStates) {
                        for (int i = 0; i < sellStates.size(); i++) {
                            dishState[i] = sellStates.get(i).getState();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        finishState1 = true;
                    }
                });
    }
    /*adView*/

    /**
     * 以下方法用于: 初始化图片轮播的小圆点和目录
     * by txk
     */

    private void initPoints(int count) {
        for (int i = 0; i < count; i++) {

            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    20, 20);
            params.setMargins(0, 0, 20, 0);
            iv.setLayoutParams(params);

            iv.setImageResource(R.drawable.dot2);

            ll_dotGroup.addView(iv);

        }
        ((ImageView) ll_dotGroup.getChildAt(curIndex))
                .setImageResource(R.drawable.dot1);
        newsTitle.setText(adList.get(curIndex).getDishname());
    }

    /**
     * 以下方法用于:自动播放
     * by txk
     */
    private void startAutoScroll() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 每隔4秒钟切换一张图片
        scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 5, 4, TimeUnit.SECONDS);
    }


    @Override
    public void onConnected() {

    }

    @Override
    public void onLost() {

    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onReceived(String topic, String message) {
        if(topic.equals(Util.MQTT_TOPIC)){
//            MqttMessage mqttMessage=gson.fromJson(message,MqttMessage.class);
            Util.Loge(message);
        }
    }

    @Override
    public void onSentSuccessfully() {
        Util.Loge("sentSuccessfully");
    }

    /**
     * 以下方法用于:切换图片任务
     * by txk
     */

    private class ViewPagerTask implements Runnable {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int count = picsAdapter.getCount();
                    mViewPager.setCurrentItem((curIndex + 1) % count);
                }
            });
        }
    }

    /**
     * 定义ViewPager控件页面切换监听器
     */
    class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            ImageView imageView1 = (ImageView) ll_dotGroup.getChildAt(position);
            ImageView imageView2 = (ImageView) ll_dotGroup.getChildAt(curIndex);
            if (imageView1 != null) {
                imageView1.setImageResource(R.drawable.dot2);
            }
            if (imageView2 != null) {
                imageView2.setImageResource(R.drawable.dot1);
            }
            curIndex = position;
            newsTitle.setText(adList.get(curIndex).getDishname());

        }

        boolean b = false;

        @Override
        public void onPageScrollStateChanged(int state) {
            //这段代码可不加，主要功能是实现切换到末尾后返回到第一张
            switch (state) {
                case 1:
                    // 手势滑动
                    b = false;
                    break;
                case 2:
                    // 界面切换中
                    b = true;
                    break;
                case 0:
                    // 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (mViewPager.getCurrentItem() == mViewPager.getAdapter()
                            .getCount() - 1 && !b) {
                        mViewPager.setCurrentItem(0);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (mViewPager.getCurrentItem() == 0 && !b) {
                        mViewPager.setCurrentItem(mViewPager.getAdapter()
                                .getCount() - 1);
                    }
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * 以下方法用于:定义ViewPager控件适配器
     * by txk
     */

    class PicsAdapter extends PagerAdapter {

        private List<ImageView> views = new ArrayList<ImageView>();

        @Override
        public int getCount() {
            if (views == null) {
                return 0;
            }
            return views.size();
        }

        public void setData() {
            for (int i = 0; i < totalPage; i++) {
                ImageView iv = new ImageView(Main.this);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                iv.setLayoutParams(params);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                //设置ImageView的属性
                iv.setImageResource(imgResIds[i]);
                views.add(iv);
            }
        }

        public Object getItem(int position) {
            if (position < getCount()) {
                return views.get(position);
            }
            return null;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            if (position < views.size()) {
                container.removeView(views.get(position));
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return views.indexOf(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            if (position < views.size()) {
                final ImageView imageView = views.get(position);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Main.this, "" + adList.get(position).getDescription(), Toast.LENGTH_SHORT).show();
                    }
                });
                container.addView(imageView);
                return views.get(position);
            }
            return null;
        }
    }

    class ZoomOutPageTransforme implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        @Override
        public void transformPage(@NonNull View page, float position) {
            int pageHeight = page.getHeight();
            int pageWidth = page.getWidth();
            if (position < -1) {
                page.setAlpha(0f);
            } else if (position <= 1) {
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    page.setTranslationX(horMargin - vertMargin / 2);
                } else {
                    page.setTranslationX(-horMargin + vertMargin / 2);
                }
                // Scale the page down (between MIN_SCALE and 1)
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                page.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            } else {
                page.setAlpha(0f);
            }
        }
    }

    class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }

}
