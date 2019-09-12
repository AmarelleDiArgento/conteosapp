package com.lotus.conteos_app;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lotus.conteos_app.Model.iFenologia;
import com.lotus.conteos_app.Model.tab.fenologiaTab;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


public class VisualFenoActivity extends AppCompatActivity {

    List<fenologiaTab> lf = new ArrayList<>();
    String path = null;

    iFenologia iF = null;
    int dia = 0, IdVariedad;
    String variedad ,imagen,imagen2,imagen3,imagen4;
    Float gDia;

    ImageView jpgView1, jpgView2, jpgView3, jpgView4;
    TextView txt1,txt2,txt3,txt4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_feno);
        getSupportActionBar().hide();

        jpgView1 = (ImageView) findViewById(R.id.jpgView1);
        jpgView2 = (ImageView) findViewById(R.id.jpgView2);
        jpgView3 = (ImageView) findViewById(R.id.jpgView3);
        jpgView4 = (ImageView) findViewById(R.id.jpgView4);

        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);
        txt4 = (TextView) findViewById(R.id.txt4);

        cargarImagenes();
    }

    //REALIZA LA CARGAS DE IMAGEN SEGUN FENOLOFIA
    public void cargarImagenes() {
        try {
            SharedPreferences fotollegada = getBaseContext().getSharedPreferences("Fotosfenol", MODE_PRIVATE);
            if (fotollegada != null) {
                variedad = fotollegada.getString("variedad", "");
                imagen = fotollegada.getString("fotoImagen", "");
                imagen2 = fotollegada.getString("fotoImagen2", "");
                imagen3 = fotollegada.getString("fotoImagen3", "");
                imagen4 = fotollegada.getString("fotoImagen4", "");
                gDia = fotollegada.getFloat("gDia", 0);
                dia = fotollegada.getInt("dia", 0);
                IdVariedad = fotollegada.getInt("IdVariedad",0);

                final String path = "/storage/extSdCard/"+variedad+"/"+imagen+"";
                final String path2 = "/storage/extSdCard/"+variedad+"/"+imagen2+"";
                final String path3 = "/storage/extSdCard/"+variedad+"/"+imagen3+"";
                final String path4 = "/storage/extSdCard/"+variedad+"/"+imagen4+"";

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

                cargaDl(dia,gDia,IdVariedad);
            }

        } catch (Exception e) {
            Toast toast = Toast.makeText(getApplicationContext(), "Lo sentimos pero no se pudieron cargar las fotos \n"+e.toString(), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP,0,100);
            toast.show();
        }
    }

    //CARGA DE LOGITUN Y DIAMETRO
    public void cargaDl(int dia,Float gDia,int idVariedad){

        try {
            path = getExternalFilesDir(null) + File.separator;
            iF = new iFenologia(path);
            lf = iF.all();

            List<fenologiaTab> fi = iF.forGrado(dia, gDia, idVariedad);
            Double d = fi.get(0).getDiametro_boton();
            Double l = fi.get(0).getLargo_boton();
            Double d1 = fi.get(1).getDiametro_boton();
            Double l1 = fi.get(1).getLargo_boton();
            Double d2 = fi.get(2).getDiametro_boton();
            Double l2 = fi.get(2).getLargo_boton();
            Double d3 = fi.get(3).getDiametro_boton();
            Double l3 = fi.get(3).getLargo_boton();

            parseoDecimal(d,l,d1,l1,d2,l2,d3,l3);

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Exception carga DL \n" +e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    //PARSEO DE DECIMAL
    public void parseoDecimal(Double d,Double l, Double d1, Double l1, Double d2, Double l2, Double d3,Double l3){
        BigDecimal dpar = new BigDecimal(d);
        dpar = dpar.setScale(2, RoundingMode.DOWN);

        BigDecimal lpar = new BigDecimal(l);
        lpar = lpar.setScale(2, RoundingMode.DOWN);

        BigDecimal d1par = new BigDecimal(d1);
        d1par = d1par.setScale(2, RoundingMode.DOWN);

        BigDecimal l1par = new BigDecimal(l1);
        l1par = l1par.setScale(2, RoundingMode.DOWN);

        BigDecimal d2par = new BigDecimal(d2);
        d2par = d2par.setScale(2, RoundingMode.DOWN);

        BigDecimal l2par = new BigDecimal(l2);
        l2par = l2par.setScale(2, RoundingMode.DOWN);

        BigDecimal d3par = new BigDecimal(d3);
        d3par = d3par.setScale(2, RoundingMode.DOWN);

        BigDecimal l3par = new BigDecimal(l3);
        l3par = l3par.setScale(2, RoundingMode.DOWN);

        txt1.setText("Diametro: " + dpar +"\n Longitud: " + lpar);
        txt1.setTextSize(20);
        txt2.setText("Diametro: " + d1par +"\n Longitud: " + l1par);
        txt2.setTextSize(20);
        txt3.setText("Diametro: " + d2par +"\n Longitud: " + l2par);
        txt3.setTextSize(20);
        txt4.setText("Diametro: " + d3par +"\n Longitud: " + l3par);
        txt4.setTextSize(20);
    }
}

