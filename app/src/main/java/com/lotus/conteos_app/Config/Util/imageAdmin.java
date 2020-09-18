package com.lotus.conteos_app.Config.Util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.lotus.conteos_app.R;

import java.io.File;
import java.io.FileNotFoundException;

public class imageAdmin {


    final String path = "/storage/extSdCard/";


    public void getImage(String path2, ImageView iv, long idVariedad, String imagen) throws Exception {

        File f = new File( path + idVariedad + "/" + imagen);
        File f2 = new File(path2 + idVariedad + "/" + imagen);

        Log.i("RIMG",""+f2);

        Bitmap bmp = BitmapFactory.decodeFile(f2.getAbsolutePath());
        if (bmp == null) {
            iv.setImageResource(R.drawable.flor); //poner imagen genérica
        } else {
            iv.setImageBitmap(bmp);
        }
    }


}