package com.lotus.conteos_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lotus.conteos_app.Config.Util.extrapolacion;
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

    float gDia, resultadoLimiteTotal;
    EditText c1, c4, total, codebar;
    LinearLayout notification;
    Spinner cuadro;
    ImageView jpgView1, jpgView2, jpgView3, jpgView4;
    TextView c2, c3, gradoDia, finca, variedad, bloque, cama, fechaAct, usuario, NoArea, NoPlantas, NoCuadros, idusuario, IdSiembra;

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

    long idVariedad, idFinca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        try {
            sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);
            codebar = findViewById(R.id.resulcode);

            //codebar.setEnabled(false);

            notification = findViewById(R.id.notification);
            gradoDia = findViewById(R.id.gradodia);
            gradoDia.setText(valueOf(getGradoDia()));
            IdSiembra = findViewById(R.id.resulcode);
            c1 = findViewById(R.id.c1et);
            c2 = findViewById(R.id.c2et);
            c3 = findViewById(R.id.c3et);
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
            c4.setText("0");
            c1.setText("0");
            total.setText("0");

            validarConteoExtrapolacion(total);
            validarConteoExtrapolacion(c1);
            validarConteoExtrapolacion(c4);

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

            validarConteoTotal();


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
            iC = new iConteo(path, this);
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
    public void btnBuscar(View v){

        try {
            String idVariedad = codebar.getText().toString();

            if (idVariedad.isEmpty()) {
                Toast.makeText(this, "No puedes dejar el campo vacio", Toast.LENGTH_SHORT).show();
            } else {
                long bs = Long.parseLong(idVariedad);
                if (bs <= 0) {
                    Toast.makeText(this, "El digito es invalido", Toast.LENGTH_SHORT).show();
                } else {
                    buscarSiembra(bs);
                    iCB.all();
                }
            }
            bajarTeclado();
        }catch (Exception e){
            Log.i("ERRORBUSCAR",e.toString());
        }
    }

    //REALIZA EL FILTRO DE BUSQUEDA SIEMBRAS
    public void buscarSiembra(long bs) {
        try {
            Log.i("SIEMBRA",""+bs);
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

                ab = iCB.cuadroyvariedad( p.getIdBloque(),  p.getIdVariedad());

                finca.setText(p.getFinca());
                variedad.setText(p.getVariedad());
                bloque.setText(p.getBloque());
                cama.setText(s);
                NoArea.setText(String.valueOf(p.getArea()));
                NoPlantas.setText(String.valueOf(p.getPlantas()));
                NoCuadros.setText(String.valueOf(ab.getNumeroCuadros()));

                if(ab.getIdFenologia() != null){
                    idFinca = p.getIdFinca();
                    idVariedad = ab.getIdFenologia();
                    cargarImagenes(ab.getIdFenologia(), p.getIdFinca());
                }else{
                    Toast.makeText(this, "No hay fenologias relacionadas con la cama", Toast.LENGTH_SHORT).show();
                }

            }

        } catch (Exception e) {
            Toast.makeText(this, "Error busqueda" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    //REALIZA LA CARGAS DE IMAGEN SEGUN FENOLOFIA
    public void cargarImagenes(long idVariedad, long idFinca) throws Exception{
        try {
            imageAdmin iA = new imageAdmin();
            List<fenologiaTab> fi = forGradoloc(dia, gDia, idVariedad);

            String path2 = "/storage/emulated/0/Pictures/fenologias/"+idFinca+"/";

            iA.getImage(path2,jpgView1, idVariedad, fi.get(0).getImagen());
            iA.getImage(path2,jpgView2, idVariedad, fi.get(1).getImagen());
            iA.getImage(path2,jpgView3, idVariedad, fi.get(2).getImagen());
            iA.getImage(path2,jpgView4, idVariedad, fi.get(3).getImagen());

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error Imnagenes\n" + e, Toast.LENGTH_LONG).show();
        }
    }


    public List<fenologiaTab> forGradoloc(int dia, float gDia, long idVariedad) throws Exception {
        Log.i("calculo", "Llego al metodo");
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

        while (i.hasNext()) {
            fenologiaTab f = i.next();
            if (f.getIdFenologia() == idVariedad) {

                if (img[c] <= f.getGrados_dia()) {
                    double pos = f.getGrados_dia() - img[c];
                    double pre = img[c] - fu.getGrados_dia();

                    Log.i("calculo"," gdia fenologia : "+f.getGrados_dia()+" - IMAGEN : "+img[c]+" = pos : "+pos);
                    Log.i("calculo"," IMAGEN : "+img[c]+"- gdia fenologia : "+fu.getGrados_dia()+" = pre : "+pre);

                    Log.i("cercania", "sem"+c+ " max : "+f.getGrados_dia()+" min : "+fu.getGrados_dia());

                    if (pre >= pos) {
                        Log.i("calculo","pre : "+pre+" es mayor que pos"+pos);
                        fi.add(f);
                    } else {
                        Log.i("calculo","pre : "+pre+" es menor que pos"+pos);
                        fi.add(fu);
                    }
                    c++;
                    if (c >= 4) { break; }
                }
                fu = f;

            }else{
                Log.i("calculo", "ID fenologia no se encontro : "+idVariedad);
            }
        }
        if (c <= 4) {
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
             extrapolacion exP = new extrapolacion();

            if (IdSiembra.getText().toString().isEmpty() || total.getText().toString().isEmpty())  {
                Toast toast = Toast.makeText(getApplicationContext(),"Lo sentimos, no es posible realizar un registro" + (IdSiembra.getText().toString().isEmpty() ? "\n por favor realiza una búsqueda" : "\n por favor agrega un total"), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 100);
                toast.show();
            } else {
                if (!IdSiembra.getText().toString().isEmpty() || Integer.parseInt(IdSiembra.getText().toString()) > 0) {

                    conteoTab c = new conteoTab();
                    int ntotal = parseNum(total.getText().toString()),
                        nSem1 = parseNum(c1.getText().toString()),
                        nSem4 = parseNum(c4.getText().toString());
                    int nSem2 = exP.extrapolarConteo(ntotal, nSem1, nSem4),
                        nSem3 = ntotal - nSem1 - nSem2- nSem4;

                    Log.i("ENVIO", "SEM2 : "+nSem2+" SEM3 : " +nSem3);

                    c.setIdSiembra(p.getIdSiembra());
                    c.setCama(p.getCama());

                    c.setIdBloque(p.getIdBloque());
                    c.setBloque(p.getBloque());
                    c.setIdVariedad(p.getIdVariedad());
                    c.setVariedad(p.getVariedad());
                    c.setCuadro(Integer.parseInt(cuadro.getSelectedItem().toString()));

                    c.setConteo1(nSem1);
                    c.setConteo2(nSem2);
                    c.setConteo3(nSem3);
                    c.setConteo4(nSem4);
                    c.setTotal(ntotal);

                    c.setCuadros(Integer.parseInt(NoCuadros.getText().toString()));
                    c.setPlantas(p.getPlantas());
                    c.setArea(p.getArea());
                    c.setEstado("0");

                    int usu = Integer.parseInt(idusuario.getText().toString());
                    c.setIdUsuario(usu);
                    Toast.makeText(this, iC.insert(c), Toast.LENGTH_LONG).show();
                    reposicionamientoCuadro();
                    validacionTotal();

                } else {
                    Toast.makeText(this, "hola", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Exception al momento de registrar \n \n" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void validacionTotal(){
        if(resultadoLimiteTotal <= 0.39 || resultadoLimiteTotal  >= 0.61) {
            notification.addView(text());
        }

        new Handler().postDelayed(new Runnable() {
            public void run() {
                notification.removeAllViews();
            }
        }, 7000);
    }


    public View text(){

        LinearLayout line = new LinearLayout(this);
        line.setBackgroundColor(Color.parseColor("#F39C12"));
        line.setPadding(0,20,0,20);
        line.setGravity(Gravity.CENTER);

        String data = "El valor del total no concuerdan con la semana 1 y semana 4 \n pero se guardo el registro con exito";
        TextView txt = new TextView(this);
        txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        line.setGravity(Gravity.CENTER);
        txt.setText(data);
        txt.setTextColor(Color.parseColor("#FDFEFE"));
        txt.setTextSize(20);

        line.addView(txt);
        return line;
    };

    public void reposicionamientoCuadro(){//reposiciona el valor del cuadro a un valor superior o a 0 si es llega a 8
        int size = Integer.parseInt(cuadro.getSelectedItem().toString());
        cuadro.setSelection(size == 8 ? 0 : size++);
        Toast.makeText(this, "En este momento estas sobre el cuadro " + size, Toast.LENGTH_LONG).show();
        c1.setText(valueOf(0));
        c4.setText(valueOf(0));
        total.setText(valueOf(0));
    }

    public int parseNum(String data){
        return  Integer.parseInt(data);
    }

    public void validarConteoTotal(){

        total.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    c1.setText( c1.getText().toString().isEmpty() ? "0" : c1.getText().toString());
                    c4.setText( c4.getText().toString().isEmpty() ? "0" : c4.getText().toString());

                    float conteo1 = Float.parseFloat(c1.getText().toString());
                    float conteo4 = Float.parseFloat(c4.getText().toString());
                    float conteoTotal = Float.parseFloat(total.getText().toString());

                    if (conteo1 != 0.0 && conteo4 != 0.0) {
                        resultadoLimiteTotal = (conteo1 + conteo4) / conteoTotal;
                        Log.i("RESULTADOTOTAL", "RESULTADOTOTAL : " + (((resultadoLimiteTotal > 0.39) && (resultadoLimiteTotal  < 0.61) ?  "se encuentra dentro del rango requerido ---> " : "No esta dentro del rango ---> ")+resultadoLimiteTotal));
                    } else {
                        Toast.makeText(MainActivity.this, "Por favor completa las casillas para las semanas 1 y 4", Toast.LENGTH_SHORT).show();
                        total.setText("");
                    }
                }catch (Exception e){
                }
            }
            @Override public void afterTextChanged(Editable editable) { }
        });
    }

    public void validarConteoExtrapolacion(EditText edt){

        edt.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {

                    int t = Integer.parseInt(total.getText().toString());
                    int cc1 = Integer.parseInt(c1.getText().toString().isEmpty() ? "0" : c1.getText().toString());
                    int cc4 = Integer.parseInt(c4.getText().toString().isEmpty() ? "0" : c4.getText().toString());

                    extrapolacion ext = new extrapolacion();

                    float res2 = ext.extrapolarFaltante(t, cc1, cc4).get(0);
                    float res3 = ext.extrapolarFaltante(t, cc1, cc4).get(1);

                    c2.setText(res2+"");
                    c3.setText(res3+"");
                }catch (Exception e){
                }
            }
            @Override public void afterTextChanged(Editable editable) { }
        });
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

            SharedPreferences.Editor edit = sp.edit();
            edit.putFloat("gDia", gDia);
            edit.putInt("dia", dia);
            edit.putLong("IdVariedad", idVariedad);
            edit.putLong("IdFinca", idFinca);
            edit.commit();
            edit.apply();

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