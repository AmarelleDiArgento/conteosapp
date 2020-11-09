package com.lotus.conteos_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lotus.conteos_app.Config.Util.imageAdmin;
import com.lotus.conteos_app.Config.Util.jsonAdmin;
import com.lotus.conteos_app.Model.iFenologia;
import com.lotus.conteos_app.Model.tab.fenologiaTab;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class VisualFenoActivity extends AppCompatActivity {

    List<fenologiaTab> lf = new ArrayList<>();
    String path = null;

    SharedPreferences sp = null;

    iFenologia iF = null;
    int dia = 0;
    long idVariedad, idFinca;
    String variedad, imagen, imagen2, imagen3, imagen4;
    Float gDia;

    ImageView jpgView1, jpgView2, jpgView3, jpgView4;
    TextView txt1, txt2, txt3, txt4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_feno);
        getSupportActionBar().hide();
        try {
            sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);

            jpgView1 = findViewById(R.id.jpgView1);
            jpgView2 = findViewById(R.id.jpgView2);
            jpgView3 = findViewById(R.id.jpgView3);
            jpgView4 = findViewById(R.id.jpgView4);

            txt1 = findViewById(R.id.txt1);
            txt2 = findViewById(R.id.txt2);
            txt3 = findViewById(R.id.txt3);
            txt4 = findViewById(R.id.txt4);
            path = getExternalFilesDir(null) + File.separator;
            iF = new iFenologia(path);

        } catch (Exception e) {
            Toast.makeText(this, "Exception On create:     " + e.toString(), Toast.LENGTH_LONG).show();

        }

        cargarImagenes();
    }

    //REALIZA LA CARGAS DE IMAGEN SEGUN FENOLOFIA
    public void cargarImagenes() {
        try {

            variedad = sp.getString("variedad", "");
            gDia = sp.getFloat("gDia", 0);
            dia = sp.getInt("dia", 0);
            idVariedad = sp.getLong("IdVariedad", 0);
            idFinca = sp.getLong("IdFinca",0);



            imageAdmin iA = new imageAdmin();
            List<fenologiaTab> fi = forGradoloc(dia, gDia, idVariedad);

            path = getExternalFilesDir(null) + File.separator;
            String path2 = "/storage/emulated/0/Pictures/fenologias/"+idFinca+"/";

            iA.getImage(path2,jpgView1, idVariedad, fi.get(0).getImagen());
            datos(txt1, fi.get(0).getDiametro_boton(), fi.get(0).getLargo_boton(), fi.get(0).getGrados_dia(), fi.get(0).getImagen());

            iA.getImage(path2,jpgView2, idVariedad, fi.get(1).getImagen());
            datos(txt2, fi.get(1).getDiametro_boton(), fi.get(1).getLargo_boton(), fi.get(1).getGrados_dia(), fi.get(1).getImagen());

            iA.getImage(path2,jpgView3, idVariedad, fi.get(2).getImagen());
            datos(txt3, fi.get(2).getDiametro_boton(), fi.get(2).getLargo_boton(), fi.get(2).getGrados_dia(), fi.get(2).getImagen());

            iA.getImage(path2,jpgView4, idVariedad, fi.get(3).getImagen());
            datos(txt4, fi.get(3).getDiametro_boton(), fi.get(3).getLargo_boton(), fi.get(3).getGrados_dia(), fi.get(3).getImagen());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error \n" + e, Toast.LENGTH_LONG).show();
        }
    }

    public String getPath(String path){
        String[] res = path.split((char) 47+"");
        return res[6]+"/"+res[7]+"/"+res[8]+"/";
    }

    public List<fenologiaTab> forGradoloc(int dia, float gDia, long idVariedad) throws Exception {

        int d = (8 - dia);

        float[] img = new float[4];
        img[0] = (d) * gDia;
        img[1] = (d + 7) * gDia;
        img[2] = (d + 21) * gDia;
        img[3] = (d + 28) * gDia;
        int c = 0;
        //Toast.makeText(this, "dia: " + dia + "gDia: " + gDia + "idVariedad: " + idVariedad, Toast.LENGTH_LONG).show();
        // en caso de no saber que paso en los grados dia.
        //Toast.makeText(this, img[0] + ",  " + img[1] + ",  " + img[2] + ",  " + img[3] + ".", Toast.LENGTH_LONG).show();
        String data = "";
        Iterator<fenologiaTab> i = iF.all().iterator();
        List<fenologiaTab> fi = new ArrayList<>();
        fenologiaTab fu = new fenologiaTab();

        while (i.hasNext()) {
            fenologiaTab f = i.next();
            if (f.getIdFenologia() == idVariedad) {

                data += img[c] + " - " + f.getGrados_dia() + " " + (img[c] <= f.getGrados_dia()) + "\n";
                if (img[c] <= f.getGrados_dia()) {
                    double pos = f.getGrados_dia() - img[c];
                    double pre = img[c] - fu.getGrados_dia();

                    if (pre >= pos) {
                        fi.add(f);
                    } else {
                        fi.add(fu);
                    }
                    c++;
                    if (c >= 4) {
                        break;
                    }
                }
                fu = f;
            }
        }
        if (c < 4) {
            fi.add(fu);
        }
        jsonAdmin jsonAdmin = new jsonAdmin();
        jsonAdmin.CrearArchivo(path, "Error 2", data);
        return fi;
    }

    public void datos(TextView tv, Double dia, Double lon, Double grados, String rutaFenologica) {
        tv.setText("Diametro: " + dia + "    Longitud: " + lon + "\n Grados día acumulados: " + grados);
        tv.setTextSize(16);
    }

   public BigDecimal pD(double dou) {
        BigDecimal dpar = new BigDecimal(dou);
        return dpar.setScale(2, RoundingMode.DOWN);
    }

    public void fenologiaCompleta(View v){
        Intent i = new Intent(this, fenologiaCompletaVisual.class);
        startActivity(i);
    }
}

