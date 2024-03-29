package com.lotus.conteos_app;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lotus.conteos_app.Config.Util.Dialog;
import com.lotus.conteos_app.Config.Util.ModalFincas;
import com.lotus.conteos_app.Config.Util.TableDinamic;
import com.lotus.conteos_app.Config.Util.extrapolacion;
import com.lotus.conteos_app.Config.Util.jsonAdmin;
import com.lotus.conteos_app.Config.Util.storageFarmWorking;
import com.lotus.conteos_app.Model.iConteo;
import com.lotus.conteos_app.Model.iCuadrosBloque;
import com.lotus.conteos_app.Model.iFenologia;
import com.lotus.conteos_app.Model.iFincas;
import com.lotus.conteos_app.Model.iPlano;
import com.lotus.conteos_app.Model.tab.conteoTab;
import com.lotus.conteos_app.Model.tab.fincasTab;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistorialMainActivity extends AppCompatActivity {
    SharedPreferences sp = null;
    public String path = null;
    String fecha = "";
    jsonAdmin ja = null;
    EditText gradosDiaTxt;
    DatePicker date;
    ImageView btn_show_picker;
    TextView fech, fechita, fechaoculta, usuLog;
    Calendar calendarDate;

    Button btnSubir;

    float gDia;
    List<conteoTab> clc = new ArrayList<>();
    private TableLayout tableLayout;
    TableDinamic tb;
    private int year;
    private int month;
    private int day;

    iConteo iC;

    // Encabezados de la tabla
    private String[] header = {"Bloque", "Variedad", "CC", "CT", "CS1", "CS4", "CTT", "EST1", "EST2", "EST3", "EST4", "ESTT"};
    // Datos de la tabla

    int idFarmWorking;
    String nameFarmWorking, pathFarmWorking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historial_main);
        getSupportActionBar().hide();
        try {

            sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);
            path = getExternalFilesDir(null) + File.separator;
            gradosDiaTxt = findViewById(R.id.gradosDia);

            date = findViewById(R.id.date_picker);
            btn_show_picker = (ImageButton) findViewById(R.id.btn_datapicker);
            fech = findViewById(R.id.txt_fecha);
            fechita = findViewById(R.id.fechita);
            fechaoculta = findViewById(R.id.fechaoculta);
            btnSubir = findViewById(R.id.btnSubir);

            iC = new iConteo(path, this);
            ja = new jsonAdmin();

            gradosDiaTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gradosDiaTxt.setSelectAllOnFocus(true);
                    view.clearFocus();
                    view.requestFocus();
                }
            });

            getFarmWorking();
            getDate();

            //PROCEDIMIENTO PICKER
            date.setVisibility(View.INVISIBLE);

            Calendar cal = Calendar.getInstance();

            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
            year = cal.get(Calendar.YEAR);

            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyy");
            fecha = sdf.format(cal.getTime());
            fechaoculta.setText(fecha);

            enviarFecha(fechaoculta.getText().toString());

            date.init(year, month, day, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                    HistorialMainActivity.this.day = day;
                    HistorialMainActivity.this.month = month;
                    HistorialMainActivity.this.year = year;

                    cargadefecha();
                }
            });
            createTable();
            gradosDiaTxt.setText(String.valueOf(recibirGradoDia()));


            class MyKeyListerner implements View.OnKeyListener {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        sharedGradoDia(null);
                        return true;
                    }
                    return false;
                }
            }

            View.OnKeyListener listener = new MyKeyListerner();
            gradosDiaTxt.setOnKeyListener(listener);

            countRecords();



        } catch (Exception e) {
            Toast.makeText(this, "Error en el create " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        getFarmWorking();
        getDate();
    }

    public void getFarmWorking(){
        try {
            storageFarmWorking sfw = new storageFarmWorking(this);
            sfw.storageIdFarmWorking(0, "", "", false);

            for (int i = 0; i < sfw.getLstStoragefarms().size(); i++) {

                Log.i("workingFarm", "Encontro : "+sfw.getLstStoragefarms().get(i).getNameFarm());

                idFarmWorking = sfw.getLstStoragefarms().get(i).getIdFarm();
                nameFarmWorking = sfw.getLstStoragefarms().get(i).getNameFarm();
                pathFarmWorking = sfw.getLstStoragefarms().get(i).getPath();
            }
        }catch (Exception e){
            Log.i("workingFarm", "Error : "+e.toString());
        }
    }

    public void countRecords() throws Exception{
        fecha = fechaoculta.getText().toString();
        iC.nombre = fecha;

        List<conteoTab> lstConteos = iC.all();

        //int cantidadRegistros = iC.all() != null ?  iC.all().size() : 0;

        int cantidadRegistros = 0;

        if(iC.all() != null){
            for(conteoTab c : lstConteos){

                Log.i("conteosRegister", "id conteo ; "+c.getIdConteo()+", estado : "+c.getEstado());

                if(c.getEstado().equals("0")){
                    cantidadRegistros++;
                }
            }
        }else{
            Log.i("conteosRegister", "Esta vacio fecha : "+fecha);
        }

        btnSubir.setText(cantidadRegistros == 0 ? "Sin registros" : "Enviar ("+cantidadRegistros+")");
    }

    public void clicTable(View v) {
        try {
            conteoTab ct = clc.get(tb.getIdTabla() - 1);
            if (ct != null) {

                SharedPreferences.Editor edit = sp.edit();
                edit.putString("date", fecha);
                edit.putInt("bloque", ct.getIdBloque());
                edit.putInt("idvariedad", ct.getIdVariedad());
                edit.putString("fecha", fechita.getText().toString());
                edit.apply();

                Intent i = new Intent(this, ActivityDetalles.class);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "No has seleccionado aún una fila", Toast.LENGTH_LONG).show();
            }
        } catch (Exception E) {
            Toast.makeText(getApplicationContext(), "No has seleccionado aún una fila", Toast.LENGTH_LONG).show();
        }
    }

    public void enviarFecha(String fechaBusqueda){
        SharedPreferences.Editor edit = sp.edit();
        Toast.makeText(this, ""+fechaBusqueda, Toast.LENGTH_SHORT).show();
        edit.putString("fechaActual", fecha);
        edit.putString("fechaBusqueda", fechaBusqueda);
        edit.apply();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        createTable();
    }

    //OBTENER FECHA ACTUAL
    public void getDate() {
        try {
            calendarDate = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
            fecha = sdf.format(calendarDate.getTime());

            //fech.setText(diahoy + "/" + (meshoy + 1) + "/" + añohoy);
            fech.setText(fecha);
            fech.setTextSize(30);

            String usuario = "Sin user";

            //obteniendo el usuario
            if(sp != null) {
                usuario = sp.getString("nombre", "");
            }
            fechita.setText("Fecha: " + fecha + "\nUsuario: " + usuario + "\nFinca "+nameFarmWorking);

        } catch (Exception e) {
            Toast.makeText(this, "Exception getDate" + e, Toast.LENGTH_LONG).show();
        }
    }

    //METODO PARA VALIDAR EL CAMPO DE LOS GRADOS DIA
    public void goMain(View v) {
        try {
            if (gDia >= 5) {
                Intent intent = new Intent(HistorialMainActivity.this, MainActivity.class);
                startActivity(intent);

            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Por favor verifique que tengas los grados dia menos de 7", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Error goMain  " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void nuevoRegistro(View v){
        Intent intent = new Intent(HistorialMainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //METODO PARA EL ENVIO DE LOS GRADOS DIA CON SHARED PREFERENCES
    public void sharedGradoDia(View view) {
        SharedPreferences.Editor edit = sp.edit();
        gDia = Float.valueOf(gradosDiaTxt.getText().toString());

        if ((gDia >= 5) && (gDia <= 15)) {
            edit.putFloat("gradoDia", gDia);
            edit.commit();
            edit.apply();

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(gradosDiaTxt.getWindowToken(), 0);

            Toast.makeText(this, "Se ha guardado exitosamente los grados dia", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "Por favor verifica que tengas los grados dia entre un rango de 5 y  15 grados dia", Toast.LENGTH_LONG).show();

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(gradosDiaTxt.getWindowToken(), 0);
        }
    }

    //METODO PARA ALMACENAR LOS GRADOS DIA
    public float recibirGradoDia() {

        if (sp != null) {
            gDia = sp.getFloat("gradoDia", 0);
            return gDia;
        } else {
            return 0;
        }
    }

    //OCULTA EL PICKER CUANDO COMIENZA LA ACTIVIDAD Y LO HACE VISIBLE POR MEDIO DEL BOTON
    public void showpicker(View v) {
        if (btn_show_picker.isClickable() && date.getVisibility() == View.INVISIBLE) {

            date.setVisibility(View.VISIBLE);
        } else if (btn_show_picker.isClickable() && date.getVisibility() == View.VISIBLE) {
            date.setVisibility(View.INVISIBLE);
        } else {
            Toast.makeText(this, "No se pudo cargar el picker de la fecha", Toast.LENGTH_SHORT).show();
        }
    }

    //MUESTRA LA FECHA SELECCIONADA SEGUN LO QUE ELIGIO EN EL PICKER
    public void cargadefecha() {

        String fechaconver;

        StringBuffer strBuffer = new StringBuffer();

        strBuffer.append(this.day);
        strBuffer.append("/");
        strBuffer.append(this.month + 1);
        strBuffer.append("/");
        strBuffer.append(this.year);
        fech.setText(strBuffer.toString());
        fech.setTextSize(30);

        fechaconver = strBuffer.toString();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");


        try {
            Date date = formatter.parse(fechaconver);
            String nn = formatter.format(date);

            String[] Strsplit = nn.split("/");
            String fechanew1 = Strsplit[0].trim() + Strsplit[1].trim() + Strsplit[2].trim();
            String dateInString = fechanew1;

            fechaoculta.setText(dateInString);
            enviarFecha(fechaoculta.getText().toString());

        } catch (Exception e) {
            Toast.makeText(this, "no se pudo convertir \n" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public List<conteoTab> calcular() {
        try {
            fecha = fechaoculta.getText().toString();
            iC.nombre = fecha;
            clc.clear();
            List<conteoTab> cl = iC.all();
            for (conteoTab c : cl) {
                boolean val = true;
                for (int i = 0; i <= clc.size() - 1; i++) {

                    if (c.getIdVariedad() == clc.get(i).getIdVariedad() && c.getIdBloque() == clc.get(i).getIdBloque()) {

                        int cu = clc.get(i).getCuadro() + 1;
                        int c1 = clc.get(i).getConteo1() + c.getConteo1();
                        int c4 = clc.get(i).getConteo4() + c.getConteo4();
                        int ct = clc.get(i).getTotal() + c.getTotal();

                        clc.get(i).setCuadro(cu);
                        clc.get(i).setConteo1(c1);
                        clc.get(i).setConteo4(c4);
                        clc.get(i).setTotal(ct);
                        val = false;
                    }
                }
                if (val) {
                    c.setCuadro(1);
                    clc.add(c);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "No existen registros actuales que coincidan con la fecha fecha", Toast.LENGTH_LONG).show();
            clc.clear();
        }
        return clc;
    }

    // Dibuja la tabla de calculos
    public ArrayList<String[]> cargarConteo() {
        DecimalFormat frt = new DecimalFormat("#,###");
        final ArrayList<String[]> rows = new ArrayList<>();

        try {
            rows.clear();
            String fob = fechaoculta.getText().toString();
            iC.nombre = fob;

            extrapolacion exP = new extrapolacion();

            final List<conteoTab> cl = calcular();
            for (final conteoTab c : cl) {


                int estimado_Sem1 = exP.extrapolar(c.getCuadros(), c.getCuadro(), c.getConteo1());
                int estimado_Sem4 = exP.extrapolar(c.getCuadros(), c.getCuadro(), c.getConteo4());
                int estimado_total = exP.extrapolar(c.getCuadros(), c.getCuadro(), c.getTotal());

                float resultadoEstimado2 = exP.extrapolarFaltante(estimado_total, estimado_Sem1, estimado_Sem4).get(0);
                float resultadoEstimado3 = exP.extrapolarFaltante(estimado_total, estimado_Sem1, estimado_Sem4).get(1);

                rows.add(new String[]{
                        c.getBloque(),
                        c.getVariedad(),
                        String.valueOf(c.getCuadro()),
                        String.valueOf(c.getCuadros()),
                        String.valueOf(frt.format(c.getConteo1())),
                        String.valueOf(frt.format(c.getConteo4())),
                        String.valueOf(frt.format(c.getTotal())),
                        String.valueOf(frt.format(estimado_Sem1)),
                        String.valueOf(frt.format(resultadoEstimado2)),
                        String.valueOf(frt.format(resultadoEstimado3)),
                        String.valueOf(frt.format(estimado_Sem4)),
                        String.valueOf(frt.format(estimado_total))
                    }
                );
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error exception Cargar conteo: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        return rows;
    }

    //CALCULO DE FONOLOGIAS (REGLA DE 3)
    public int extrapolar(int CT, int CC, int S) {
        return (S * CT) / CC;
    }


    //CREACION DE LA TABLA
    public void createTable() {
        try {
            tableLayout = findViewById(R.id.tabla);
            tb = new TableDinamic(tableLayout, getApplicationContext(), "enviarDetalle", clc, null, null, null, null, null, null,null);
            tableLayout.removeAllViews();
            tb.addHeader(header);
            tb.addData(cargarConteo());
            tb.backgroundHeader(
                    Color.parseColor("#20C0FF")
            );
            tb.backgroundData(
                    Color.parseColor("#FFFFFF"),
                    Color.parseColor("#81F0EDED")
            );


        } catch (Exception e) {
            Toast.makeText(this, "Error de la  table: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    //ACTUALIZA LOS PLANOS
    public void actualizarBases(View v) {
        try {

            //new ModalFincas(this, path).crear();

            Intent i = new Intent(this, download_farm_screen.class);
            startActivity(i);

        } catch (Exception e) {
            Toast.makeText(this, "No se dispone de conexion:\n"
                    + "Trabajando con recursos locales\n"
                    + "Codigo: " + e, Toast.LENGTH_LONG).show();
        }
    }

    public void buscarxfecha(View v) throws Exception{
        calcular();
        createTable();
        //PROCEDIMIENTO PICKER
        date.setVisibility(View.INVISIBLE);
        countRecords();
    }

    public void cerrarsesion(View v) {
        Intent i = new Intent(HistorialMainActivity.this, Login.class);
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        Toast.makeText(this, "se ha cerrado sesion exitosamente", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void subirRegistro(View v) {
        try {
            iConteo iC = new iConteo(path, this);
            iC.nombre = fechaoculta.getText().toString();
            iC.batch(fechaoculta.getText().toString());
            countRecords();
        } catch (Exception ex) {
            Toast.makeText(this, "Exception al subir el registro \n \n" + ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    //PARA VOLVER
    public void onBackPressed() {
    }
}
