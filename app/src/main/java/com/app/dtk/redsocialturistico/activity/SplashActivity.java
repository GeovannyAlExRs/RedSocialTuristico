package com.app.dtk.redsocialturistico.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dtk.redsocialturistico.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME = 4500;

    private TextView txt_red1, txt_red2, txt_v, txt_v1, txt_company;
    private ImageView img_logo;

    private Animation animationTop, animationBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash);

        // Animations to Activity Splash...
        animationTop = AnimationUtils.loadAnimation(this, R.anim.scroll_up);
        animationBottom = AnimationUtils.loadAnimation(this, R.anim.scroll_down);

        getView();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent().setClass(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, SPLASH_TIME);

    }

    private void getView() {
        img_logo = findViewById(R.id.id_img_logo);

        txt_red1 = findViewById(R.id.id_txt_red1);
        txt_red2 = findViewById(R.id.id_txt_red2);

        txt_v = findViewById(R.id.id_txt_v);
        txt_v1 = findViewById(R.id.id_txt_v1);
        txt_company = findViewById(R.id.id_txt_red2);

        img_logo.setAnimation(animationTop);
        txt_red1.setAnimation(animationTop);
        txt_red2.setAnimation(animationTop);

        txt_v.setAnimation(animationBottom);
        txt_v1.setAnimation(animationBottom);
        txt_company.setAnimation(animationBottom);


    }
}