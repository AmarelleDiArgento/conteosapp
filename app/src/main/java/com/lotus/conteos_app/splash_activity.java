package com.lotus.conteos_app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class splash_activity extends AppCompatActivity {

    private final int DURACION_SPLASH = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_activity);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent i = new Intent(splash_activity.this,Login.class);
                startActivity(i);
                finish();
            };
        },DURACION_SPLASH);
    }
}
