package com.lotus.conteos_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lotus.conteos_app.Config.Util.TableDinamic;
import com.lotus.conteos_app.Config.Util.jsonAdmin;
import com.lotus.conteos_app.Model.iFenologia;
import com.lotus.conteos_app.Model.iPlano;
import com.lotus.conteos_app.Model.tab.fenologiaTab;
import com.lotus.conteos_app.Model.tab.planoTab;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistorialMainActivity extends AppCompatActivity {

    List<planoTab> pl1 = new ArrayList<>();
    jsonAdmin ja = null;
    String path = null;
    EditText data_tbl, gradosDiaTxt;
    DatePicker date;
    ImageView btn_show_picker;
    TextView fech;
    private TableLayout tableLayout;
    private int year;
    private int month;
    private int day;
    private String[] header = {"ID", "Variedad", "Grados Dia", "Diametro", "Largo", "Imagen"};
    private ArrayList<String[]> rows = new ArrayList<>();
    //RecyclerView data_tbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historial_main);
        // INICIAR LISTAS

        // actualizarPlano();
        // actualizarFenologias();


        //data_tbl =(EditText) findViewById(R.id.data_tbl);
        // data_tbl =(EditText) findViewById(R.id.data_tbl);
        try {
            tableLayout = findViewById(R.id.tabla);
            TableDinamic tb = new TableDinamic(tableLayout, getApplicationContext());
            tb.addHeader(header);
            tb.addData(cargarFenologias());
            tb.backgroundHeader(
                    Color.parseColor("#20C0FF")
            );
            tb.backgroundData(
                    Color.parseColor("#FFFFFF"),
                    Color.parseColor("#E5DBDBDB")
            );

        } catch (Exception e) {

            Toast.makeText(this, "Error table: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        date = findViewById(R.id.date_picker);
        btn_show_picker = (ImageButton) findViewById(R.id.btn_datapicker);
        fech = findViewById(R.id.txt_fecha);

        ja = new jsonAdmin();

        //INICIALIZAMOS PLANOS

        //obtiene ruta donde se encuentran los archivos.
        path = getExternalFilesDir(null) + File.separator;

        date.setVisibility(View.INVISIBLE);

        Calendar currCalendar = Calendar.getInstance();

        year = currCalendar.get(Calendar.YEAR);
        month = currCalendar.get(Calendar.MONTH);
        day = currCalendar.get(Calendar.DAY_OF_MONTH);


        date.init(year - 1, month + 1, day + 5, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                HistorialMainActivity.this.year = year;
                HistorialMainActivity.this.month = month;
                HistorialMainActivity.this.day = day;

                cargadefecha(year, month, day);
            }
        });
    }

    // descarga el plano de la basde datos y lo alamcena en plano.json (Archivo local)
    public void actualizarPlano() {

        try {
            iPlano iP = new iPlano();
            String nombre = "plano";
            String contenido = iP.all().toString();
            // Toast.makeText(this, contenido, Toast.LENGTH_LONG).show();

            if (ja.CrearArchivo(path, nombre, contenido)) {
                Toast.makeText(this, "Plano actualizado exitosamente", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Error al actualizar el plano", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Sin conexión\n trabajando con plano local. \n Code: " + e.hashCode(), Toast.LENGTH_LONG).show();
        }
    }

    public void actualizarFenologias() {

        try {
            iFenologia iF = new iFenologia();
            String nombre = "fenologias";
            String contenido = iF.all().toString();
            // Toast.makeText(this, contenido, Toast.LENGTH_LONG).show();

            if (ja.CrearArchivo(path, nombre, contenido)) {
                Toast.makeText(this, "Fenologias actualizadas exitosamente", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Error al actualizar las fenologias", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Sin conexión\n trabajando con fenologias locales. \n Code: " + e.hashCode(), Toast.LENGTH_LONG).show();
        }
    }

    public void cargadefecha(int year, int month, int day) {
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append(this.year);
        strBuffer.append(" / ");
        strBuffer.append(this.month + 1);
        strBuffer.append(" / ");
        strBuffer.append(this.day);
        //Toast.makeText(this,strBuffer.toString(),Toast.LENGTH_SHORT).show();
        fech.setText(strBuffer.toString());
        fech.setTextSize(30);
    }

    public void showpicker(View v) {

        if (btn_show_picker.isClickable() && date.getVisibility() == View.INVISIBLE) {
            date.setVisibility(View.VISIBLE);
        } else if (btn_show_picker.isClickable() && date.getVisibility() == View.VISIBLE) {
            date.setVisibility(View.INVISIBLE);
        } else {
            Toast.makeText(this, "pailas", Toast.LENGTH_SHORT).show();
        }
    }

    // descarga el plano de la basde datos y lo alamcena en plano.json (Archivo local)
    public ArrayList<String[]> cargarFenologias() {
        DecimalFormat frt = new DecimalFormat("#,###.00");
        try {

            iFenologia iF = new iFenologia();
            List<fenologiaTab> fl = iF.all();

            Toast.makeText(this, "" + fl.size(), Toast.LENGTH_LONG).show();
            for (fenologiaTab f : fl) {
                rows.add(new String[]{
                                String.valueOf(f.getIdFenologia()),
                                f.getVariedad(),
                                String.valueOf(frt.format(f.getGrados_dia())),
                                String.valueOf(frt.format(f.getDiametro_boton())),
                                String.valueOf(frt.format(f.getLargo_boton())),
                                f.getImagen()
                        }
                );
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error exception: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        return rows;
    }

    public void intent_home(View v) {

        gradosDiaTxt = findViewById(R.id.gradosDia);
        String datodia = gradosDiaTxt.getText().toString();
        //Toast.makeText(this,"se pasa el grado: "+datodia,Toast.LENGTH_LONG).show();
        SharedPreferences guardarRut = getBaseContext().getSharedPreferences("guardarRut", MODE_PRIVATE);
        SharedPreferences.Editor edit = guardarRut.edit();
        edit.putString("rut", datodia);
        edit.commit();
        edit.apply();

        Intent intent = new Intent(HistorialMainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void listFiles() {
        try {
            List<String> list = ja.listFiles(path);
            // asociar arreglo cuadros al desplegable cuadro
            ArrayAdapter<String> fl = new ArrayAdapter<>(this, R.layout.spinner_item_personal, list);
            // files.setAdapter(fl);
            //data.setText(list.toString());
        } catch (Exception e) {
            Toast.makeText(this, "LISTFILES()--->  " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void actualizarBases(View v) {
        // INICIAR LISTAS
        actualizarPlano();
        actualizarFenologias();

    }
}
