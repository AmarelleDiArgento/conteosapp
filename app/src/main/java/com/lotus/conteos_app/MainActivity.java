package com.lotus.conteos_app;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lotus.conteos_app.Config.Util.jsonAdmin;
import com.lotus.conteos_app.Model.iConteo;
import com.lotus.conteos_app.Model.tab.conteoTab;
import com.lotus.conteos_app.Model.tab.fenologiaTab;
import com.lotus.conteos_app.Model.tab.planoTab;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText siembra;

    // declaracion de variables campos
    EditText grados;
    EditText c1, c2, c3, c4, resulcode, dato_dia;
    Spinner cuadro, files;
    ImageView jpgView1, jpgView2, jpgView3, jpgView4;
    Button bt1, bt2, bt3, bt4;
    TextView info, data, tipo, finca, variedad, bloque, cama, fechaAct;
    EditText gdia;

    // Arreglo, desplegable (Spinner) cuadros
    String[] cuadros = {"1", "2", "3", "4", "5", "6", "7", "8"};
    // Arreglos globales Conteo y Plano
    List<conteoTab> cl = new ArrayList<>();
    List<planoTab> pl = new ArrayList<>();
    List<fenologiaTab> fl = new ArrayList<>();

    jsonAdmin ja = null;
    //imageAdmin ia = null;
    String path = null;

    int dia, hora, gradosDia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Asociacion de campos y botones

        cuadro = findViewById(R.id.cuadro_sp);

        c1 = findViewById(R.id.c1_et);
        c2 = findViewById(R.id.c2_et);
        c3 = findViewById(R.id.c3_et);
        c4 = findViewById(R.id.c4_et);

        jpgView1 = findViewById(R.id.feno1);
        jpgView2 = findViewById(R.id.feno2);
        jpgView3 = findViewById(R.id.feno3);
        jpgView4 = findViewById(R.id.feno4);

        dato_dia = findViewById(R.id.dato_dia);

        finca = findViewById(R.id.cam_finca);
        variedad = findViewById(R.id.cam_variedad);
        bloque = findViewById(R.id.cam_bloque);
        cama = findViewById(R.id.cam_cama);

        fechaAct = findViewById(R.id.fechaAct);

        //ADQUIRIENDO POR MEDIO DEL SHARED PREFERENCES LOS GRADOS DIA
        SharedPreferences guardardia = getBaseContext().getSharedPreferences("guardarRut", MODE_PRIVATE);
        String gDia = guardardia.getString("rut", "");
        dato_dia.setText(gDia);

        //ADQUIRIENDO EL DATO OBTENIDO DEL C. BARRAS Y ALMACENANDOLO EN EL CAMPO DE BUSQUEDA
        resulcode = findViewById(R.id.resulcode);

        try {
            Bundle bundle = getIntent().getExtras();

            if (bundle != null) {
                /*
                gradosDia = bundle.getInt("grados");

                if (gradosDia > 0) {
                    //dato_dia.setText(String.valueOf(gradosDia));
                }
                */
                int dato = bundle.getInt("codigo");

                if (dato > 0) {
                    resulcode.setText(String.valueOf(dato));
                    buscarSiembra();
                }

            } else {
                resulcode.setText("");
            }

        } catch (Exception EX) {
            Toast.makeText(this, "EXCEPTION: " + EX.toString(), Toast.LENGTH_LONG).show();
        }

        //OBTENIENDO LA FECHA ACTUAL
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final String strDate = sdf.format(c.getTime());
        String fecha = strDate;
        fechaAct.setText(String.valueOf(fecha));

        dia = c.get(Calendar.DAY_OF_WEEK) - 1;
        hora = c.get(Calendar.HOUR_OF_DAY);
        if (hora >= 12) {
            dia++;
        }

        // asociar arreglo cuadros al desplegable cuadro
        ArrayAdapter<String> cuadroArray = new ArrayAdapter<>(this, R.layout.spinner_item_personal, cuadros);
        cuadro.setAdapter(cuadroArray);

        //obtiene ruta donde se encuentran los archivos.
        path = getExternalFilesDir(null) + File.separator;
        ja = new jsonAdmin();
        // ia = new imageAdmin();

        // INICIAR LISTAS
        cargarPlanoLocal();
        cargarFenologiaLocal();
        // imagenes(12, 1);

        //listFiles();
    }

    // convierte el contenido del archivo plano.json en un arreglo (REQUIERE UNA LISTA GLOBAL)
    public void cargarPlanoLocal() {
        try {
            Gson gson = new Gson();
            pl = gson.fromJson(ja.ObtenerLista(path, "plano.json"), new TypeToken<List<planoTab>>() {
            }.getType());
        } catch (Exception e) {
            //data.setText(e.toString());
            Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void cargarFenologiaLocal() {
        try {
            Gson gson = new Gson();
            fl = gson.fromJson(ja.ObtenerLista(path, "fenologias.json"), new TypeToken<List<fenologiaTab>>() {
            }.getType());

        } catch (Exception e) {
            // data.setText(e.toString());
            Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    // busca el id de la siembra en el arreglo global y retorna la informacion en el text view info
    public void buscarSiembra() {

        finca = findViewById(R.id.cam_finca);
        variedad = findViewById(R.id.cam_variedad);
        bloque = findViewById(R.id.cam_bloque);
        cama = findViewById(R.id.cam_cama);

        int bs = valnum(resulcode);

        //Toast.makeText(this, "valor de codigo  " + bs, Toast.LENGTH_LONG).show();

        boolean infoS = false;


        if (pl != null || bs != 0) {
            for (planoTab p : pl) {
                if (p.getIdSiembra() == bs) {
                    infoS = true;
                    finca.setText(p.getFinca());
                    bloque.setText(p.getBloque());
                    variedad.setText(p.getVariedad());
                    cama.setText(p.getCama() + p.getSufijo());
                    Toast.makeText(this, p.getVariedad(), Toast.LENGTH_LONG).show();

                    imagenes(gradosDia, p.getIdVariedad());
                }
            }
            if (!infoS) {
                Toast.makeText(this, "Siembra no encontrada", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "Informacion invalida", Toast.LENGTH_LONG).show();
        }
    }

    //CARGAR LA IMAGEN

    public File getStoragePath(ImageView iv, String Variedad, String imagen) {
        try {

            File f = new File("/storage/extSdCard/" + Variedad + "/" + imagen);

            if (f.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());

                iv.setImageBitmap(bitmap);

                if (!f.getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath()) && f.isDirectory() && f.canRead()) {
                    return f;
                }
            } else {
                Toast.makeText(this, "No obtuvo la imagen", Toast.LENGTH_LONG).show();
            }
            return Environment.getExternalStorageDirectory();
        } catch (Exception ex) {
            Toast.makeText(this, "error" + ex.toString(), Toast.LENGTH_LONG).show();
        }

        return null;
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

            ja.CrearArchivo(path, nombre, cl.toString());
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
            cl = gson.fromJson(ja.ObtenerLista(path, fecha), new TypeToken<List<planoTab>>() {
            }.getType());

            Toast.makeText(this, cl.size(), Toast.LENGTH_LONG).show();

            for (conteoTab c : cl) {
                msj = msj + iC.insert(c);
            }
            Toast.makeText(this, msj, Toast.LENGTH_LONG).show();
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

    //INSTANCIAMOS LA CAMARA Y LA ACTIVA
    public void barcode(View v) {
        Intent intent = new Intent(v.getContext(), Camera.class);
        startActivityForResult(intent, 0);
    }


    public void imagenes(int g, int v) {
        try {
            int d = 7 - dia;

            int[] img = new int[4];
            img[0] = (d) * g;
            img[1] = (d + 7) * g;
            img[2] = (d + 21) * g;
            img[3] = (d + 28) * g;
            int c = 0;
            int max = 0;

            Toast.makeText(this, img[0] + " " + img[1] + " " + img[2] + " " + img[3], Toast.LENGTH_LONG).show();

            Iterator<fenologiaTab> i = fl.iterator();
            String data = "";
            List<fenologiaTab> fi = new ArrayList<>();
            fenologiaTab fu = new fenologiaTab();

            // Toast.makeText(this, "Imagenes", Toast.LENGTH_LONG).show();
            // Toast.makeText(this, "Data: " + fl.size(), Toast.LENGTH_LONG).show();

            while (i.hasNext()) {
                fenologiaTab f = i.next();
                if (f.getIdVariedad() == v) {
                    if (img[c] <= f.getGrados_dia()) {
                        fi.add(fu);
                        // Toast.makeText(this, "Data: " + f.toString(), Toast.LENGTH_LONG).show();

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

            this.getStoragePath(jpgView1, fi.get(0).getVariedad(), fi.get(0).getImagen());
            this.getStoragePath(jpgView2, fi.get(1).getVariedad(), fi.get(1).getImagen());
            this.getStoragePath(jpgView3, fi.get(2).getVariedad(), fi.get(2).getImagen());
            this.getStoragePath(jpgView4, fi.get(3).getVariedad(), fi.get(3).getImagen());


        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

        }

    }
}
