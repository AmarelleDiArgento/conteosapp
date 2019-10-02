package com.lotus.conteos_app;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lotus.conteos_app.Config.Util.TableDinamic;
import com.lotus.conteos_app.Config.Util.jsonAdmin;
import com.lotus.conteos_app.Model.iConteo;
import com.lotus.conteos_app.Model.iFenologia;
import com.lotus.conteos_app.Model.iPlano;
import com.lotus.conteos_app.Model.tab.conteoTab;

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

    float gDia;
    List<conteoTab> clc = new ArrayList<>();
    private TableLayout tableLayout;
    TableDinamic tb;
    private int year;
    private int month;
    private int day;

    // Encabezados de la tabla
    private String[] header = {"Bloque", "Variedad", "CC", "CT", "CN1", "EST1", "CN4", "EST4", "CTA", "ESTT"};
    // Datos de la tabla

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historial_main);
        getSupportActionBar().hide();
        try {

            sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);
            path = getExternalFilesDir(null) + File.separator;
            // path = "/storage/extSdCard/";
            gradosDiaTxt = findViewById(R.id.gradosDia);

            date = findViewById(R.id.date_picker);
            btn_show_picker = (ImageButton) findViewById(R.id.btn_datapicker);
            fech = findViewById(R.id.txt_fecha);
            fechita = findViewById(R.id.fechita);
            fechaoculta = findViewById(R.id.fechaoculta);
            //usuLog = findViewById(R.id.usuLog);
            //String usuario=sp.getString("nombre","");
            //usuLog.setText(usuario);
            ja = new jsonAdmin();

            //gradosDiaTxt.setSelectAllOnFocus(true);
            gradosDiaTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gradosDiaTxt.setSelectAllOnFocus(true);
                    view.clearFocus();
                    view.requestFocus();
                }
            });

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

            date.init(year, month, day, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                    HistorialMainActivity.this.day = day;
                    HistorialMainActivity.this.month = month;
                    HistorialMainActivity.this.year = year;

                    cargadefecha();
                }
            });
            //limpia los registros

            // createTable();
            createTable();
            gradosDiaTxt.setText(String.valueOf(recibirGradoDia()));


            class MyKeyListerner implements View.OnKeyListener {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        //Toast.makeText(Login.this,"se oprimio el boton",Toast.LENGTH_SHORT).show();
                        sharedGradoDia(null);
                        return true;
                    }
                    return false;
                }
            }

            View.OnKeyListener listener = new MyKeyListerner();
            gradosDiaTxt.setOnKeyListener(listener);

        } catch (Exception e) {
            Toast.makeText(this, "Error en el create " + e.toString(), Toast.LENGTH_LONG).show();

        }
    }

    public void clicTable(View v) {

        try {

            conteoTab ct = clc.get(tb.getIdTabla() - 1);
            if (ct != null) {
                String bloque = ct.getBloque();
                String usuario = fechita.getText().toString();

                SharedPreferences.Editor edit = sp.edit();
                edit.putString("date", fecha);
                edit.putInt("bloque", ct.getIdBloque());
                edit.putInt("idvariedad", ct.getIdVariedad());
                edit.putString("usulog", usuario);
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

            //obteniendo el usuario
            String usuario = sp.getString("nombre", "");
            fechita.setText("Fecha: " + fecha + "\nUsuario: " + usuario);

        } catch (Exception e) {
            Toast.makeText(this, "Exception getDate" + e, Toast.LENGTH_LONG).show();
        }
    }

    //METODO PARA VALIDAR EL CAMPO DE LOS GRADOS DIA
    public void goMain(View v) {
        try {
            if (gDia >= 5) {
                //Toast.makeText(this,"se pasa el grado: "+datodia,Toast.LENGTH_LONG).show();

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

        //string a date
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");


        try {
            Date date = formatter.parse(fechaconver);
            String nn = formatter.format(date);
            //Toast.makeText(this, "nuevo formato 1  \n" +date, Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, "nuevo formato 2  \n" +nn, Toast.LENGTH_SHORT).show();

            String[] Strsplit = nn.split("/");
            String fechanew1 = Strsplit[0].trim() + Strsplit[1].trim() + Strsplit[2].trim();
            String dateInString = fechanew1;

            //Toast.makeText(this, "sin split  \n" +dateInString, Toast.LENGTH_SHORT).show();
            fechaoculta.setText(dateInString);
        } catch (Exception e) {
            Toast.makeText(this, "no se pudo convertir \n" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public List<conteoTab> calcular() {
        //String text = "";
        try {
            iConteo iC = new iConteo(path);
            fecha = fechaoculta.getText().toString();
            iC.nombre = fecha;
            clc.clear();
            List<conteoTab> cl = iC.all();
            for (conteoTab c : cl) {
                boolean val = true;
                for (int i = 0; i <= clc.size() - 1; i++) {
                    // c.getIdBloque() == clc.get(i).getIdBloque()
                    // c.getIdVariedad() == clc.get(i).getIdVariedad()
                    // c.getIdSiembra() == clc.get(i).getIdSiembra()

                    if (c.getIdVariedad() == clc.get(i).getIdVariedad() || c.getIdBloque() == clc.get(i).getIdBloque()) {

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

            //tostada(text).show();


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
            final iConteo iC = new iConteo(path);
            String fob = fechaoculta.getText().toString();
            iC.nombre = fob;

            final List<conteoTab> cl = calcular();
            for (final conteoTab c : cl) {
                // {"Finca", "Bloque", "Variedad", "CC", "CT", "S1C", "S1P", "S4C", "S4P

                rows.add(new String[]{
                                c.getBloque(),
                                c.getVariedad(),
                                String.valueOf(c.getCuadro()),
                                String.valueOf(c.getCuadros()),
                                String.valueOf(frt.format(c.getConteo1())),
                                String.valueOf(frt.format(extrapolar(c.getCuadros(), c.getCuadro(), c.getConteo1()))),
                                String.valueOf(frt.format(c.getConteo4())),
                                String.valueOf(frt.format(extrapolar(c.getCuadros(), c.getCuadro(), c.getConteo4()))),
                                String.valueOf(frt.format(c.getTotal())),
                                String.valueOf(frt.format(extrapolar(c.getCuadros(), c.getCuadro(), c.getTotal())))
                        }

                );

                Toast.makeText(this, "id Variedad"+c.getIdVariedad(), Toast.LENGTH_LONG).show();
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
            tb = new TableDinamic(tableLayout, getApplicationContext());
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


            int idFinca = sp.getInt("idFinca", 0);

            iFenologia iF = new iFenologia(path);
            iPlano iP = new iPlano(path);

            if (iP.local(idFinca) && iF.local(idFinca)) {
                Toast.makeText(this, "Local actualizado exitosamente", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "No se dispone de conexion:\n"
                    + "Trabajando con recursos locales\n"
                    + "Codigo: " + e, Toast.LENGTH_LONG).show();
        }
    }

    public void buscarxfecha(View v) {
        calcular();
        createTable();
        //PROCEDIMIENTO PICKER
        date.setVisibility(View.INVISIBLE);
    }

    public void cerrarsesion(View v) {
        Intent i = new Intent(HistorialMainActivity.this, Login.class);
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        Toast.makeText(this, "se ha cerrado sesion exitosamente", Toast.LENGTH_SHORT).show();
        finish();
    }

    //PARA VOLVER
    public void onBackPressed() {
    }


    Toast tostada(String mjs) {
        return Toast.makeText(this, mjs, Toast.LENGTH_LONG);
    }
}
