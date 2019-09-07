package com.lotus.conteos_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;

import com.lotus.conteos_app.Config.Util.imageAdmin;
import com.lotus.conteos_app.Model.iFenologia;
import com.lotus.conteos_app.Model.tab.fenologiaTab;

import java.io.File;
import java.util.List;

public class VisualFenoActivity extends AppCompatActivity {

    ImageView jpgView1, jpgView2;

    iFenologia iF = null;

    final String path = "/storage/extSdCard/FREEDOM/GDA8117 JP6.JPG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_feno);
        getSupportActionBar().hide();

        jpgView1 = (ImageView) findViewById(R.id.jpgView1);
        jpgView2 = (ImageView) findViewById(R.id.jpgView2);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            String sem =bundle.getString("dato");
            cargarImagenes(sem);
        }else {
            Toast.makeText(this,"el dato no se cargo",Toast.LENGTH_SHORT).show();
        }
    }

    //REALIZA LA CARGAS DE IMAGEN SEGUN FENOLOFIA
    public void cargarImagenes(String sem) {
        try {

            if(sem.equals("semana1")){
                Toast.makeText(this,"se cargan las imagenes para "+sem,Toast.LENGTH_SHORT).show();
                File f = new File( path );

                if (f.exists()) {

                    Toast.makeText(this,"path " +f.toString(),Toast.LENGTH_SHORT).show();
                    Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());
                    jpgView1.setImageBitmap(bitmap);
                    jpgView2.setImageBitmap(bitmap);
                }else{
                    Toast.makeText(this,"no se encontro imagen  " +f.toString(),Toast.LENGTH_SHORT).show();
                }


            }else if(sem.equals("semana2")){
                Toast.makeText(this,"se cargan las imagenes para "+sem,Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast toast = Toast.makeText(getApplicationContext(), "PAILAS "+e.toString(), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP,0,100);
            toast.show();
        }
    }
}
