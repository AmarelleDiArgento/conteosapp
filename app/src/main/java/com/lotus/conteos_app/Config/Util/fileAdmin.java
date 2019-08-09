package com.lotus.conteos_app.Config.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public abstract class fileAdmin {


    public String sdPath = null;

    public fileAdmin() {
        this.sdPath = Environment.getExternalStorageDirectory() + File.separator;
    }

    //METODO PARA CAMBIAR LA IMAGEN SEGUN LO TIPEADO EN EL TEXTPLAIN
    public void btn_consulta(ImageView imageView, String file_name) throws Exception{

        File file = new File(sdPath + file_name);
        if (file.exists()) {

            try {
                //Toast.makeText(this,""+file,Toast.LENGTH_LONG).show();
                Bitmap mybit = BitmapFactory.decodeFile(file.getAbsolutePath());
                imageView.setImageBitmap(mybit);
            } catch (Exception ex) {

                //Toast.makeText(this,"se efectuo un error---->" +ex.toString(),Toast.LENGTH_LONG).show();

            }

        } else {
            //Toast.makeText(this,"xxxxxxxx"+file,Toast.LENGTH_LONG).show();
        }
    }


}
