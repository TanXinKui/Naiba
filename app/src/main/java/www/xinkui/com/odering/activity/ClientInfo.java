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
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import www.xinkui.com.odering.R;
import www.xinkui.com.odering.bean.User;
import www.xinkui.com.odering.database.DataProvider;
import www.xinkui.com.odering.network.NetWorkManager;
import www.xinkui.com.odering.network.exception.ApiException;
import www.xinkui.com.odering.network.response.ResponseTransformer;
import www.xinkui.com.odering.network.schedulers.SchedulerProvider;
import www.xinkui.com.odering.user.Login;
import www.xinkui.com.odering.util.Util;

import static java.lang.Thread.sleep;

/**
 * Created by lenovo on 2017/11/2.
 */

/**
 * @author TONXOK
 * @description
 * @time 2019/4/28 10:23
 */
public class ClientInfo extends Activity {
    private long firstTime = 0;
    TextView client_balance;
    Button charge, client_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client);
        client_refresh = (Button) findViewById(R.id.client_refresh);
        client_balance = (TextView) findViewById(R.id.client_balance);
        queryBalance(DataProvider.getInstance().getUsername());
        TextView username = (TextView) findViewById(R.id.username);
        username.setText("用户名             " + DataProvider.getInstance().getUsername());
        initViews();
    }

    private void initViews() {
        charge = (Button) findViewById(R.id.charge);
        charge.setOnClickListener(v ->
                Toast.makeText(ClientInfo.this, "请先到柜台充值或扫描餐桌二维码(每次仅支持充值20￥,50￥,100￥,200￥,1000￥)充值备注：“充值+账号” 如：充值+TXK", Toast.LENGTH_LONG).show()
        );
        Button btn1 = (Button) this.findViewById(R.id.client_bottom_left);
        Button btn2 = (Button) this.findViewById(R.id.client_bottom_mid);
        Button btn3 = (Button) this.findViewById(R.id.client_exit);
        client_refresh.setOnClickListener(v -> {
                    client_refresh.setText("正在刷新");
                    Intent intent = new Intent(ClientInfo.this, ClientInfo.class);
                    Toast.makeText(ClientInfo.this, "正在刷新", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    overridePendingTransition(0, 0);
                }
        );
        btn1.setOnClickListener(v -> {
                    Intent intent = new Intent(ClientInfo.this, Main.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    overridePendingTransition(0, 0);
                }
        );
        btn2.setOnClickListener(v -> {
                    if (DataProvider.getInstance().getTotalPay() == 0) {
                        Toast.makeText(ClientInfo.this, "请先到订餐页面订餐！", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(ClientInfo.this, OrderList.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                }
        );
        btn3.setOnClickListener(v -> {
                    Intent intent = new Intent(ClientInfo.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
        );
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > Util.INTERVAL) {
                Toast.makeText(ClientInfo.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
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

    private void queryBalance(String userName) {
        NetWorkManager.getRequest().showBalance(userName)
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Observer<Integer>() {
                               @Override
                               public void onSubscribe(Disposable d) {

                               }

                               @Override
                               public void onNext(Integer integer) {
                                   client_balance.setText("账户余额         " + integer + "￥");
                               }

                               @Override
                               public void onError(Throwable e) {

                               }

                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
