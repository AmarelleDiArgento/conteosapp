package com.lotus.conteos_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.lotus.conteos_app.Config.Util.jsonAdmin;
import com.lotus.conteos_app.Config.Util.imageAdmin;
import com.lotus.conteos_app.Model.iPlano;
import com.lotus.conteos_app.Model.tab.conteoTab;

import com.lotus.conteos_app.Model.iConteo;
import com.lotus.conteos_app.Model.tab.planoTab;

import com.google.gson.reflect.TypeToken;

import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class MainActivity extends AppCompatActivity {
    // Variable general para captura de codigos de barras
    private ZBarScannerView vbc;

    // declaracion de variables campos
    Spinner sp1, sp2, sp3;
    EditText siembra;
    EditText grados;
    EditText c1;
    EditText c2;
    EditText c3;
    EditText c4;
    EditText err;
    EditText resulcode;
    Spinner cuadro, files;
    ImageView fen1, fen2;
    Button bt1, bt2, bt3, bt4;
    TextView info, data, tipo, finca, variedad, bloque, cama, fechaAct;

    EditText gdia;

    // Arreglo, desplegable (Spinner) cuadros
    String[] cuadros = {"1", "2", "3", "4", "5", "6", "7", "8"};

    // Arreglos globales Conteo y Plano
    List<conteoTab> cl = new ArrayList<>();
    List<planoTab> pl = new ArrayList<>();

    /*

     */
    jsonAdmin ja = null;
    //imageAdmin ia = null;
    String path = null;

    String fec = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fechaAct = (TextView) findViewById(R.id.fechaAct);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final String strDate = sdf.format(c.getTime());
        String fecha = strDate;
        fechaAct.setText(String.valueOf(fecha));

        int dia = c.get(Calendar.DAY_OF_WEEK) -1;
        int hora = c.get(Calendar.HOUR_OF_DAY);
        if (hora >= 12) {
            dia++;
        }

        Toast.makeText(this, "Dia: " + dia, Toast.LENGTH_LONG).show();

        // Asociacion de campos y botones
        siembra = (EditText) findViewById(R.id.siembra_et);
        grados = (EditText) findViewById(R.id.grados_et);
        cuadro = (Spinner) findViewById(R.id.cuadro_sp);
        files = (Spinner) findViewById(R.id.file_sp);
        c1 = (EditText) findViewById(R.id.c1_et);
        c2 = (EditText) findViewById(R.id.c2_et);
        c3 = (EditText) findViewById(R.id.c3_et);
        c4 = (EditText) findViewById(R.id.c4_et);
        fen1 = (ImageView) findViewById(R.id.feno1_ib);
        fen2 = (ImageView) findViewById(R.id.feno2_ib);
        bt1 = (Button) findViewById(R.id.button1);
        bt2 = (Button) findViewById(R.id.button2);
        bt3 = (Button) findViewById(R.id.button3);
        bt4 = (Button) findViewById(R.id.button4);

        info = (TextView) findViewById(R.id.info_tv);
        data = (TextView) findViewById(R.id.data_tbl);
        resulcode = (EditText) findViewById(R.id.resulcode);

        finca = (TextView) findViewById(R.id.cam_finca);
        variedad = (TextView) findViewById(R.id.cam_variedad);
        bloque = (TextView) findViewById(R.id.cam_bloque);
        cama = (TextView) findViewById(R.id.cam_cama);
        fechaAct = (TextView) findViewById(R.id.fechaAct);


        // asociar arreglo cuadros al desplegable cuadro
        ArrayAdapter<String> cuadroArray = new ArrayAdapter<>(this, R.layout.spinner_item_personal, cuadros);
        cuadro.setAdapter(cuadroArray);

        //obtiene ruta donde se encuentran los archivos.
        path = getExternalFilesDir(null) + File.separator;
        ja = new jsonAdmin(path);
        // ia = new imageAdmin();

        // iniciar listas
        actualizarPlano();
        cargarPlanoLocal();
        //listFiles();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "OnResume AM", Toast.LENGTH_SHORT).show();
        // La actividad se ha vuelto visible (ahora se "reanuda").
        //resibir_dato();
    }


    public void resibir_dato() {
        Toast.makeText(this, "entro al metodo recibir", Toast.LENGTH_LONG).show();

        try {
            Intent intentReceived = getIntent();
            Bundle data = intentReceived.getExtras();
            Toast.makeText(this, "VALOR " + data, Toast.LENGTH_LONG).show();
            if (data != null) {
                String res = data.getString("dato01");
                Toast.makeText(this, " el valor es " + res, Toast.LENGTH_LONG).show();
                resulcode.setText(res);
            } else {
                Toast.makeText(this, " el valor esta vacio ", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, " problema " + ex, Toast.LENGTH_LONG).show();
            resulcode.setText(ex.toString());
        }
    }

    //GUARDAMOS INSTANCIA PARA LOS DATOS DE LOS CAMPOS

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        final TextView fecha = (TextView) findViewById(R.id.fechaAct);
        CharSequence datfec = fecha.getText();
        Toast.makeText(this, "save   " + datfec, Toast.LENGTH_SHORT).show();
        outState.putCharSequence("hola", datfec);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        final TextView fecha = (TextView) findViewById(R.id.fechaAct);
        CharSequence userText = savedInstanceState.getString("hola");
        Toast.makeText(this, "restore", Toast.LENGTH_SHORT).show();
        fecha.setText(userText);
    }

    private void listFiles() {
        try {
            List<String> list = ja.listFiles();
            // asociar arreglo cuadros al desplegable cuadro
            ArrayAdapter<String> fl = new ArrayAdapter<>(this, R.layout.spinner_item_personal, list);
            files.setAdapter(fl);
            // data.setText(list.toString());
        } catch (Exception e) {
            Toast.makeText(this, "LISTFILES()--->  " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    // descarga el plano de la basde datos y lo alamcena en plano.json (Archivo local)
    public void actualizarPlano() {

        try {
            iPlano iP = new iPlano();
            String nombre = "plano";
            String contenido = iP.all().toString();
            // resulcode.setText(contenido);

            // Toast.makeText(this, "xcv----  " + contenido, Toast.LENGTH_LONG).show();

            if (ja.CrearArchivo(nombre, contenido)) {
                Toast.makeText(this, "Plano generado exitosamente", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Error al generar el plano", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Plano local, error  " + e.toString(), Toast.LENGTH_LONG).show();
            //err.setText(e.toString());

        }
    }

    // convierte el contenido del archivo plano.json en un arreglo (REQUIERE UNA LISTA GLOBAL)
    public void cargarPlanoLocal() {
        try {
            Gson gson = new Gson();
            pl = gson.fromJson(ja.ObtenerLista("plano.json"), new TypeToken<List<planoTab>>() {
            }.getType());

        } catch (Exception e) {
            //data.setText(e.toString());
            Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    // busca el id de la siembra en el arreglo global y retorna la informacion en el text view info
    public void buscarSiembra() {

        // setContentView(R.layout.activity_main);

        finca = (TextView) findViewById(R.id.cam_finca);
        variedad = (TextView) findViewById(R.id.cam_variedad);
        bloque = (TextView) findViewById(R.id.cam_bloque);
        cama = (TextView) findViewById(R.id.cam_cama);

        int bs = valnum(resulcode);

//        Toast.makeText(this, "entro a la siembra" + bs, Toast.LENGTH_SHORT).show();

        boolean infoS = false;

        if (pl != null || bs != 0) {
//            Toast.makeText(this, "se listan " + bs, Toast.LENGTH_LONG).show();
//            Toast.makeText(this, "se listan " + pl, Toast.LENGTH_LONG).show();
            for (planoTab p : pl) {

                if (p.getIdSiembra() == bs) {
//                    Toast.makeText(this, "se listan " + p.getIdFinca(), Toast.LENGTH_LONG).show();
//                    Toast.makeText(this, "se listan " + p.getSufijo(), Toast.LENGTH_LONG).show();

                    infoS = true;
                    finca.setText(p.getFinca());
                    bloque.setText(p.getBloque());
                    variedad.setText(p.getVariedad());
                    cama.setText(p.getCama() + p.getSufijo());

//                    Toast.makeText(this, "consulta" + variedad, Toast.LENGTH_LONG).show();
                }
            }

            if (!infoS) {
                Toast.makeText(this, "Siembra no encontrada", Toast.LENGTH_LONG).show();
            }

        } else {
            //info.setText("");
            Toast.makeText(this, "Informacion invalida", Toast.LENGTH_LONG).show();
        }


    }

    // registra conteos en arreglo local (REQUIERE UNA LISTA GLOBAL)
    public void registrarConteo(View v) {
        Date hoy = new Date();

        try {
            conteoTab c = new conteoTab();
            c.setIdSiembra(Long.parseLong(siembra.getText().toString()));
            c.setFecha(hoy);
            c.setCuadro(Integer.parseInt(cuadro.getSelectedItem().toString()));
            c.setConteo1(Integer.parseInt(c1.getText().toString()));
            c.setConteo2(0);
            c.setConteo3(0);
            c.setConteo4(Integer.parseInt(c4.getText().toString()));
            c.setIdUsuario(123);
            cl.add(c);
            data.setText(cl.toString());
            String nombre = hoy();

            ja.CrearArchivo(nombre, cl.toString());
            //listFiles();

        } catch (Exception e) {

            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void btnReg(View v) {
        try {
            String nombre = files.getSelectedItem().toString();
            registarBdConteos(nombre);

        } catch (Exception e) {

            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void registarBdConteos(String fecha) {
        try {
            iConteo iC = new iConteo();
            String msj = "";

            Gson gson = new Gson();
            cl = gson.fromJson(ja.ObtenerLista(fecha), new TypeToken<List<planoTab>>() {
            }.getType());

            Toast.makeText(this, cl.size(), Toast.LENGTH_LONG).show();

            for (conteoTab c : cl) {
                msj = msj + iC.insert(c);
            }
            Toast.makeText(this, msj, Toast.LENGTH_LONG).show();
            ;
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            ;

        }

    }

    /*
     *  ----------------------------------------------------------------------------------
     *   Registro de conteos segun criterio
     *  ----------------------------------------------------------------------------------
     */

    // Aumentar conteo de semana 1
    public void sumFen1(View v) {
        int n = valnum(c1);
        c1.setText(String.valueOf(n + 1));
        //Toast.makeText(this, "hola btn1", Toast.LENGTH_SHORT).show();
    }

    // Disminuir conteo de semana 1
    public void resFen1(View v) {

        int n = valnum(c1);
        if (n > 0) {
            c1.setText(String.valueOf(n - 1));
            //Toast.makeText(this, "hola btn2", Toast.LENGTH_SHORT).show();
        }
    }

    // Aumentar conteo de semana 4
    public void sumFen2(View v) {
        int n = valnum(c4);
        c4.setText(String.valueOf(n + 1));
        //Toast.makeText(this, "hola btn3", Toast.LENGTH_SHORT).show();
    }

    // Disminuir conteo de semana 4
    public void resFen2(View v) {
        int n = valnum(c4);
        if (n > 0) {
            c4.setText(String.valueOf(n - 1));
            //Toast.makeText(this, "hola btn4", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     *  ----------------------------------------------------------------------------------
     *   Funciones relacionadas
     *  ----------------------------------------------------------------------------------
     */

    //btn buscar siembra
    public void btn_buscar(View v) {
        buscarSiembra();
    }


    // generar nombre de archivo json segun fecha de registro AAAA-MM-DD
    public String hoy() throws Exception {
        Date hoy = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(hoy);
    }

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


    public void barcode(View v) {
        Intent intent = new Intent(v.getContext(), Camera.class);
        startActivityForResult(intent, 0);
    }


    //PARA VOLVER A LA ACTIVIDAD ANTERIOR(CAMARA)
    public void onBackPressed() {
        //Toast.makeText(this,"se retrocedio",Toast.LENGTH_LONG).show();
        Intent i = new Intent(MainActivity.this, MainActivity.class);
        startActivity(i);
        vbc.stopCamera();//aqui apaga la camara
    }


    //CARGAR LA IMAGEN

    public void img_cargar(View v) {
        Toast.makeText(this, "btn carga", Toast.LENGTH_SHORT).show();
        this.getStoragePath();
    }

    File getStoragePath() {
        gdia = (EditText) findViewById(R.id.dato_dia);
        ImageView jpgView1 = (ImageView) findViewById(R.id.feno1_ib);
        ImageView jpgView2 = (ImageView) findViewById(R.id.feno2_ib);
        ImageView jpgView3 = (ImageView) findViewById(R.id.resFen1_bt);
        ImageView jpgView4 = (ImageView) findViewById(R.id.resFen2_bt);

        try {
            String dia = gdia.getText().toString();//OBTENIENDO NOMBRE DE LA FOTO

            if (dia == "9.94") {

            } else if (dia == "19.66") {


                File file1a = new File("/storage/extSdCard/LOST.DIR/flor1.JPG");
                File file1b = new File("/storage/extSdCard/LOST.DIR/flor2.JPG");
                File file1c = new File("/storage/extSdCard/LOST.DIR/flor3.JPG");
                File file1d = new File("/storage/extSdCard/LOST.DIR/flor4.JPG");

                if (file1a.exists() && file1b.exists() && file1c.exists() && file1d.exists()) {

                    Bitmap mybit1 = BitmapFactory.decodeFile(file1a.getPath());
                    jpgView1.setImageBitmap(mybit1);


                    Bitmap mybit2 = BitmapFactory.decodeFile(file1b.getPath());
                    jpgView2.setImageBitmap(mybit2);


                    Bitmap mybit3 = BitmapFactory.decodeFile(file1c.getPath());
                    jpgView3.setImageBitmap(mybit3);


                    Bitmap mybit4 = BitmapFactory.decodeFile(file1d.getPath());
                    jpgView4.setImageBitmap(mybit4);



                    if (!file1a.getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath()) && file1a.isDirectory() && file1a.canRead()) {
                        return file1a;
                    }

                    if (!file1b.getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath()) && file1b.isDirectory() && file1b.canRead()) {
                        return file1b;
                    }

                    if (!file1c.getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath()) && file1c.isDirectory() && file1c.canRead()) {
                        return file1c;
                    }

                    if (!file1d.getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath()) && file1d.isDirectory() && file1d.canRead()) {
                        return file1d;
                    }
                } else {
                    Toast.makeText(this, "no obtuvo la imagen", Toast.LENGTH_LONG).show();
                }
            }

            File file = new File("/storage/extSdCard/LOST.DIR/" + dia + ".JPG");

            return Environment.getExternalStorageDirectory();
        } catch (Exception ex) {
            Toast.makeText(this, "error" + ex, Toast.LENGTH_LONG).show();
        }

        return null;
    }
}
