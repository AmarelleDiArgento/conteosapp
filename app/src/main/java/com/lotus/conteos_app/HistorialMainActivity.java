package com.lotus.conteos_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lotus.conteos_app.Config.Util.jsonAdmin;
import com.lotus.conteos_app.Model.iPlano;
import com.lotus.conteos_app.Model.tab.planoTab;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class HistorialMainActivity extends AppCompatActivity {

    List<planoTab> pl = new ArrayList<>();

    jsonAdmin ja = null;
    EditText data;
    String path = null;
    ImageView jpnName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historial_main);

        data = (EditText) findViewById(R.id.data_tbl);
        path = getExternalFilesDir(null) + File.separator;
        ja = new jsonAdmin(path);

        //consulta_Plano();
    }

    public void consulta_Plano(){

        try {
            iPlano iP = new iPlano();
            String contenido = iP.all().toString();
            data.setText(contenido);
        } catch (Exception e) {
            Toast.makeText(this, "Plano local, no hay conexion a la base de datos", Toast.LENGTH_LONG).show();
            data.setText(e.toString());
        }
    }


    public void img_cargar(View v){
    this.getStoragePath();
    }

    File getStoragePath() {
        ImageView jpgView = (ImageView)findViewById(R.id.jpgview);

     try {
         String dia = data.getText().toString();//OBTENIENDO NOMBRE DE LA FOTO
         File file = new File("/storage/extSdCard/LOST.DIR/"+dia+".JPG");
         if (file.exists()) {

             Bitmap mybit = BitmapFactory.decodeFile(file.getPath());
             jpgView.setImageBitmap(mybit);
             if (!file.getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath()) && file.isDirectory() && file.canRead()) {
                 return file;
             }
         }else{
             Toast.makeText(this,"no obtuvo la imagen",Toast.LENGTH_LONG).show();
         }
         return Environment.getExternalStorageDirectory();
     }catch (Exception ex){
         Toast.makeText(this,"error"+ex,Toast.LENGTH_LONG).show();
     }

        return null;
    }
}
