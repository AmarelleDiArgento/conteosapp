package com.lotus.conteos_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lotus.conteos_app.Model.iPlano;
import com.lotus.conteos_app.Model.tab.conteoTab;

import com.lotus.conteos_app.Model.iConteo;
import com.lotus.conteos_app.Model.tab.planoTab;

import com.google.gson.reflect.TypeToken;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class MainActivity extends AppCompatActivity {
    // Variable general para captura de codigos de barras
    private ZBarScannerView vbc;

    // declaracion de variables campos
    Spinner sp1, sp2, sp3;
    EditText siembra, grados, c1, c2, c3, c4;
    Spinner cuadro, files;
    ImageButton fen1, fen2;
    Button bt1, bt2, bt3, bt4;
    TextView info, data;

    // Arreglo, desplegable (Spinner) cuadros
    String[] cuadros = {"1", "2", "3", "4", "5", "6", "7", "8"};

    // Arreglos globales Conteo y Plano
    List<conteoTab> cl = new ArrayList<>();
    List<planoTab> pl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Asociacion de campos y botones
        siembra = (EditText) findViewById(R.id.siembra_et);
        grados = (EditText) findViewById(R.id.grados_et);
        cuadro = (Spinner) findViewById(R.id.cuadro_sp);
        files = (Spinner) findViewById(R.id.file_sp);
        c1 = (EditText) findViewById(R.id.c1_et);
        c2 = (EditText) findViewById(R.id.c2_et);
        c3 = (EditText) findViewById(R.id.c3_et);
        c4 = (EditText) findViewById(R.id.c4_et);
        fen1 = (ImageButton) findViewById(R.id.feno1_ib);
        fen2 = (ImageButton) findViewById(R.id.feno2_ib);
        bt1 = (Button) findViewById(R.id.button1);
        bt2 = (Button) findViewById(R.id.button2);
        bt3 = (Button) findViewById(R.id.button3);
        bt4 = (Button) findViewById(R.id.button4);

        info = (TextView) findViewById(R.id.info_tv);
        data = (TextView) findViewById(R.id.data_tvm);

        // asociar arreglo cuadros al desplegable cuadro
        ArrayAdapter<String> cuadroArray = new ArrayAdapter<>(this, R.layout.spinner_item_personal, cuadros);
        cuadro.setAdapter(cuadroArray);

        // iniciar listas
        actualizarPlano();
        cargarPlanoLocal();
        listFiles();
    }

    private void listFiles() {
        List<String> list = new ArrayList<String>();
        //obtiene ruta donde se encuentran los archivos.
        String path = getExternalFilesDir(null) + File.separator;
        File f = new File(path);
        //obtiene nombres de archivos dentro del directorio.
        File file[] = f.listFiles();
        for (int i = 0; i < file.length; i++) {
            Log.d("Files", "Archivo : " + file[i].getName());
            //Agrega nombres de archivos a List para ser agregado a adapter.
            if (!file[i].getName().equalsIgnoreCase("plano.json")) {

                list.add(file[i].getName());
            }
        }


        // asociar arreglo cuadros al desplegable cuadro
        ArrayAdapter<String> fl = new ArrayAdapter<>(this, R.layout.spinner_item_personal, list);
        files.setAdapter(fl);
        // data.setText(list.toString());
    }

    // lectura de codigo de barras
    // descarga el plano de la basde datos y lo alamcena en plano.json (Archivo local)
    public void actualizarPlano() {
        try {
            iPlano iP = new iPlano();
            String nombre = "plano";
            String contenido = iP.all().toString();
            if (CrearArchivo(nombre, contenido)) {
                Toast.makeText(this, "Plano generado exitosamente", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Error al generar el plano", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Plano local, no hay conexion a la base de datos", Toast.LENGTH_LONG).show();
        }
    }

    // convierte el contenido del archivo plano.json en un arreglo (REQUIERE UNA LISTA GLOBAL)
    public void cargarPlanoLocal() {
        try {

            Gson gson = new Gson();
            pl = gson.fromJson(ObtenerLista("plano.json"), new TypeToken<List<planoTab>>() {
            }.getType());

        } catch (Exception e) {
            data.setText(e.toString());
            Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();

        }
    }

    // busca el id de la siembra en el arreglo global y retorna la informacion en el text view info
    public void buscarSiembra(View v) {
        int bs = valnum(siembra);
        String infoS = "";

        if (pl != null || bs != 0) {
            for (planoTab p : pl) {
                if (p.getIdSiembra() == bs) {
                    infoS = "Finca: " + p.getFinca() + " Bloque: " + p.getBloque() + "  Variedad: " + p.getVariedad();
                }
            }

            if (!infoS.equalsIgnoreCase("")) {
                info.setText(infoS);
            } else {
                info.setText("");
                Toast.makeText(this, "Siembra no encontrada", Toast.LENGTH_LONG).show();
            }
        } else {
            info.setText("");
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

            CrearArchivo(nombre, nombre + ": " + cl.toString());
            listFiles();

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
            cl = gson.fromJson(ObtenerLista(fecha), new TypeToken<List<planoTab>>() {
            }.getType());

            Toast.makeText(this, cl.size(), Toast.LENGTH_LONG);

            for (conteoTab c : cl) {
                msj = msj + iC.insert(c);
            }
            Toast.makeText(this, msj, Toast.LENGTH_LONG);
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG);

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
        Toast.makeText(this,"hola tu",Toast.LENGTH_SHORT).show();
    }

    // Disminuir conteo de semana 1
    public void resFen1(View v) {

        int n = valnum(c1);
        if (n > 0) {
            c1.setText(String.valueOf(n - 1));
        }
    }

    // Aumentar conteo de semana 4
    public void sumFen2(View v) {
        int n = valnum(c4);
        c4.setText(String.valueOf(n + 1));

    }

    // Disminuir conteo de semana 4
    public void resFen2(View v) {
        int n = valnum(c4);
        if (n > 0) {
            c4.setText(String.valueOf(n - 1));
        }
    }

    /*
     *  ----------------------------------------------------------------------------------
     *   Funciones relacionadas
     *  ----------------------------------------------------------------------------------
     */

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

    // generador de archivos, requiere dos cadenas de texto nombre de archivo y contenido del mismo
    public boolean CrearArchivo(String nombre, String contenido) {
        boolean ok = false;
        try {
            FileOutputStream fos = null;
            File f = new File(getExternalFilesDir(null), nombre + ".json");
            fos = new FileOutputStream(f);
            fos.write(contenido.getBytes());
            fos.close();

            ok = true;
        } catch (Exception e) {
            data.setText(e.toString());
            ok = false;
        }
        return ok;
    }

    public String ObtenerLista(String nombre) {
        String jsonString = "";
        try {

            String path = getExternalFilesDir(null) + File.separator + nombre;
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            jsonString = sb.toString();

        } catch (Exception e) {
            //data.setText(e.toString());
            Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        return jsonString;
    }


    public void barcode(View v) {
        vbc = new ZBarScannerView(this);
        vbc.setResultHandler(new barcodeimp());
        setContentView(vbc);
        vbc.startCamera();
    }

    class barcodeimp implements ZBarScannerView.ResultHandler {

        @Override
        public void handleResult(Result result) {
            try {
                String bc = result.getContents();
                setContentView(R.layout.activity_main);
                vbc.stopCamera();
                siembra.setText(bc);
            } catch (Exception e) {
                Log.d("ERROR: ", e.toString());
            }
        }
    }
}
