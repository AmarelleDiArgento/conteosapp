package com.lotus.conteos_app.Config.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public abstract class FileAdmin {


    private ImageView img_cambia;

    //METODO PARA CAMBIAR LA IMAGEN SEGUN LO TIPEADO EN EL TEXTPLAIN
    public void btn_consulta (View view){

        String SD_CARD_PATH = Environment.getExternalStorageDirectory().toString();
        File file = new File(SD_CARD_PATH + "/"+"prueba.jpg");
        if(file.exists()){

            try{
                //Toast.makeText(this,""+file,Toast.LENGTH_LONG).show();
                Bitmap mybit= BitmapFactory.decodeFile(SD_CARD_PATH + "/"+"prueba.jpg");
                img_cambia.setImageBitmap(mybit);
            }catch (Exception ex){

                //Toast.makeText(this,"se efectuo un error---->" +ex.toString(),Toast.LENGTH_LONG).show();

            }

        }else{
            //Toast.makeText(this,"xxxxxxxx"+file,Toast.LENGTH_LONG).show();
        }
    }


}
