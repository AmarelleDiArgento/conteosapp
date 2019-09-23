package com.lotus.conteos_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import java.util.List;

import static com.lotus.conteos_app.R.drawable.flor;
import static java.lang.String.valueOf;


public class MainActivity extends AppCompatActivity {

    SharedPreferences sp = null;
    planoTab p = null;

    float gDia;
    EditText c1, c4, IdSiembra, codebar,total;
    Spinner cuadro;
    ImageView jpgView1, jpgView2, jpgView3, jpgView4;
    TextView gradoDia, finca, variedad, bloque, cama, fechaAct, usuario, NoArea, NoPlantas, NoCuadros, idusuario;

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
        getSupportActionBar().hide();
        try {
            sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);
            codebar = (EditText) findViewById(R.id.resulcode);

            gradoDia = (TextView) findViewById(R.id.gradodia);
            gradoDia.setText(valueOf(getGradoDia()));
            String gD = String.valueOf(sp.getFloat("gradoDia", 0));
            IdSiembra = (EditText) findViewById(R.id.resulcode);
            c1 = (EditText) findViewById(R.id.c1et);
            c4 = (EditText) findViewById(R.id.c4et);
            total = (EditText) findViewById(R.id.total);

            usuario = findViewById(R.id.usuLog);
            usuario.setText(sp.getString("nombre", ""));

            idusuario = findViewById(R.id.idusuario);
            idusuario.setText(sp.getString("codigo",""));

            IdSiembra.setSelectAllOnFocus(true);
            c1.setSelectAllOnFocus(true);
            c4.setSelectAllOnFocus(true);

