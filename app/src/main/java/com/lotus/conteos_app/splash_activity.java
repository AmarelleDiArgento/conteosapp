package com.lotus.conteos_app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.lotus.conteos_app.Model.iMonitor;

import java.io.File;
import java.util.Calendar;

public class splash_activity extends AppCompatActivity {

    private long delayed;
    long ini, fin;

    String path = null;
    Calendar calendarDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_activity);

        delayed = 5000;
        path = getExternalFilesDir(null) + File.separator;

        getSupportActionBar().hide();


        ini = Calendar.getInstance().getTimeInMillis();
        try {

            iMonitor iM = new iMonitor(path);
            iM.local();

            // delayed =  delayed - Integer.valueOf((int) ());

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();;
        }

        fin = Calendar.getInstance().getTimeInMillis();
        if((fin - ini)>5000){
            delayed = 0;
        }else{
            delayed = delayed - (fin - ini);
                    }
        // Toast.makeText(this, "Time: " + delayed, Toast.LENGTH_LONG).show();


        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent i = new Intent(splash_activity.this,Login.class);
                startActivity(i);
                finish();
            };
        },delayed);
    }

}
