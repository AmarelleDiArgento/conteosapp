package com.lotus.conteos_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ActionMenuView;
import android.widget.EditText;
import android.widget.Toast;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class Camera extends AppCompatActivity {
    private ZBarScannerView vbc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        //Toast.makeText(this,"llegamos a la camara",Toast.LENGTH_LONG).show();

        vbc = new ZBarScannerView(this);
        vbc.setResultHandler(new Camera.barcodeimp());
        setContentView(vbc);
        vbc.startCamera();
    }

    public class barcodeimp implements ZBarScannerView.ResultHandler {
        @Override
        public void handleResult(Result result) {
            try {
                int bc = Integer.parseInt(result.getContents());

                    if (bc != 0) {
                                //Toast toast = Toast.makeText(getApplicationContext(), "si hay resultado " + bc, Toast.LENGTH_SHORT);
                                //toast.show();

                            Intent intent = new Intent (Camera.this,MainActivity.class);
                            //Exportar parametro
                            intent.putExtra("codigo", bc);
                            startActivityForResult(intent, 0);
                    } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "no hay resultado", Toast.LENGTH_SHORT);
                            toast.show();
                    }
            } catch (Exception e) {
                Log.d("ERROR: ", e.toString());
                vbc.stopCamera();//aqui apaga la camara
            }
        }
    }

    //PARA VOLVER A LA ACTIVIDAD ANTERIOR(CAMARA)
    public void onBackPressed() {
        Intent i = new Intent(Camera.this, MainActivity.class);
        startActivity(i);
        vbc.stopCamera();//aqui apaga la camara
    }
}
