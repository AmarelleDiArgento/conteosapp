package com.lotus.conteos_app;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;


public class VisualFenoActivity extends AppCompatActivity {

    ImageView jpgView1, jpgView2, jpgView3, jpgView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_feno);
        getSupportActionBar().hide();

        jpgView1 = (ImageView) findViewById(R.id.jpgView1);
        jpgView2 = (ImageView) findViewById(R.id.jpgView2);
        jpgView3 = (ImageView) findViewById(R.id.jpgView3);
        jpgView4 = (ImageView) findViewById(R.id.jpgView4);

        cargarImagenes();
    }

    //REALIZA LA CARGAS DE IMAGEN SEGUN FENOLOFIA
    public void cargarImagenes() {
        try {

            String variedad ,imagen,imagen2,imagen3,imagen4;

            SharedPreferences fotollegada = getBaseContext().getSharedPreferences("Fotosfenol", MODE_PRIVATE);
            if (fotollegada != null) {
                variedad = fotollegada.getString("fotoVariedad", "");
                imagen = fotollegada.getString("fotoImagen", "");
                imagen2 = fotollegada.getString("fotoImagen2", "");
                imagen3 = fotollegada.getString("fotoImagen3", "");
                imagen4 = fotollegada.getString("fotoImagen4", "");

                final String path = "/storage/extSdCard/"+variedad+"/"+imagen+"";
                final String path2 = "/storage/extSdCard/"+variedad+"/"+imagen2+"";
                final String path3 = "/storage/extSdCard/"+variedad+"/"+imagen3+"";
                final String path4 = "/storage/extSdCard/"+variedad+"/"+imagen4+"";
                //Toast.makeText(this,path,Toast.LENGTH_LONG).show();

                File f = new File( path );
                File f2 = new File( path2 );
                File f3 = new File( path3 );
                File f4 = new File( path4 );

                Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());
                Bitmap bitmap2 = BitmapFactory.decodeFile(f2.getPath());
                Bitmap bitmap3 = BitmapFactory.decodeFile(f3.getPath());
                Bitmap bitmap4 = BitmapFactory.decodeFile(f4.getPath());
                jpgView1.setImageBitmap(bitmap);
                jpgView2.setImageBitmap(bitmap2);
                jpgView3.setImageBitmap(bitmap3);
                jpgView4.setImageBitmap(bitmap4);

            } else {
                Toast.makeText(this,"no llegaron las fotos",Toast.LENGTH_SHORT).show();
            }



        } catch (Exception e) {
            Toast toast = Toast.makeText(getApplicationContext(), "Lo sentimo pero no se pudieron cargar las fotos \n"+e.toString(), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP,0,100);
            toast.show();
        }
    }
}
