package com.lotus.conteos_app;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

    public static int id;
    SharedPreferences sp = null;

    List<conteoTab> clc = new ArrayList<>();

    String path = null;
    String fecha = "";
    private TableLayout tableLayout;
    TableDinamic tb;

    TextView txtId, txtCuadro, txtBloque, txtVariedad, fechita, usulog;
    EditText cap_1, cap_2, cap_ct;

    // Encabezados de la tabla
    private String[] header = {"id", "Bloque", "Cuadro", "C1", "C2", "C3", "C4", "CT"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);
        getSupportActionBar().hide();

        sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);
        try {
            txtId = findViewById(R.id.txtIdSiembra);
            txtVariedad = findViewById(R.id.txtVariedad);
            txtBloque = findViewById(R.id.txtBloque);
            cap_1 = findViewById(R.id.cap_c1);
            cap_2 = findViewById(R.id.cap_c2);
            cap_ct = findViewById(R.id.cap_ct);
            fechita = findViewById(R.id.fechita);
            usulog = findViewById(R.id.usuLog);
            txtCuadro = findViewById(R.id.txtCuadro);

//            String usuario = sp.getString("usulog", "");
//            usulog.setText(usuario);

            path = getExternalFilesDir(null) + File.separator;
            createTable();

            cap_1.setSelectAllOnFocus(true);
            cap_2.setSelectAllOnFocus(true);
            cap_ct.setSelectAllOnFocus(true);

            cap_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.clearFocus();
                    view.requestFocus();
                }
            });

            cap_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.clearFocus();
                    view.requestFocus();
                }
            });

            cap_ct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.clearFocus();
                    view.requestFocus();
                }
            });

        }catch (Exception e){
            tostada("Error\n"+e).show();
        }
    }

    //OBTENER EL ID DEL REGISTRO SEGUN LA FILA
    public void clicTable(View v) {

        try {

            Toast.makeText(this, "ID " + tb.getIdTabla(), Toast.LENGTH_LONG).show();
            conteoTab ct = clc.get(tb.getIdTabla() - 1);

            String variedad = ct.getVariedad();
            String bloque = ct.getBloque();
            Long idSiembra = ct.getIdSiembra();
            String idSiempar = String.valueOf(idSiembra);

            int cuadro = ct.getCuadro();
            int conteo1 = ct.getConteo1();
            int conteo4 = ct.getConteo4();
            int total = ct.getTotal();

            Toast.makeText(getApplicationContext(), "Total\n" + ct.getTotal(), Toast.LENGTH_LONG).show();

            txtCuadro.setText("Cuadro: " + cuadro);
            cap_1.setText(String.valueOf(conteo1));
            cap_2.setText(String.valueOf(conteo4));
            cap_ct.setText(String.valueOf(total));
            txtId.setText("Siembra: " + idSiempar);
            txtVariedad.setText("Variedad: " + variedad);
            txtBloque.setText("Bloque: " + bloque);

        } catch (Exception E) {
            Toast.makeText(getApplicationContext(), "No has seleccionado aún una fila \n" + E, Toast.LENGTH_LONG).show();
        }
    }

    //FILTRO DE LA TABLA POR BLOQUE
    public List<conteoTab> filtro() throws Exception {
        //String text = "";
        iConteo iC = new iConteo(path);
        try {
            fecha = sp.getString("date", "");
            iC.nombre = fecha;

            List<conteoTab> cl = iC.all();

            for (conteoTab c : cl) {
                boolean val = true;
                for (int i = 0; i <= clc.size(); i++) {
                    if (c.getIdBloque() == sp.getInt("bloque", 0) || c.getIdVariedad() == sp.getInt("idvariedad", 0)) {
                        val = true;
                    } else {
                        val = false;
                    }
                }
                if (val) {
                    clc.add(c);
                } else {
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "No existen registros actuales que coincidan con la fecha", Toast.LENGTH_LONG).show();
            clc.clear();
        }
        return clc;
    }

    // Dibuja la tabla
    public ArrayList<String[]> cargarTabla() {

        final ArrayList<String[]> rows = new ArrayList<>();
        try {

            iConteo iC = new iConteo(path);

            iC.nombre = fecha;

            final List<conteoTab> cl = filtro();

            for (final conteoTab c : cl) {

                rows.add(new String[]{
                                String.valueOf(c.getIdSiembra()),
                                String.valueOf(c.getBloque()),
                                String.valueOf(c.getCuadro()),
                                String.valueOf(c.getConteo1()),
                                String.valueOf(c.getConteo2()),
                                String.valueOf(c.getConteo3()),
                                String.valueOf(c.getConteo4()),
                                String.valueOf((c.getTotal())),
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
            tb = new TableDinamic(tableLayout, getApplicationContext());
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


    //BOTONES PARA ACTIVAR LOS DIALOGOS
    public void btn_actualizar(View v) {
        String conteo1 = cap_1.getText().toString();
        String conteo2 = cap_2.getText().toString();

        if (conteo1.isEmpty()) {
            tostada("No haz seleccionado un registro para actualizar").show();
        } else if (conteo2.isEmpty()) {
            tostada("No haz seleccionado un registro para actualizar").show();
        } else {
            String msj = "Seguro que deseas actualizar este registro";
            String tipo = "btn_actualizar";
            DialogConfirm(msj, tipo);
        }
    }

    public void btn_borrar_registro(View v) {
        String conteo1 = cap_1.getText().toString();
        String conteo2 = cap_2.getText().toString();

        if (conteo1.isEmpty()) {
            tostada("No haz seleccionado un registro para borrar").show();
        } else if (conteo2.isEmpty()) {
            tostada("No haz seleccionado un registro para borrar").show();
        } else {
            String msj = "Seguro que deseas eliminar este registro";
            String tipo = "btn_borrar_registro";
            DialogConfirm(msj, tipo);
        }
    }

    public void btn_limpiar(View v) {
        String conteo1 = cap_1.getText().toString();
        String conteo2 = cap_2.getText().toString();

        if (conteo1.isEmpty()) {
            tostada("No haz seleccionado un registro para borrar").show();
        } else if (conteo2.isEmpty()) {
            tostada("No haz seleccionado un registro para borrar").show();
        } else {
            String msj = "Seguro que deseas borrar todo lo relacionado con este bloque";
            String tipo = "btn_limpiar";
            DialogConfirm(msj, tipo);
        }
    }


    //MENSAJES DE CONFIRMACIÓN PARA EJECUTAR LOS METODO DEL CRUD
    public void DialogConfirm(String msj, String tipo) {
        try {
            final String metodo = tipo;
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(ActivityDetalles.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
            alertdialog.setMessage(msj)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // agrega aqui la funcion que quieres hacer con el boton positivo
                            if (metodo.equals("btn_actualizar")) {
                                actualizar_registro();
                            } else if (metodo.equals("btn_borrar_registro")) {
                                borrar_registro();
                            } else if (metodo.equals("btn_limpiar")) {
                                limpiar();
                            }

                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // agrega aqui la funcion que quieres hacer con el boton negativo
                        }
                    });
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDetalles.this);
            builder.setInverseBackgroundForced(true);
            AlertDialog mensaje = alertdialog.create();
            mensaje.show();
        } catch (Exception e) {
            Toast.makeText(this, "error\n" + e, Toast.LENGTH_LONG).show();
        }
    }


    //METODOS PARA HACER EL CRUD
    public void actualizar_registro() {
        Toast.makeText(getApplicationContext(), "llega al metodo actualizar", Toast.LENGTH_SHORT).show();
    }

    public void borrar_registro() {
        Toast.makeText(getApplicationContext(), "llega al metodo borrar", Toast.LENGTH_SHORT).show();
    }

    public void limpiar() {
        Toast.makeText(getApplicationContext(), "llega al metodo limpiar", Toast.LENGTH_SHORT).show();
    }


    //TOSTADAS
    Toast tostada(String mjs) {
        return Toast.makeText(this, mjs, Toast.LENGTH_LONG);
    }
}
