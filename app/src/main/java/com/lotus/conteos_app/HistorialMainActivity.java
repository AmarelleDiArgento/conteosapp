package com.lotus.conteos_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lotus.conteos_app.Config.Util.TableDinamic;
import com.lotus.conteos_app.Config.Util.jsonAdmin;
import com.lotus.conteos_app.Model.iConteo;
import com.lotus.conteos_app.Model.iFenologia;
import com.lotus.conteos_app.Model.iPlano;
import com.lotus.conteos_app.Model.tab.conteoTab;
import com.lotus.conteos_app.Model.tab.fenologiaTab;
import com.lotus.conteos_app.Model.tab.planoTab;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistorialMainActivity extends AppCompatActivity {

    String fecha = "";

    jsonAdmin ja = null;
    EditText gradosDiaTxt;
    DatePicker date;
    ImageView btn_show_picker;
    TextView fech;
    String path = null;

    float gDia;

    private int year;
    private int month;
    private int day;


    private TableLayout tableLayout;
    // Encabezados de la tabla
    private String[] header = {"Bloque", "Variedad", "CC", "CT", "S1C", "S1P", "S4C", "S4P"};
    // Datos de la tabla

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historial_main);
        try {
            path = getExternalFilesDir(null) + File.separator;
            // path = "/storage/extSdCard/";
            gradosDiaTxt = findViewById(R.id.gradosDia);

            date = findViewById(R.id.date_picker);
            btn_show_picker = (ImageButton) findViewById(R.id.btn_datapicker);
            fech = findViewById(R.id.txt_fecha);

            ja = new jsonAdmin();

            getDate();

            //PROCEDIMIENTO PICKER
            date.setVisibility(View.INVISIBLE);

            Calendar currCalendar = Calendar.getInstance();

            day = currCalendar.get(Calendar.DAY_OF_MONTH);
            month = currCalendar.get(Calendar.MONTH);
            year = currCalendar.get(Calendar.YEAR);

            date.init(year, month + 1, day + 5, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                    HistorialMainActivity.this.day = day;
                    HistorialMainActivity.this.month = month;
                    HistorialMainActivity.this.year = year;

                    cargadefecha(day, month, year);
                }
            });

            // createTable();
            gradosDiaTxt.setText(String.valueOf(recibirGradoDia()));


        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        createTable();
    }

    //OBTENER FECHA ACTUAL
    public void getDate() {

        Calendar calendarDate = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        fecha = sdf.format(calendarDate.getTime());

        //desglosando fecha actual
        int diahoy = calendarDate.get(Calendar.DAY_OF_MONTH);
        int meshoy = calendarDate.get(Calendar.MONTH);
        int añohoy = calendarDate.get(Calendar.YEAR);

        fech.setText(diahoy + "/" + (meshoy + 1) + "/" + añohoy);
        fech.setTextSize(30);
    }

    //METODO PARA VALIDAR EL CAMPO DE LOS GRADOS DIA
    public void goMain(View v) {
        try {
            if (gDia >= 7) {
                //Toast.makeText(this,"se pasa el grado: "+datodia,Toast.LENGTH_LONG).show();

                Intent intent = new Intent(HistorialMainActivity.this, MainActivity.class);
                startActivity(intent);

            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Por favor verifique que tengas los grados dia menos de 7", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    //METODO PARA EL ENVIO DE LOS GRADOS DIA CON SHARED PREFERENCES
    public void sharedGradoDia(View view) {
        SharedPreferences gradoDia = getBaseContext().getSharedPreferences("gradoDia", MODE_PRIVATE);
        SharedPreferences.Editor edit = gradoDia.edit();
        gDia = Float.valueOf(gradosDiaTxt.getText().toString());

        if (gDia >= 7) {
            edit.putFloat("gradoDia", gDia);
            edit.commit();
            edit.apply();
        } else {
            Toast.makeText(getApplicationContext(), "Por favor verifique que tengas los grados dia menos de 7", Toast.LENGTH_LONG).show();
        }
    }

    //METODO PARA ALMACENAR LOS GRADOS DIA
    public float recibirGradoDia() {
        SharedPreferences gradoDia = getBaseContext().getSharedPreferences("gradoDia", MODE_PRIVATE);
        if (gradoDia != null) {
            gDia = (float) gradoDia.getFloat("gradoDia", 0);
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
            Toast.makeText(this, "pailas", Toast.LENGTH_SHORT).show();
        }
    }

    //MUESTRA LA FECHA SELECCIONADA SEGUN LO QUE ELIGIO EN EL PICKER
    public void cargadefecha(int year, int month, int day) {
        StringBuffer strBuffer = new StringBuffer();

        strBuffer.append(this.day);
        strBuffer.append(" / ");
        strBuffer.append(this.month + 1);
        strBuffer.append(" / ");
        strBuffer.append(this.year);
        fech.setText(strBuffer.toString());
        fech.setTextSize(30);
    }

    public List<conteoTab> calcular() {
        List<conteoTab> clc = new ArrayList<>();

        try {
            iConteo iC = new iConteo(path);
            iC.nombre = fecha;

            // Toast.makeText(this, path + fecha + ".json", Toast.LENGTH_LONG).show();

            List<conteoTab> cl = iC.all();
            for (conteoTab c : cl) {
                boolean val = true;
                for (int i = 0; i <= clc.size()-1; i++) {



                    if (c.getIdVariedad() == clc.get(i).getIdVariedad() || c.getIdBloque() == clc.get(i).getIdBloque()) {

                        int cu = clc.get(i).getCuadro() + 1;
                        int c1 = clc.get(i).getConteo1() + c.getConteo1();
                        int c4 = clc.get(i).getConteo4() + c.getConteo4();

                        clc.get(i).setCuadro(cu);
                        clc.get(i).setConteo1(c1);
                        clc.get(i).setConteo4(c4);
                        val = false;
                    }
                }
                if (val) {
                    c.setCuadro(1);
                    clc.add(c);
                }

                // Toast.makeText(this, "Dato 1: " + c.toString() + "\n Dato 2: " + clc.toString(), Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            Toast.makeText(this, "Error exception: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        return clc;
    }

    // Dibuja la tabla de calculos
    public ArrayList<String[]> cargarConteo() {
        DecimalFormat frt = new DecimalFormat("#,###.00");

        ArrayList<String[]> rows = new ArrayList<>();

        try {
            rows.clear();
            iConteo iC = new iConteo(path);
            iC.nombre = fecha;

            // Toast.makeText(this, path + fecha + ".json", Toast.LENGTH_LONG).show();

            List<conteoTab> cl = calcular();

            // Toast.makeText(this, "" + cl.size(), Toast.LENGTH_LONG).show();


            for (conteoTab c : cl) {
                // {"Finca", "Bloque", "Variedad", "CC", "CT", "S1C", "S1P", "S4C", "S4P
                rows.add(new String[]{

                                c.getBloque(),
                                c.getVariedad(),
                                String.valueOf(c.getCuadro()),
                                String.valueOf(c.getCuadros()),
                                String.valueOf(c.getConteo1()),
                                String.valueOf(extrapolar(c.getCuadros(), c.getCuadro(), c.getConteo1())),
                                String.valueOf(c.getConteo4()),
                                String.valueOf(extrapolar(c.getCuadros(), c.getCuadro(), c.getConteo4())),
                        }
                );
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error exception: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        return rows;
    }

    public int extrapolar(int CT, int CC, int S) {
        return (S * CT) / CC;

    }

    //CREACION DE LA TABLA
    public void createTable() {
        try {
            tableLayout = findViewById(R.id.tabla);
            TableDinamic tb = new TableDinamic(tableLayout, getApplicationContext());
            tb.addHeader(header);
            tb.addData(cargarConteo());
            tb.backgroundHeader(
                    Color.parseColor("#20C0FF")
            );
            tb.backgroundData(
                    Color.parseColor("#FFFFFF"),
                    Color.parseColor("#E5DBDBDB")
            );

        } catch (Exception e) {


            gradosDiaTxt = findViewById(R.id.gradosDia);
            int datodia1 = Integer.parseInt(gradosDiaTxt.getText().toString());
            //Toast.makeText(this,"se pasa el grado: "+datodia1,Toast.LENGTH_LONG).show();

            String datodia = gradosDiaTxt.getText().toString();

            Toast.makeText(this, "Error table: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    //ACTUALIZA LOS PLANOS
    public void actualizarBases(View v) {
        try {
            iFenologia iF = new iFenologia(path);
            iPlano iP = new iPlano(path);

            if (iP.local() && iF.local()) {
                Toast.makeText(this, "Local actualizado exitosamente", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "No se dispone de conexion:\n"
                    + "Trabajando con recursos locales\n"
                    + "Codigo: " + e.hashCode(), Toast.LENGTH_LONG).show();
        }
    }
}
