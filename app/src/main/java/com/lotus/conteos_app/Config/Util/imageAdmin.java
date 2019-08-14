package com.lotus.conteos_app.Config.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class imageAdmin {


    public String sdPath = null;

    public imageAdmin() {
        this.sdPath = Environment.getExternalStorageDirectory() + File.separator;
    }

    //METODO PARA CAMBIAR LA IMAGEN SEGUN LO TIPEADO EN EL TEXTPLAIN
    public boolean getImage(ImageView imageView, String file_name) throws Exception {
        File file = new File(sdPath + file_name);
        if (file.exists()) {
            //Toast.makeText(this,""+file,Toast.LENGTH_LONG).show();
            Bitmap mybit = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(mybit);
            return true;
        } else {
            return false;
        }
    }


}
