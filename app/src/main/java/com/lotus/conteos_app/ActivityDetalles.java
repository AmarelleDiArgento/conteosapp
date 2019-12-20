package com.lotus.conteos_app;

import android.content.DialogInterface;
import android.content.Intent;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityDetalles extends AppCompatActivity {

    public static int id;
    SharedPreferences sp = null;
    Calendar calendarDate = null;

    List<conteoTab> clc = new ArrayList<>();

    String path = null;
    String fecha = "";
    private TableLayout tableLayout;
    TableDinamic tb;
    iConteo iC = null;

    TextView txtId, txtCuadro, txtBloque, txtVariedad,  fechita, txtidReg;
    EditText cap_1, cap_2, cap_ct;

    // Encabezados de la tabla
    private String[] header = {"idReg", "Bloque", "Cuadro", "C1", "C2", "C3", "C4", "CT"};

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
            fechita = findViewById(R.id.usulog);
            txtCuadro = findViewById(R.id.txtCuadro);
            txtidReg = findViewById(R.id.idReg);

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

            getDate();
            cargarRecursos();

        }catch (Exception e){
            tostada("Error\n"+e).show();
        }
    }

    public void cargarRecursos(){
        SimpleDateFormat sdfn = new SimpleDateFormat("ddMMyyyy");
        try{
            iC = new iConteo(path);
            fecha = sp.getString("date", "");
            iC.nombre = fecha;
        }catch (Exception ex){
            Toast.makeText(this, "Exception al cargar recursos \n \n"+ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    //OBTENER FECHA ACTUAL
    public void getDate() {

        try {
            calendarDate = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
            fecha = sdf.format(calendarDate.getTime());


            //obteniendo el usuario
            String usuario = sp.getString("nombre", "");
            fechita.setText("Fecha: " + fecha + "\nUsuario: " + usuario);

        } catch (Exception e) {
            Toast.makeText(this, "Exception getDate" + e, Toast.LENGTH_LONG).show();
        }
    }

    //OBTENER EL ID DEL REGISTRO SEGUN LA FILA
    public void clicTable(View v) {

        try {
            conteoTab ct = clc.get(tb.getIdTabla() - 1);

            String variedad = ct.getVariedad();
            String bloque = ct.getBloque();
            Long idSiembra = ct.getIdSiembra();
            String idSiempar = String.valueOf(idSiembra);


            long idReg = ct.getIdConteo();
            int cuadro = ct.getCuadro();
            int conteo1 = ct.getConteo1();
            int conteo4 = ct.getConteo4();
            int total = ct.getTotal();

            txtidReg.setText("idReg:" + idReg);
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
        iConteo iC = new iConteo(path);
        try {
            fecha = sp.getString("date", "");
            iC.nombre = fecha;

            List<conteoTab> cl = iC.all();

            for (conteoTab c : cl) {
                boolean val = true;
                for (int i = 0; i <= clc.size(); i++) {
                    Toast.makeText(this,"id "+c.getIdConteo(),Toast.LENGTH_SHORT).show();
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

            final List<conteoTab> cl = filtro();

            for (final conteoTab c : cl) {

                rows.add(new String[]{
                                String.valueOf(c.getIdConteo()),
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
                            }else{}

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
        try {
            iC.nombre = fecha;

            String [] data = txtidReg.getText().toString().split(":");
            Long id = Long.parseLong(data[1].trim());

            int conteo1 = Integer.parseInt(cap_1.getText().toString());
            int conteo2 = Integer.parseInt(cap_2.getText().toString());
            int conteoT = Integer.parseInt(cap_ct.getText().toString());

            conteoTab cl = iC.oneId(id);

            conteoTab c = new conteoTab();

                c.setFecha(cl.getFecha());
                c.setIdConteo(cl.getIdConteo());
                c.setIdSiembra(cl.getIdSiembra());
                c.setCama(cl.getCama());
                c.setIdBloque(cl.getIdBloque());
                c.setBloque(cl.getBloque());
                c.setIdVariedad(cl.getIdVariedad());
                c.setVariedad(cl.getVariedad());
                c.setCuadro(cl.getCuadro());
                c.setConteo1(conteo1);
                c.setConteo4(conteo2);
                c.setTotal(conteoT);
                c.setPlantas(cl.getPlantas());
                c.setArea(cl.getArea());
                c.setCuadros(cl.getCuadros());
                c.setIdUsuario(cl.getIdUsuario());

                Toast.makeText(this, ""+iC.update(id-1,c), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this,ActivityDetalles.class);
                startActivity(i);
        }catch (Exception ex){
            Toast.makeText(this, "exception al actualizar el registro \n \n"+ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void borrar_registro() {
        try {

            int idauto = 0;

            iC.nombre = fecha;
            String[] data = txtidReg.getText().toString().split(":");
            Long id = Long.parseLong(data[1].trim());

            Toast.makeText(this, "" + iC.delete(id - 1), Toast.LENGTH_SHORT).show();

            List<conteoTab> ct  =  iC.all();

            conteoTab ct2 = new conteoTab();

            for(conteoTab ctt : ct){
                Toast.makeText(this, ""+iC.delete(ctt.getIdConteo()+1), Toast.LENGTH_SHORT).show();
                ct2.setFecha(ctt.getFecha());
                ct2.setIdConteo(idauto+1);
                ct2.setIdSiembra(ctt.getIdSiembra());
                ct2.setCama(ctt.getCama());
                ct2.setIdBloque(ctt.getIdBloque());
                ct2.setBloque(ctt.getBloque());
                ct2.setIdVariedad(ctt.getIdVariedad());
                ct2.setVariedad(ctt.getVariedad());
                ct2.setCuadro(ctt.getCuadro());
                ct2.setConteo1(ctt.getConteo1());
                ct2.setConteo2(ctt.getConteo2());
                ct2.setConteo3(ctt.getConteo3());
                ct2.setConteo4(ctt.getConteo4());
                ct2.setTotal(ctt.getTotal());
                ct2.setPlantas(ctt.getPlantas());
                ct2.setArea(ctt.getArea());
                ct2.setCuadros(ctt.getCuadros());
                ct2.setIdUsuario(ctt.getIdUsuario());

                iC.insertBeDelete(ct2);
            }

            Intent i = new Intent(this, ActivityDetalles.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }catch (Exception ex){
            Toast.makeText(this, "Exception al borrar el registro \n \n"+ex, Toast.LENGTH_SHORT).show();
        }

    }



    //TOSTADAS
    Toast tostada(String mjs) {
        return Toast.makeText(this, mjs, Toast.LENGTH_LONG);
    }
}
