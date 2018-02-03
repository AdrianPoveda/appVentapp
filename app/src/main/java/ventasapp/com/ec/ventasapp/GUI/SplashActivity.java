package ventasapp.com.ec.ventasapp.GUI;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.RelativeLayout;

import ventasapp.com.ec.ventasapp.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        int secondsDelayed = 2;
        RelativeLayout layout =(RelativeLayout)findViewById(R.id.splash);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashActivity.this, Login.class));
                finish();

            }
        }, secondsDelayed * 1000);
    }
}
