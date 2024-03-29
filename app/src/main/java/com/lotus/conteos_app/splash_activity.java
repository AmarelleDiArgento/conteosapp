package com.lotus.conteos_app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lotus.conteos_app.Model.iCuadrosBloque;
import com.lotus.conteos_app.Model.iFincas;
import com.lotus.conteos_app.Model.iMonitor;

import java.io.File;
import java.util.Calendar;

public class splash_activity extends AppCompatActivity {

    private long delayed;
    long ini, fin;

    String path = null;
    TextView txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_activity);

        txtStatus = findViewById(R.id.idStatus2);


        delayed = 5000;
        path = getExternalFilesDir(null) + File.separator;

        getSupportActionBar().hide();


        ini = Calendar.getInstance().getTimeInMillis();
        fin = Calendar.getInstance().getTimeInMillis();


        if((fin - ini)>5000){
            delayed = 0;
        }else{
            delayed = delayed - (fin - ini);
        }

        if(screen() != 0){
            txtStatus.setText("Descargando datos por favor espere un momento...");
            ins();
        }

        new Handler().postDelayed(() -> {
                Intent i = new Intent(splash_activity.this,Login.class);
                startActivity(i);
                finish();
        },delayed);
    }

    public void ins(){
        try {
            iMonitor iM = new iMonitor(path);
            iFincas iF = new iFincas(path);
            iCuadrosBloque iCB = new iCuadrosBloque(path);
            iCB.local();
            iM.local();
            iF.local();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();;
        }
    }

    public int screen(){
        Bundle b = getIntent().getExtras();
        int s = b != null ? b.getInt("redireccion",0) : 0;
        return s;
    }

}
