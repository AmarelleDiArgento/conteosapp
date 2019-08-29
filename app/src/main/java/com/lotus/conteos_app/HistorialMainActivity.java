package com.lotus.conteos_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lotus.conteos_app.Config.Util.jsonAdmin;
import com.lotus.conteos_app.Model.iFenologia;
import com.lotus.conteos_app.Model.iPlano;
import com.lotus.conteos_app.Model.tab.planoTab;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistorialMainActivity extends AppCompatActivity {

    List<planoTab> pl1 = new ArrayList<>();


    private int year;
    private int month;
    private int day;

    jsonAdmin ja = null;
    String path = null;
    EditText data_tbl,gradosDiaTxt;
    DatePicker date;
    ImageView btn_show_picker;
    TextView fech;
    //RecyclerView data_tbl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historial_main);
        // INICIAR LISTAS

        // actualizarPlano();
        // actualizarFenologias();


        //data_tbl =(EditText) findViewById(R.id.data_tbl);
        data_tbl =(EditText) findViewById(R.id.data_tbl);
        date = (DatePicker) findViewById(R.id.date_picker);
        btn_show_picker=(ImageButton) findViewById(R.id.btn_datapicker);
        fech =(TextView)findViewById(R.id.txt_fecha);

        ja = new jsonAdmin();

        //INICIALIZAMOS PLANOS
        cargarHistorial();
        //obtiene ruta donde se encuentran los archivos.
        path = getExternalFilesDir(null) + File.separator;

        date.setVisibility(View.INVISIBLE);

        Calendar currCalendar = Calendar.getInstance();

        year = currCalendar.get(Calendar.YEAR);
        month = currCalendar.get(Calendar.MONTH);
        day = currCalendar.get(Calendar.DAY_OF_MONTH);


        date.init(year - 1, month  + 1, day + 5, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                HistorialMainActivity.this.year = year;
                HistorialMainActivity.this.month = month;
                HistorialMainActivity.this.day = day;

                cargadefecha(year,month,day);
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
        strBuffer.append(this.month+1);
        strBuffer.append(" / ");
        strBuffer.append(this.day);
        //Toast.makeText(this,strBuffer.toString(),Toast.LENGTH_SHORT).show();
        fech.setText(strBuffer.toString());
        fech.setTextSize(30);
    }

    public void showpicker(View v){

        if (btn_show_picker.isClickable() && date.getVisibility()==View.INVISIBLE){
            date.setVisibility(View.VISIBLE);
        }else if(btn_show_picker.isClickable() && date.getVisibility()==View.VISIBLE){
            date.setVisibility(View.INVISIBLE);
        }else{
            Toast.makeText(this,"pailas",Toast.LENGTH_SHORT).show();
        }
    }


    // descarga el plano de la basde datos y lo alamcena en plano.json (Archivo local)
    public void cargarHistorial() {

        try {
            iPlano iP = new iPlano();
            String nombre = "plano";
            String contenido = iP.all().toString();
            data_tbl.setText(contenido);
        } catch (Exception e) {
            Toast.makeText(this, "Error exception" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void intent_home(View v){

        gradosDiaTxt=(EditText)findViewById(R.id.gradosDia);

        int dato = Integer.parseInt(gradosDiaTxt.getText().toString()) ;

        // Toast.makeText(this,"se pasa el grado: "+dato,Toast.LENGTH_LONG).show();

        Intent intent = new Intent (HistorialMainActivity.this,MainActivity.class);
        //Exportar parametro
        intent.putExtra("grados", dato);
        startActivityForResult(intent, 0);
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



    public void actualizarBases(View v){
        // INICIAR LISTAS

        actualizarPlano();
        actualizarFenologias();

    }
}