            IdSiembra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.clearFocus();
                    view.requestFocus();
                }
            });
            c1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.clearFocus();
                    view.requestFocus();
                }
            });
            c4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.clearFocus();
                    view.requestFocus();
                }
            });



            cuadro = (Spinner) findViewById(R.id.cuadrosp);
            // Arreglo, desplegable (Spinner) cuadros
            String[] cuadros = {"1", "2", "3", "4", "5", "6", "7", "8"};

            // asociar arreglo cuadros al desplegable cuadro
            ArrayAdapter<String> cuadroArray = new ArrayAdapter<>(this, R.layout.spinner_item_personal, cuadros);
            cuadro.setAdapter(cuadroArray);

            usuario = (TextView) findViewById(R.id.usuLog);

            jpgView1 = (ImageView) findViewById(R.id.feno1);
            jpgView2 = (ImageView) findViewById(R.id.feno2);
            jpgView3 = (ImageView) findViewById(R.id.feno3);
            jpgView4 = (ImageView) findViewById(R.id.feno4);

            finca = (TextView) findViewById(R.id.cam_finca);
            variedad = (TextView) findViewById(R.id.cam_variedad);
            bloque = (TextView) findViewById(R.id.cam_bloque);
            cama = (TextView) findViewById(R.id.cam_cama);
            NoArea = (TextView) findViewById(R.id.textViewNoArea);
            NoCuadros = (TextView) findViewById(R.id.textViewNoTotalCuadros);
            NoPlantas = (TextView) findViewById(R.id.textViewNoPlantas);

            fechaAct = (TextView) findViewById(R.id.fechaAct);

            getDate();
            cargarRecursos();
            codebar.setText("0");
            c4.setText("0");
            c1.setText("0");
            total.setText("0");

            class MyKeyListerner implements View.OnKeyListener{
                public  boolean onKey(View v,int keyCode, KeyEvent event){
                    if((event.getAction()==KeyEvent.ACTION_DOWN) && (keyCode ==  KeyEvent.KEYCODE_ENTER)){
                        //Toast.makeText(Login.this,"se oprimio el boton",Toast.LENGTH_SHORT).show();
                        buscarSiembra(0);
                        return true;
                    }
                    return  false;
                }
            }

            View.OnKeyListener listener = new MyKeyListerner();
            IdSiembra.setOnKeyListener(listener);

        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getCodeBar();
        try {
            int campo_code = Integer.parseInt(codebar.getText().toString());
            if (campo_code>0) {
                buscarSiembra(campo_code);
            } else if (campo_code==0) {
                /*Toast toast = Toast.makeText(this, "no hay ID de la siembra para consultar \n" +
                        "por favor realiza una busqueda...", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 100);
                toast.show();*/
            }else {
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Exception 0:     " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void cargarRecursos() {

        path = getExternalFilesDir(null) + File.separator;
        //path = "/storage/extSdCard/";

        try {

            SimpleDateFormat sdfn = new SimpleDateFormat("ddMMyyyy");


            iP = new iPlano(path);
            iF = new iFenologia(path);
            iC = new iConteo(path);
            iC.nombre = sdfn.format(calendarDate.getTime());

            lp = iP.all();
            lf = iF.all();
            lc = iC.all();
            //Toast.makeText(this, "Tamano: " + lc.size(), Toast.LENGTH_LONG).show();
            //Toast.makeText(this, iC.all().toString(),Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            //Toast.makeText(this, "Error de recursos: \n" + e.toString(), Toast.LENGTH_LONG).show();
        }
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

    //OBTENIENDO LOS GRADOS DIA CON SHARED PREFERENCES
    public float getGradoDia() {
       try{
           if (gradoDia != null) {
               gDia = sp.getFloat("gradoDia", 0);
               return gDia;
           } else {
               return 0;
           }
       }catch (Exception e){
           Toast.makeText(this,"Error el gdia \n"+e,Toast.LENGTH_SHORT).show();
       }
        return 0;
    }

    //OBTENIENDO EL CODIGO DE BARRAS DESDE LA CAMARA
    public void getCodeBar() {
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                String d = bundle.getString("codigo");
                String[] dato = d.split(",");
                if (dato!=null) {
                    codebar.setText(dato[0]);
                }
            } else {
                //Toast.makeText(this, "no hay dato para consultar", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception EX) {
            Toast.makeText(this, "EXCEPTION: " + EX.toString(), Toast.LENGTH_LONG).show();
        }
    }

    //BOTON (VERDE) PARA ACTIVAR LA CAMARA CODEBAR
    public void barcode(View v) {
        Intent intent = new Intent(v.getContext(), Camera.class);
        startActivityForResult(intent, 0);
    }

    //BOTON (AZUL LUPA) BUSCAR SIEMBRA
    public void btnBuscar(View v) {
        int bs = Integer.parseInt(codebar.getText().toString());
        buscarSiembra(bs);

        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(codebar.getWindowToken(), 0);
    }

    //REALIZA EL FILTRO DE BUSQUEDA SIEMBRAS
    public void buscarSiembra(int bs) {
        try {

            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(codebar.getWindowToken(), 0);

            long id = Long.parseLong(IdSiembra.getText().toString());
            p = iP.OneforIdSiembra(id);

            if (p != null) {
                String s;
                if (p.getIdSiembra() != null) {
                    s = p.getCama() + p.getSufijo();
                } else {
                    s = valueOf(p.getCama());
                }

                finca.setText(p.getFinca());
                variedad.setText(p.getVariedad());
                bloque.setText(p.getBloque());
                cama.setText(s);
                NoArea.setText(String.valueOf(p.getArea()));
                NoCuadros.setText(String.valueOf(p.getCuadros()));
                NoPlantas.setText(String.valueOf(p.getPlantas()));
                cargarImagenes(p.getIdVariedad());

            }else {
                Toast toast = Toast.makeText(getApplicationContext(), "Lo sentimos pero no se encuentra la siembra", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP,0,100);
                toast.show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error busqueda" + e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    //REALIZA LA CARGAS DE IMAGEN SEGUN FENOLOFIA
    public void cargarImagenes(int idVariedad) {

        if((jpgView3.getDrawable() == null) && (jpgView4.getDrawable() == null)){
            jpgView3.setImageResource(R.drawable.noimagen);
            jpgView4.setImageResource(R.drawable.noimagen);
        }

        if((jpgView1.getDrawable() == null) && (jpgView2.getDrawable() == null)){
            jpgView1.setImageResource(R.drawable.noimagen);
            jpgView2.setImageResource(R.drawable.noimagen);
        }

        try {

            imageAdmin iA = new imageAdmin();
            List<fenologiaTab> fi = iF.forGrado(dia, gDia, idVariedad);

            iA.getImage(jpgView1, fi.get(0).getVariedad(), fi.get(0).getImagen());
            iA.getImage(jpgView2, fi.get(1).getVariedad(), fi.get(1).getImagen());
            iA.getImage(jpgView3, fi.get(2).getVariedad(), fi.get(3).getImagen());
            iA.getImage(jpgView4, fi.get(3).getVariedad(), fi.get(2).getImagen());

            String variedad =fi.get(0).getVariedad();
            String imagen =fi.get(0).getImagen();
            String imagen2 =fi.get(1).getImagen();
            String imagen3 =fi.get(2).getImagen();
            String imagen4 =fi.get(3).getImagen();

            SharedPreferences.Editor edit = sp.edit();
            edit.putString("variedad", variedad);
            edit.putString("fotoImagen", imagen);
            edit.putString("fotoImagen2", imagen2);
            edit.putString("fotoImagen3", imagen3);
            edit.putString("fotoImagen4", imagen4);
            edit.putFloat("gDia",gDia);
            edit.putInt("dia",dia);
            edit.putInt("IdVariedad",idVariedad);
            edit.commit();
            edit.apply();



        } catch (Exception e) {
            //Toast toast = Toast.makeText(getApplicationContext(), "Error \n"+e, Toast.LENGTH_LONG);
            //toast.setGravity(Gravity.TOP,0,100);
            //toast.show();
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
        c1.setText(valueOf(n + 1));
    }

    // Disminuir conteo de semana 1
    public void resFen1(View v) {
        int n = valnum(c1);
        if (n > 0) {
            c1.setText(valueOf(n - 1));

        }
    }

    // Aumentar conteo de semana 4
    public void sumFen2(View v) {
        // Toast.makeText(this, "hola btn3", Toast.LENGTH_SHORT).show();
        int n = valnum(c4);
        c4.setText(valueOf(n + 1));
    }

    // Disminuir conteo de semana 4
    public void resFen2(View v) {
        int n = valnum(c4);
        if (n > 0) {
            c4.setText(valueOf(n - 1));

        }
    }

    //BOTON PARA REALIZAR RESTRO DE LOS CONTEOS SEGUN ID SIEMBRA
    public void registrarConteo(View v) {

        try {
            int Tot = Integer.parseInt(total.getText().toString());
            String var = variedad.getText().toString();
            int Siembraval = Integer.parseInt(IdSiembra.getText().toString());


            if (Siembraval==0) {
                Toast toast = Toast.makeText(getApplicationContext(), "Lo sentimos, no es posible realizar un registro \n por favor reliza una busqueda", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP,0,100);
                toast.show();
            }else if(Tot==0){
                Toast toast = Toast.makeText(getApplicationContext(), "Lo sentimos, no es posible realizar un registro\n por favor llena el campo total", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP,0,100);
                toast.show();
            } else {
                int siembra = Integer.parseInt(IdSiembra.getText().toString());
                if (siembra > 0) {

                    conteoTab c = new conteoTab();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    c.setIdConteo(c.getIdConteo());
                    c.setIdSiembra(p.getIdSiembra());
                    c.setCama(p.getCama());
                    c.setFecha(sdf.format(calendarDate.getTime()));
                    c.setIdBloque(p.getIdBloque());
                    c.setBloque(p.getBloque());
                    c.setIdVariedad(p.getIdVariedad());
                    c.setVariedad(p.getVariedad());
                    c.setCuadro(Integer.parseInt(cuadro.getSelectedItem().toString()));

                    c.setConteo1(Integer.parseInt(c1.getText().toString()));
                    c.setConteo2(0);
                    c.setConteo3(0);
                    c.setConteo4(Integer.parseInt(c4.getText().toString()));
                    c.setTotal(Integer.parseInt(total.getText().toString()));

                    c.setPlantas(p.getPlantas());
                    c.setArea(p.getArea());
                    c.setCuadros(p.getCuadros());

                    int usu= Integer.parseInt(idusuario.getText().toString());
                    c.setIdUsuario(usu);

                    Toast.makeText(this, iC.insert(c), Toast.LENGTH_LONG).show();
                    //Toast.makeText(this, iC.all().toString(),Toast.LENGTH_LONG).show();

                    //obteniendo posicion del spinner(Cuadro)
                    int size = Integer.parseInt(cuadro.getSelectedItem().toString());

                    //validacion de la posicion del cuadro
                    if (size == 8) {
                        cuadro.setSelection(0);
                        cuadro.setEnabled(true);
                        c1.setText(valueOf(0));
                        c4.setText(valueOf(0));
                    } else {
                        int posNext = size++;
                        Toast.makeText(this, "En este momento estas sobre el cuadro " + size, Toast.LENGTH_LONG).show();

                        cuadro.setSelection(posNext);

                        c1.setText(valueOf(0));
                        c4.setText(valueOf(0));
                    }

                } else{}
            }

        } catch (Exception e) {

            Toast.makeText(this,"hola"+ e.toString(), Toast.LENGTH_LONG).show();
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

    // botones para visualizar las imagenes
    //semana 1
    public void btn_visual1(View v){


        if((jpgView1.getDrawable() == null) && (jpgView2.getDrawable() == null) && (jpgView3.getDrawable() == null) && (jpgView4.getDrawable() == null)) {
            Toast toast = Toast.makeText(getApplicationContext(), "No se pueden cargar las imagenes, por que no has realizado una busqueda", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 100);
            toast.show();
        } else {
            Intent i = new Intent(MainActivity.this, VisualFenoActivity.class);
            startActivity(i);
        }

    }


    //PARA VOLVER A LA ACTIVIDAD ANTERIOR(CAMARA)
    public void onBackPressed() {
        Intent i = new Intent(MainActivity.this, HistorialMainActivity.class);
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        Toast.makeText(this,"btn back ",Toast.LENGTH_SHORT).show();
        finish();
        System.exit(0);
    }

}
