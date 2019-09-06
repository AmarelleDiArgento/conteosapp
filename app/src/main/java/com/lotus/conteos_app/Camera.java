package com.lotus.conteos_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class Camera extends AppCompatActivity {
    private ZBarScannerView vbc;

    //SE INICIALIZA LA CREACION DE LA ACTIVIDAD
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            vbc = new ZBarScannerView(this);
            vbc.setResultHandler(new Camera.barcodeimp());
            setContentView(vbc);
    }

    //VUELVE A LA ACTIVIDAD
    @Override
    public void onResume(){
        super.onResume();
        vbc.startCamera();
    }

    //SE PAUSA PARA ENVIAR DATO
    @Override
    public void onPause(){
        super.onPause();
        vbc.stopCamera();
    }

    //METODO PARA OBTENER EL RESULTADO DEL CODIGO DE BARRAS DESDE LA CAMARA
    public class barcodeimp implements ZBarScannerView.ResultHandler {
        @Override
        public void handleResult(Result result) {

            //Toast.makeText(Camera.this,"llega al metodo",Toast.LENGTH_SHORT).show();

            try {
                String bc = result.getContents();
                        if (bc != null) {
                            boolean camara_activada = true;
                            Intent intent = new Intent(Camera.this, MainActivity.class);
                            //Exportar parametro
                            intent.putExtra("codigo", bc);
                            intent.putExtra("camaraActivada", camara_activada);
                            startActivityForResult(intent, 0);
                            vbc.stopCamera();
                        } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "no hay resultado", Toast.LENGTH_SHORT);
                                toast.show();
                        }
            } catch (Exception e) {
                Toast toast = Toast.makeText(getApplicationContext(), "Lo sentimos, no se obtuvo resultado del codigo", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP,0,100);
                toast.show();
                Intent i = new Intent(Camera.this,MainActivity.class);
                startActivity(i);
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
