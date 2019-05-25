package www.xinkui.com.odering.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import www.xinkui.com.odering.activity.Main;
import www.xinkui.com.odering.R;
import www.xinkui.com.odering.bean.User;
import www.xinkui.com.odering.database.DataProvider;
import www.xinkui.com.odering.network.NetWorkManager;
import www.xinkui.com.odering.network.exception.ApiException;
import www.xinkui.com.odering.network.response.ResponseTransformer;
import www.xinkui.com.odering.network.schedulers.SchedulerProvider;
import www.xinkui.com.odering.util.Util;

import static java.lang.Thread.sleep;

/**
 * @author TONXOK
 * @description 登录页面
 * @time 2019/4/22 16:42
 */

public class Login extends Activity {
    private long firstTime = 0;
    Button btn2, btn3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        btn2 = (Button) this.findViewById(R.id.login_2_2);
        btn3 = (Button) this.findViewById(R.id.login_resgister);
        btn2.setOnClickListener(v -> {
            EditText et1 = (EditText) findViewById(R.id.login_name);
            EditText et2 = (EditText) findViewById(R.id.login_pass);
            String name1 = et1.getText().toString();
            String pass1 = et2.getText().toString();
            NetWorkManager.getRequest().userLogin(new User(name1, pass1))
                    .compose(ResponseTransformer.handleResult())
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .subscribe(checkToLogin -> {
                        succeedLogin(name1);
                    }, throwable -> {
                        // 处理异常
                        Log.v("tanxinkui", "Failed" + throwable.toString());
                        ApiException td = (ApiException) throwable;
                        Log.v("tanxinkui", "Failed" + td.getDisplayMessage());
                    });
        });
        btn3.setOnClickListener(v->{
                Intent intent = new Intent(Login.this, Register.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
            });

    }

    private void succeedLogin(String loginName) {
        DataProvider.getInstance().initData();
        DataProvider.getInstance().setUsername(loginName);
        Intent intent = new Intent(Login.this, Main.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(0, 0);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > Util.INTERVAL) {
                Toast.makeText(Login.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {

                super.onDestroy();
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
