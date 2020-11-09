package com.lotus.conteos_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lotus.conteos_app.Config.Util.imageAdmin;
import com.lotus.conteos_app.Model.iCuadrosBloque;
import com.lotus.conteos_app.Model.iFenologia;
import com.lotus.conteos_app.Model.iPlano;
import com.lotus.conteos_app.Model.tab.cuadros_bloqueTab;
import com.lotus.conteos_app.Model.tab.fenologiaTab;

import java.io.File;
import java.util.ListIterator;

public class fenologiaCompletaVisual extends AppCompatActivity {

    iFenologia iF;

    String path;
    int position = 0,index = 0, indexR = 0;
    TextView txt1, txt2, txt3, txt4;
    ImageView image1, image2, image3, image4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenologia_completa_visual);
        getSupportActionBar().hide();
        try {
            path = getExternalFilesDir(null) + File.separator;;
            iF = new iFenologia(path);

            txt1 = findViewById(R.id.txt1);
            txt2 = findViewById(R.id.txt2);
            txt3 = findViewById(R.id.txt3);
            txt4 = findViewById(R.id.txt4);

            image1 = findViewById(R.id.image1);
            image2 = findViewById(R.id.image2);
            image3 = findViewById(R.id.image3);
            image4 = findViewById(R.id.image4);

            cambio();
        }catch (Exception e){
            Log.i("Fenologia: ", "ERROR: "+e.toString());
        }
    }

    public void next(View v){
        cambio();
    }

    public void after(View v){
        cambioAtras();
    }

    public void cambio(){
        try {
            while (position < iF.all().size()) {
                position++;
                index++;
                fenologiaTab f = iF.all().get(position);
                datos(f, index);
                if (position % 4 == 0) {
                    index = 0;
                    break;
                }
            }
        }catch (Exception e){
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void cambioAtras(){
        try {
            int positionAnterior = (position-8);
            if(position - 4 > 0){
                while (positionAnterior < (position-4)){
                    positionAnterior++;
                    indexR++;
                    datos(iF.all().get(positionAnterior), indexR);
                    if(positionAnterior == (position-4)){
                        indexR = 0;
                        break;
                    }
                }
                position = positionAnterior;
            }else{
                Log.i("FENOLOGIASRECORRIDAS", "NOPE");
            }
        }catch (Exception e){
            Log.i("FENOLOGIASRECORRIDAS", ""+e.toString());
        }
    }

    public void datos(fenologiaTab f, int index) throws Exception{
        String datos = "Diametro: " + f.getDiametro_boton() + "    Longitud: " + f.getLargo_boton() + "\n Grados día acumulados: " + f.getGrados_dia();

        long finca = 0000;
        iCuadrosBloque iCB = new iCuadrosBloque(path);
        for(cuadros_bloqueTab cb : iCB.all()){
            if(cb.getIdFenologia() == f.getIdFenologia()){
                finca = cb.getIdFinca();
                break;
            }
        }

        String path2 = "/storage/emulated/0/Pictures/fenologias/"+finca+"/";
        switch (index){
            case 1:
                txt1.setText(datos);
                new imageAdmin().getImage(path2, image1, f.getIdFenologia(), f.getImagen());
                break;
            case 2:
                txt2.setText(datos);
                new imageAdmin().getImage(path2, image2, f.getIdFenologia(), f.getImagen());
                break;
            case 3:
                txt3.setText(datos);
                new imageAdmin().getImage(path2, image3, f.getIdFenologia(), f.getImagen());
                break;
            case 4:
                txt4.setText(datos);
                new imageAdmin().getImage(path2, image4, f.getIdFenologia(), f.getImagen());
                break;
        }

    }
}