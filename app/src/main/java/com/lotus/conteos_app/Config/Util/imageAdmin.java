package com.lotus.conteos_app.Config.Util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;

import com.lotus.conteos_app.VisualFenoActivity;

import java.io.File;

import static android.widget.Toast.makeText;

public class imageAdmin {


    final String path = "/storage/extSdCard/";


    public void getImage(ImageView iv, String Variedad, String imagen) throws Exception {

        File f = new File( path + Variedad + "/" + imagen);

        if (f.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());

            iv.setImageBitmap(bitmap);
        }
    }

}