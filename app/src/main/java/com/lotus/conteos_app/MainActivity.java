package com.lotus.conteos_app;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lotus.conteos_app.Config.Util.imageAdmin;
import com.lotus.conteos_app.Model.iPlano;
import com.lotus.conteos_app.Model.iFenologia;
import com.lotus.conteos_app.Model.iConteo;
import com.lotus.conteos_app.Model.tab.planoTab;
import com.lotus.conteos_app.Model.tab.fenologiaTab;
import com.lotus.conteos_app.Model.tab.conteoTab;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    float gDia;
    EditText c1, c2, c3, c4, IdSiembra;
    Spinner cuadro;
    ImageView jpgView1, jpgView2, jpgView3, jpgView4;
    TextView gradoDia, finca, variedad, bloque, cama, fechaAct, usuario;

    List<planoTab> lp = new ArrayList<>();
    List<fenologiaTab> lf = new ArrayList<>();
    List<conteoTab> lc = new ArrayList<>();

    iPlano iP = null;
    iFenologia iF = null;
    iConteo iC = null;

    String path = null;

    Calendar calendarDate;
    String fecha = "";

    int dia = 0;
    int hora = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {

            gradoDia = (TextView) findViewById(R.id.gradoDia);
            gradoDia.setText(String.valueOf(recibirGradoDia()));

            IdSiembra = (EditText) findViewById(R.id.resulcode);
            c1 = (EditText) findViewById(R.id.c1et);
            c2 = (EditText) findViewById(R.id.c2et);
            c3 = (EditText) findViewById(R.id.c3et);
            c4 = (EditText) findViewById(R.id.c4et);

            cuadro = (Spinner) findViewById(R.id.cuadrosp);
            // Arreglo, desplegable (Spinner) cuadros
            String[] cuadros = {"1", "2", "3", "4", "5", "6", "7", "8"};

            // asociar arreglo cuadros al desplegable cuadro
            ArrayAdapter<String> cuadroArray = new ArrayAdapter<>(this, R.layout.spinner_item_personal, cuadros);
            cuadro.setAdapter(cuadroArray);

            usuario = (TextView) findViewById(R.id.usuario);

            jpgView1 = (ImageView) findViewById(R.id.feno1);
            jpgView2 = (ImageView) findViewById(R.id.feno2);
            jpgView3 = (ImageView) findViewById(R.id.feno3);
            jpgView4 = (ImageView) findViewById(R.id.feno4);

            finca = (TextView) findViewById(R.id.cam_finca);
            variedad = (TextView) findViewById(R.id.cam_variedad);
            bloque = (TextView) findViewById(R.id.cam_bloque);
            cama = (TextView) findViewById(R.id.cam_cama);

            fechaAct = (TextView) findViewById(R.id.fechaAct);

            getDate();
            cargarRecursos();

        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

        }

    }

    private void cargarRecursos() {

        path = getExternalFilesDir(null) + File.separator;
        //path = "/storage/extSdCard/";

        try {

            iP = new iPlano(path);
            iF = new iFenologia(path);
            iC = new iConteo(path);

            lp = iP.all();
            lf = iF.all();
            lc = iC.all();

        } catch (Exception e) {
            Toast.makeText(this, "Error de recursos: \n" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    //OBTENIENDO LA FECHA ACTUAL
    public void getDate() {

        calendarDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        fecha = sdf.format(calendarDate.getTime());

        fechaAct.setText(fecha);

        dia = calendarDate.get(Calendar.DAY_OF_WEEK) - 1;
        hora = calendarDate.get(Calendar.HOUR_OF_DAY);
        if (hora >= 12) {
            dia++;
        }

    }

    public float recibirGradoDia() {
        SharedPreferences gradoDia = getBaseContext().getSharedPreferences("gradoDia", MODE_PRIVATE);
        if (gradoDia != null) {
            gDia = gradoDia.getFloat("gradoDia", 0);
            return gDia;
        } else {
            return 0;
        }
    }

    public void buscarSiembra(View view) {
        try {
            long id = Long.parseLong(IdSiembra.getText().toString());
            planoTab p = iP.OneforIdSiembra(id);

            if (p != null) {
                String s;
                if (p.getSufijo() != null) {
                    s = p.getCama() + p.getSufijo();
                } else {
                    s = String.valueOf(p.getCama());
                }

                finca.setText(p.getFinca());
                variedad.setText(p.getVariedad());
                bloque.setText(p.getBloque());
                cama.setText(s);

                cargarImagenes(p.getIdVariedad());
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error busqueda" + e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void cargarImagenes(int idVariedad) {
        try {

            imageAdmin iA = new imageAdmin();
            List<fenologiaTab> fi = iF.forGrado(dia, gDia, idVariedad);

            Toast.makeText(this, fi.size() + fi.toString(), Toast.LENGTH_LONG).show();

            iA.getImage(jpgView1, fi.get(0).getVariedad(), fi.get(0).getImagen());
            iA.getImage(jpgView2, fi.get(1).getVariedad(), fi.get(1).getImagen());
            iA.getImage(jpgView3, fi.get(2).getVariedad(), fi.get(2).getImagen());
            iA.getImage(jpgView4, fi.get(3).getVariedad(), fi.get(3).getImagen());


        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /*
     *  ----------------------------------------------------------------------------------
     *   Registro de conteos segun criterio
     *  ----------------------------------------------------------------------------------
     */

    // Aumentar conteo de semana 1
    public void sumFen1(View v) {

        // Toast.makeText(this, "hola btn1", Toast.LENGTH_SHORT).show();
        int n = valnum(c1);
        c1.setText(String.valueOf(n + 1));
    }

    // Disminuir conteo de semana 1
    public void resFen1(View v) {
        // Toast.makeText(this, "hola btn2", Toast.LENGTH_SHORT).show();
        int n = valnum(c1);
        if (n > 0) {
            c1.setText(String.valueOf(n - 1));

        }
    }

    // Aumentar conteo de semana 4
    public void sumFen2(View v) {
        // Toast.makeText(this, "hola btn3", Toast.LENGTH_SHORT).show();
        int n = valnum(c4);
        c4.setText(String.valueOf(n + 1));
    }

    // Disminuir conteo de semana 4
    public void resFen2(View v) {
        // Toast.makeText(this, "hola btn4", Toast.LENGTH_SHORT).show();
        int n = valnum(c4);
        if (n > 0) {
            c4.setText(String.valueOf(n - 1));

        }
    }


    public void registrarConteo(View v) {

        try {
            conteoTab c = new conteoTab();
            c.setIdSiembra(Long.parseLong(IdSiembra.getText().toString()));
            c.setFecha(calendarDate.getTime());
            c.setCuadro(Integer.parseInt(cuadro.getSelectedItem().toString()));
            c.setConteo1(Integer.parseInt(c1.getText().toString()));
            c.setConteo2(0);
            c.setConteo3(0);
            c.setConteo4(Integer.parseInt(c4.getText().toString()));
            c.setIdUsuario(123);

            Toast.makeText(this, iC.insert(c), Toast.LENGTH_LONG).show();

        } catch (Exception e) {

            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /*
     *  ----------------------------------------------------------------------------------
     *   Funciones relacionadas
     *  ----------------------------------------------------------------------------------
     */

    // Valida el contenido de un edit text y retorna un entero
    public int valnum(EditText et) {
        int n = 0;
        if (et.getText().toString().length() > 0) {
            try {
                n = Integer.parseInt(et.getText().toString());
            } catch (Exception e) {

                Toast.makeText(this, "Error: Porque rayos hay una letra aqui?", Toast.LENGTH_LONG).show();
                n = 0;
            }
        }
        return n;
    }

}
