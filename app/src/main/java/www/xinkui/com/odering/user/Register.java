package www.xinkui.com.odering.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import www.xinkui.com.odering.R;
import www.xinkui.com.odering.bean.User;
import www.xinkui.com.odering.network.NetWorkManager;
import www.xinkui.com.odering.network.exception.ApiException;
import www.xinkui.com.odering.network.response.ResponseTransformer;
import www.xinkui.com.odering.network.schedulers.SchedulerProvider;
import www.xinkui.com.odering.util.Util;

import static java.lang.Thread.sleep;

/**
 * Created by lenovo on 2017/12/14.
 */

/**
 * @author TONXOK
 * @description
 * @time 2019/4/28 10:25
 */
public class Register extends Activity {
    String name, pass, qrmm, sj;
    private long firstTime = 0;
    Button closebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login1);
        ImageButton btn3 = (ImageButton) findViewById(R.id.zczh);
        closebtn = (Button) findViewById(R.id.closebtn);
        closebtn.setOnClickListener(v -> {
                    Intent intent = new Intent(Register.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
        );
        btn3.setOnClickListener(v -> {
                    EditText et1 = (EditText) findViewById(R.id.yhm1);
                    EditText et2 = (EditText) findViewById(R.id.mm1);
                    EditText et3 = (EditText) findViewById(R.id.qrmm1);
                    EditText et4 = (EditText) findViewById(R.id.yx1);
                    name = et1.getText().toString();
                    pass = et2.getText().toString();
                    qrmm = et3.getText().toString();
                    sj = et4.getText().toString();
                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass) && qrmm.equals(pass) && !TextUtils.isEmpty(sj)) {
                        et1.setText("");
                        et2.setText("");
                        toRegister(new www.xinkui.com.odering.bean.Register(name, pass, sj));
                    } else if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(qrmm) || TextUtils.isEmpty(sj)) {
                        Toast.makeText(Register.this, "请输入完整！", Toast.LENGTH_SHORT).show();
                    } else if (!qrmm.equals(pass)) {
                        Toast.makeText(Register.this, "密码输入不一致！", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void toRegister(www.xinkui.com.odering.bean.Register register) {
        NetWorkManager.getRequest().userRegister(register)
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.v(this.getClass().getName(), s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ApiException e1 = (ApiException) e;
                        Log.v(this.getClass().getName(), e1.getDisplayMessage());
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(Register.this, "注册成功！", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(Register.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > Util.INTERVAL) {
                Toast.makeText(Register.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
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

}
