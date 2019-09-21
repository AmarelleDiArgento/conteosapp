package com.lotus.conteos_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lotus.conteos_app.Config.Util.TableDinamic;
import com.lotus.conteos_app.Model.iConteo;
import com.lotus.conteos_app.Model.tab.conteoTab;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ActivityDetalles extends AppCompatActivity {

    SharedPreferences sp = null;

    List<conteoTab> clc = new ArrayList<>();

    String path = null;
    String fecha = "";
    private TableLayout tableLayout;
    TableDinamic tb;

    TextView txtIdSiembra,txtCuadro,txtBloque,txtVariedad;
    EditText cap_1,cap_2,cap_ct;

    // Encabezados de la tabla
    private String[] header = {"id","Bloque", "Cuadro", "C1", "C2", "C3", "C4", "CT"};
    public TextView  fechaoculta;
    // createTable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);
        getSupportActionBar().hide();

        txtIdSiembra=findViewById(R.id.txtIdSiembra);
        txtBloque=findViewById(R.id.txtBloque);
        txtCuadro=findViewById(R.id.txtCuadro);
        txtVariedad=findViewById(R.id.txtVariedad);
        cap_1=findViewById(R.id.cap_c1);
        cap_2=findViewById(R.id.cap_c2);
        cap_ct=findViewById(R.id.cap_ct);

        path = getExternalFilesDir(null) + File.separator;
        createTable();
    }

    public void clicTable(View v) {

        try {
            tb = new TableDinamic(tableLayout, getApplicationContext());
            conteoTab ct = clc.get(tb.getIdTabla());
            Toast.makeText(this,"id \n"+ct.toString(),Toast.LENGTH_SHORT).show();
            String variedad = ct.getVariedad();
            String bloque= ct.getBloque();
            Long idSiembra= ct.getIdSiembra();
            String idSiempar= String.valueOf(idSiembra);
            int cuadro = ct.getCuadro();
            int conteo1 = ct.getConteo1();
            int conteo4 = ct.getConteo4();
            int total = ct.getTotal();

            //Toast.makeText(this,"variedad \n"+variedad,Toast.LENGTH_SHORT).show();
            //Toast.makeText(this,"bloque \n"+bloque,Toast.LENGTH_SHORT).show();
            //Toast.makeText(this,"id siembra \n"+idSiembra.toString(),Toast.LENGTH_SHORT).show();


            txtIdSiembra.setText("Id Siembra: "+idSiempar);
            txtBloque.setText("Bloque: "+bloque);
            txtCuadro.setText("Cuadro: "+cuadro);
            txtVariedad.setText("Variedad: "+variedad);
            cap_1.setText(String.valueOf(conteo1));
            cap_2.setText(String.valueOf(conteo4));
            cap_ct.setText(String.valueOf(total));

        }catch (Exception E){
            tostada("ERROR\n " + E.toString()).show();
        }

    }


    public List<conteoTab> calcular() {
        //String text = "";
        try {
            iConteo iC = new iConteo(path);
            fecha = fechaoculta.getText().toString();
            iC.nombre = fecha;

            List<conteoTab> cl = iC.all();
            for (conteoTab c : cl) {
                boolean val = true;
                    for (int i = 0; i <= clc.size() ; i++) {
                        if (  (c.getBloque().equals(sp.getString("bloque","")))  ) {
                            val = true;
                        }else {
                            val= false;
                        }
                    }
                if (val) {
                    clc.add(c);
                }else {}
            }
        } catch (Exception e) {
            Toast.makeText(this, "No existen registros actuales que coincidan con la fecha fecha", Toast.LENGTH_LONG).show();
            clc.clear();
        }
        return clc;
    }



    // Dibuja la tabla de calculos
    public ArrayList<String[]> cargarTabla() {

        final ArrayList<String[]> rows = new ArrayList<>();

        try {
            sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);

            iConteo iC = new iConteo(path);

            fechaoculta = findViewById(R.id.fechita);
            fechaoculta.setText(sp.getString("fechaoculta",""));



            String nomfecha=fechaoculta.getText().toString();
            String fecha = nomfecha;
            iC.nombre = fecha;

            final List<conteoTab> cl = calcular();

            for (final conteoTab c : cl) {

                rows.add(new String[]{
                                String.valueOf(c.getIdSiembra()),
                                String.valueOf(c.getBloque()),
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

    public void cerrarsesion(View v) {
        Intent i = new Intent(this, Login.class);
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        Toast.makeText(this, "se ha cerrado sesion exitosamente", Toast.LENGTH_SHORT).show();
        finish();
    }



    Toast tostada(String mjs) {
        return Toast.makeText(this, mjs, Toast.LENGTH_LONG);
    }
}
