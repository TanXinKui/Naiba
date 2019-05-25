package www.xinkui.com.odering.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import www.xinkui.com.odering.R;
import www.xinkui.com.odering.user.Login;

/**
 * Created by lenovo on 2017/10/31.
 */

/**
 * @author TONXOK
 * @description
 * @time 2019/4/28 10:26
 */
public class Introduction extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction);
        ImageView Imagelogo = (android.widget.ImageView) this.findViewById(R.id.logo);
        TextView textView = (TextView) this.findViewById(R.id.appName1);
        ImageView img1 = (ImageView) this.findViewById(R.id.appName2);
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(1000);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(Introduction.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                Introduction.this.finish();
            }
        });
        Imagelogo.setAnimation(animation);
        textView.setAnimation(animation);
        img1.setAnimation(animation);
    }
}
