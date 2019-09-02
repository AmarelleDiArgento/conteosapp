package com.lotus.conteos_app.Config.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;

public class imageAdmin {


    final String path = Environment.getExternalStorageDirectory().getParent() + File.separator + "/extSdCard/";


    public void getImage(ImageView iv, String Variedad, String imagen) throws Exception {

        File f = new File(path + Variedad + "/" + imagen);

        if (f.exists() && f.canRead()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
            iv.setImageBitmap(myBitmap);
        }
    }

}