package com.lotus.conteos_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class Camera extends AppCompatActivity {
    private ZBarScannerView vbc;

    EditText resulcode;


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




    class barcodeimp implements ZBarScannerView.ResultHandler {

        @Override
        public void handleResult(Result result) {

            try {
                setContentView(R.layout.activity_main);
                int bc = Integer.parseInt(result.getContents());

                    if (bc != 0) {
                            Toast toast = Toast.makeText(getApplicationContext(), "si hay resultado " + bc, Toast.LENGTH_SHORT);
                            toast.show();
                        vbc.stopCamera();//aqui apaga la camara
                        int resulcode=bc;
                        Intent intent= new Intent(Camera.this,MainActivity.class);
                        intent.putExtra(null,resulcode);
                        startActivity(intent);

                        vbc.stopCamera();//aqui apaga la camara

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
        //Toast.makeText(this,"se retrocedio",Toast.LENGTH_LONG).show();
        Intent i = new Intent(Camera.this, MainActivity.class);
        startActivity(i);
        vbc.stopCamera();//aqui apaga la camara
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "OnStart camara", Toast.LENGTH_SHORT).show();
        // La actividad está a punto de hacerse visible.
    }
    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "OnResume camara", Toast.LENGTH_SHORT).show();
        // La actividad se ha vuelto visible (ahora se "reanuda").
    }
    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "OnResume camara", Toast.LENGTH_SHORT).show();
        // Enfocarse en otra actividad  (esta actividad está a punto de ser "detenida").
        Intent i = new Intent(Camera.this, MainActivity.class);
        startActivity(i);
        vbc.stopCamera();//aqui apaga la camara
    }
    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "OnStop camara", Toast.LENGTH_SHORT).show();
        // La actividad ya no es visible (ahora está "detenida")
        Intent i = new Intent(Camera.this, MainActivity.class);
        startActivity(i);
        vbc.stopCamera();//aqui apaga la camara
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "OnDestroy camara", Toast.LENGTH_SHORT).show();
        // La actividad está a punto de ser destruida.
        Intent i = new Intent(Camera.this, MainActivity.class);
        startActivity(i);
        vbc.stopCamera();//aqui apaga la camara
    }
}
