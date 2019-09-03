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
import com.lotus.conteos_app.Model.iFenologia;
import com.lotus.conteos_app.Model.iPlano;
import com.lotus.conteos_app.Model.tab.fenologiaTab;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistorialMainActivity extends AppCompatActivity {

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
    private String[] header = {"ID", "Variedad", "iD", "Grados Dia", "Diametro", "Largo", "Imagen"};
    // Datos de la tabla
    private ArrayList<String[]> rows = new ArrayList<>();

    //RecyclerView data_tbl;

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

            //INICIALIZAMOS PLANOS


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

            createTable();
            gradosDiaTxt.setText(String.valueOf(recibirGradoDia()));


        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

        }
    }

    // revisar
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

    public float recibirGradoDia() {
        SharedPreferences gradoDia = getBaseContext().getSharedPreferences("gradoDia", MODE_PRIVATE);
        if (gradoDia != null) {
            gDia = (float) gradoDia.getFloat("gradoDia", 0);
            return gDia;
        } else {
            return 0;
        }
    }

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
            rows.clear();
            iFenologia iF = new iFenologia(path);
            List<fenologiaTab> fl = iF.all();

            Toast.makeText(this, "" + fl.size(), Toast.LENGTH_LONG).show();

            for (fenologiaTab f : fl) {
                rows.add(new String[]{
                                String.valueOf(f.getIdFenologia()),
                                f.getVariedad(),
                                String.valueOf(f.getIdVariedad()),
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

    public void createTable() {
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


            gradosDiaTxt = findViewById(R.id.gradosDia);
            int datodia1 = Integer.parseInt(gradosDiaTxt.getText().toString());
            //Toast.makeText(this,"se pasa el grado: "+datodia1,Toast.LENGTH_LONG).show();

            String datodia = gradosDiaTxt.getText().toString();

            Toast.makeText(this, "Error table: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

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
