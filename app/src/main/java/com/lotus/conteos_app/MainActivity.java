package com.lotus.conteos_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lotus.conteos_app.Config.Util.imageAdmin;
import com.lotus.conteos_app.Config.Util.jsonAdmin;
import com.lotus.conteos_app.Model.iCuadrosBloque;
import com.lotus.conteos_app.Model.iPlano;
import com.lotus.conteos_app.Model.iFenologia;
import com.lotus.conteos_app.Model.iConteo;
import com.lotus.conteos_app.Model.tab.cuadros_bloqueTab;
import com.lotus.conteos_app.Model.tab.planoTab;
import com.lotus.conteos_app.Model.tab.fenologiaTab;
import com.lotus.conteos_app.Model.tab.conteoTab;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import static com.lotus.conteos_app.R.drawable.flor;
import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp = null;
    planoTab p = null;
    cuadros_bloqueTab ab = null;

    float gDia;
    EditText c1, c4, IdSiembra, codebar, total;
    Spinner cuadro;
    ImageView jpgView1, jpgView2, jpgView3, jpgView4;
    TextView gradoDia, finca, variedad, bloque, cama, fechaAct, usuario, NoArea, NoPlantas, NoCuadros, idusuario;

    List<planoTab> lp = new ArrayList<>();
    List<fenologiaTab> lf = new ArrayList<>();
    List<conteoTab> lc = new ArrayList<>();
    List<cuadros_bloqueTab> lcb = new ArrayList<>();

    iPlano iP = null;
    iFenologia iF = null;
    iConteo iC = null;
    iCuadrosBloque iCB = null;

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
            codebar = findViewById(R.id.resulcode);

            gradoDia = findViewById(R.id.gradodia);
            gradoDia.setText(valueOf(getGradoDia()));
            IdSiembra = findViewById(R.id.resulcode);
            c1 = findViewById(R.id.c1et);
            c4 = findViewById(R.id.c4et);
            total = findViewById(R.id.total);

            idusuario = findViewById(R.id.idusuario);
            idusuario.setText(sp.getString("codigo", ""));

            c1.setSelectAllOnFocus(true);
            c4.setSelectAllOnFocus(true);
            total.setSelectAllOnFocus(true);

            IdSiembra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IdSiembra.setSelectAllOnFocus(true);
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
            total.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.clearFocus();
                    view.requestFocus();
                }
            });


            cuadro = findViewById(R.id.cuadrosp);
            // Arreglo, desplegable (Spinner) cuadros
            String[] cuadros = {"1", "2", "3", "4", "5", "6", "7", "8"};

            // asociar arreglo cuadros al desplegable cuadro
            ArrayAdapter<String> cuadroArray = new ArrayAdapter<>(this, R.layout.spinner_item_personal, cuadros);
            cuadro.setAdapter(cuadroArray);


            usuario = findViewById(R.id.usuLog);

            jpgView1 = findViewById(R.id.feno1);
            jpgView2 = findViewById(R.id.feno2);
            jpgView3 = findViewById(R.id.feno3);
            jpgView4 = findViewById(R.id.feno4);

            finca = findViewById(R.id.cam_finca);
            variedad = findViewById(R.id.cam_variedad);
            bloque = findViewById(R.id.cam_bloque);
            cama = findViewById(R.id.cam_cama);
            NoArea = findViewById(R.id.textViewNoArea);
            NoPlantas = findViewById(R.id.textViewNoPlantas);
            NoCuadros = findViewById(R.id.textViewNoTotalCuadros);

            fechaAct = findViewById(R.id.fechaAct);

            getDate();
            cargarRecursos();
            //codebar.setText("0");
            c4.setText("0");
            c1.setText("0");
            total.setText("0");


            class MyKeyListerner implements View.OnKeyListener {
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if(!IdSiembra.getText().toString().isEmpty()){
                    long id = Long.parseLong(IdSiembra.getText().toString());

                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            buscarSiembra(id);
                            bajarTeclado();
                            return true;
                        }
                        else {
                            return false;
                        }
                    }else{
                        return false;
                    }

                }
            }

            View.OnKeyListener listener = new MyKeyListerner();
            IdSiembra.setOnKeyListener(listener);

            requestConteoTotal();

            boolean camaraActivada = false;


        } catch (Exception e) {
            Toast.makeText(this,"onCreate \n"+ e.toString(), Toast.LENGTH_LONG).show();
        }

    }


    @Override protected void onResume() {
        super.onResume();
        getCodeBar();
        try {
            if(!codebar.getText().toString().isEmpty()) {
                long campo_code = Long.parseLong(codebar.getText().toString());
                if (campo_code <= 0) {
                } else {
                    buscarSiembra(campo_code);
                    c1.requestFocus();
                }
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Exception onResume:     " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override protected void onStop() {
        super.onStop();
    }

    @Override protected void onRestart() {
        super.onRestart();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }

    //SHARED PREFERENCES
    public void saveState() {
        try{
            int valueSpinner = cuadro.getSelectedItemPosition();

            SharedPreferences.Editor edit = sp.edit();
            edit.putInt("sp_spinner_cuadro", valueSpinner);
            edit.commit();
            edit.apply();

        }catch (Exception e){
            Toast.makeText(this,"¡¡NO SE GUARDO CORRECTAMENTE EL ESTADO DE LA ACTIVIDAD \n \n"+e,Toast.LENGTH_SHORT).show();
        }
    }


    private void cargarRecursos() {

        path = getExternalFilesDir(null) + File.separator;
        try {

            SimpleDateFormat sdfn = new SimpleDateFormat("ddMMyyyy");

            iP = new iPlano(path);
            iF = new iFenologia(path);
            iC = new iConteo(path);
            iC.nombre = sdfn.format(calendarDate.getTime());
            iCB = new iCuadrosBloque(path);

            lp = iP.all();
            lf = iF.all();
            lc = iC.all();
            lcb = iCB.all();


        } catch (Exception e) {
        }
    }

    //OBTENIENDO LA FECHA ACTUAL
    public void getDate() {

        calendarDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        fecha = sdf.format(calendarDate.getTime());


        usuario = findViewById(R.id.usuLog);
        String usu = sp.getString("nombre", "");
        usuario.setText("Fecha: " + fecha + "\nUsuario: " + usu);

        fechaAct.setText(fecha);

        dia = calendarDate.get(Calendar.DAY_OF_WEEK) - 1;

        hora = calendarDate.get(Calendar.HOUR_OF_DAY);

        if (hora >= 12) {
            dia++;
        }

    }

    //OBTENIENDO LOS GRADOS DIA CON SHARED PREFERENCES
    public float getGradoDia() {
        try {
            if (gradoDia != null) {
                gDia = sp.getFloat("gradoDia", 0);
                return gDia;
            } else {
                return 0;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error el gdia \n" + e, Toast.LENGTH_SHORT).show();
        }
        return 0;
    }

    //OBTENIENDO EL CODIGO DE BARRAS DESDE LA CAMARA
    public void getCodeBar() {
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                String d = bundle.getString("codigo");
                boolean camaraact = bundle.getBoolean("camaraActivada");

                if(camaraact){
                    cuadro.setSelection(sp.getInt("sp_spinner_cuadro",0));
                }else{
                    cuadro.setSelection(1);
                }

                String[] dato = d.split(",");
                if (dato != null) {
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
        try {
            Intent intent = new Intent(v.getContext(), Camera.class);
            startActivityForResult(intent, 0);
        }catch (Exception ex){
            Toast.makeText(this, "Exception al guardar el cuadro \n \n"+ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //BOTON (AZUL LUPA) BUSCAR SIEMBRA
    public void btnBuscar(View v) throws Exception{

        String idVariedad = codebar.getText().toString();

        if(idVariedad.isEmpty()){
            Toast.makeText(this, "No puedes dejar el campo vacio", Toast.LENGTH_SHORT).show();
        }else{
            long bs = Long.parseLong(idVariedad);
            if(bs <= 0) {
                Toast.makeText(this, "El digito es invalido", Toast.LENGTH_SHORT).show();
            }else {
                buscarSiembra(bs);
                iCB.all();
            }
        }
        bajarTeclado();
    }

    //REALIZA EL FILTRO DE BUSQUEDA SIEMBRAS
    public void buscarSiembra(long bs) {
        try {
            p = iP.OneforIdSiembra(bs);

            if (p.getIdSiembra()==null) {

                Toast toast = Toast.makeText(getApplicationContext(), "Lo sentimos pero no se encuentra la siembra", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 100);
                toast.show();

            } else {
                    String s;
                    if (p.getIdSiembra() != null) {
                        s = p.getCama() + p.getSufijo();
                    } else {
                        s = valueOf(p.getCama());
                    }

                ab = iCB.cuadroyvariedad((long) p.getIdBloque(), (long) p.getIdVariedad());
                cargarImagenes(ab.getIdFenologia(),(long) p.getIdFinca());

                finca.setText(p.getFinca());
                variedad.setText(p.getVariedad());
                bloque.setText(p.getBloque());
                cama.setText(s);
                NoArea.setText(String.valueOf(p.getArea()));
                NoPlantas.setText(String.valueOf(p.getPlantas()));
                NoCuadros.setText(String.valueOf(ab.getNumeroCuadros()));

            }

        } catch (Exception e) {
            Toast.makeText(this, "Error busqueda" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    //REALIZA LA CARGAS DE IMAGEN SEGUN FENOLOFIA
    public void cargarImagenes(long idVariedad, Long idFinca) throws Exception{
        try {

            imageAdmin iA = new imageAdmin();
            List<fenologiaTab> fi = forGradoloc(dia, gDia, idVariedad);

            String path2 = "/storage/emulated/0/Pictures/fenologias/"+idFinca+"/";

            iA.getImage(path2,jpgView1, idVariedad, fi.get(0).getImagen());
            iA.getImage(path2,jpgView2, idVariedad, fi.get(1).getImagen());
            iA.getImage(path2,jpgView3, idVariedad, fi.get(2).getImagen());
            iA.getImage(path2,jpgView4, idVariedad, fi.get(3).getImagen());

            SharedPreferences.Editor edit = sp.edit();
            edit.putFloat("gDia", gDia);
            edit.putInt("dia", dia);
            edit.putLong("IdVariedad", idVariedad);
            edit.commit();
            edit.apply();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error \n" + e, Toast.LENGTH_LONG).show();
        }
    }


    public List<fenologiaTab> forGradoloc(int dia, float gDia, long idVariedad) throws Exception {

        int d = (8 - dia);

        float[] img = new float[4];
        img[0] = (d) * gDia;
        img[1] = (d + 7) * gDia;
        img[2] = (d + 21) * gDia;
        img[3] = (d + 28) * gDia;
        int c = 0;

        Iterator<fenologiaTab> i = iF.all().iterator();
        List<fenologiaTab> fi = new ArrayList<>();
        fenologiaTab fu = new fenologiaTab();

        String data = "";

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
        return fi;
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
            if (IdSiembra.getText().toString().isEmpty()) {
                Toast toast = Toast.makeText(getApplicationContext(), "Lo sentimos, no es posible realizar un registro \n por favor realiza una búsqueda", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 100);
                toast.show();
            } else if (total.getText().toString().isEmpty()) {
                Toast toast = Toast.makeText(getApplicationContext(), "Lo sentimos, no es posible realizar un registro\n por favor llena el campo total", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 100);
                toast.show();
            } else {

                if (!IdSiembra.getText().toString().isEmpty() || Integer.parseInt(IdSiembra.getText().toString())>0) {

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

                    int usu = Integer.parseInt(idusuario.getText().toString());
                    c.setIdUsuario(usu);

                    Toast.makeText(this, iC.insert(c), Toast.LENGTH_LONG).show();
                    //obteniendo posicion del spinner(Cuadro)
                    int size = Integer.parseInt(cuadro.getSelectedItem().toString());

                    //validacion de la posicion del cuadro
                    if (size == 8) {
                        cuadro.setSelection(0);
                        cuadro.setEnabled(true);
                        c1.setText(valueOf(0));
                        c4.setText(valueOf(0));
                        total.setText(valueOf(0));
                    } else {
                        int posNext = size++;
                        Toast.makeText(this, "En este momento estas sobre el cuadro " + size, Toast.LENGTH_LONG).show();
                        cuadro.setSelection(posNext);
                        c1.setText(valueOf(0));
                        c4.setText(valueOf(0));
                        total.setText(valueOf(0));
                    }

                } else {
                    Toast.makeText(this, "hola", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Exception al momento de registrar \n \n" + e.toString(), Toast.LENGTH_LONG).show();
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
    public void btn_visual1(View v) {

        if ((jpgView1.getDrawable() == null) && (jpgView2.getDrawable() == null) && (jpgView3.getDrawable() == null) && (jpgView4.getDrawable() == null)) {
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
        Toast.makeText(this, "btn back ", Toast.LENGTH_SHORT).show();
        finish();
        System.exit(0);
    }

    public void requestConteoTotal() {
        c4.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    total.requestFocus();
                }
                return handled;
            }
        });
    }

    public  void bajarTeclado(){
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(codebar.getWindowToken(), 0);
    }

}
