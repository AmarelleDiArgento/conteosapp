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



}
