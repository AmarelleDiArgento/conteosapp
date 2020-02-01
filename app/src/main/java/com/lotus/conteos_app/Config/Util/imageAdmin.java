package com.lotus.conteos_app.Config.Util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;

public class imageAdmin {


    final String path = "/storage/extSdCard/";


    public void getImage(String path2, ImageView iv, long idVariedad, String imagen) throws Exception {

        File f = new File( path + idVariedad + "/" + imagen);
        File f2 = new File( path2 + idVariedad + "/" + imagen);

        if (f.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());
            iv.setImageBitmap(bitmap);
        }else if(f2.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(f2.getPath());
            iv.setImageBitmap(bitmap);
        }

    }


}