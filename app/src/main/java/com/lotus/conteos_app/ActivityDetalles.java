package com.lotus.conteos_app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.Toast;

import com.lotus.conteos_app.Config.Util.TableDinamic;
import com.lotus.conteos_app.Model.iConteo;
import com.lotus.conteos_app.Model.tab.conteoTab;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ActivityDetalles extends AppCompatActivity {
    String path = null;
    private TableLayout tableLayout;
    // Encabezados de la tabla
    private String[] header = {"idCama", "Cuadro", "C1", "C2", "C3", "C4", "CT"};

    // createTable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        path = getExternalFilesDir(null) + File.separator;
        createTable();
    }

    // Dibuja la tabla de calculos
    public ArrayList<String[]> cargarTabla() {

        final ArrayList<String[]> rows = new ArrayList<>();

        try {

            iConteo iC = new iConteo(path);
            String fecha = "16092019";
            iC.nombre = fecha;

            List<conteoTab> cl = iC.all();

            for (final conteoTab c : cl) {
                // {"Finca", "Bloque", "Variedad", "CC", "CT", "S1C", "S1P", "S4C", "S4P
                rows.add(new String[]{
                                String.valueOf(c.getIdSiembra()),
                                String.valueOf(c.getCuadro()),
                                String.valueOf(c.getConteo1()),
                                String.valueOf(c.getConteo2()),
                                String.valueOf(c.getConteo3()),
                                String.valueOf(c.getConteo4()),
                                String.valueOf((c.getConteo1() + c.getConteo2() + c.getConteo3() + c.getConteo4())),
                        }
                );
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error exception Cargar conteo: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        return rows;
    }

    //CREACION DE LA TABLA
    public void createTable() {
        try {
            tableLayout = findViewById(R.id.tabla);
            TableDinamic tb = new TableDinamic(tableLayout, getApplicationContext());
            tableLayout.removeAllViews();
            tb.addHeader(header);
            tb.addData(cargarTabla());
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
}
