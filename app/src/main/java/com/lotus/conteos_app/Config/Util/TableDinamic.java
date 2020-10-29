package com.lotus.conteos_app.Config.Util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lotus.conteos_app.ActivityDetalles;
import com.lotus.conteos_app.Model.tab.conteoTab;
import com.lotus.conteos_app.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class TableDinamic {
    public static int id;
    private TableLayout tableLayout;
    private Context context;
    private String[] header;
    private ArrayList<String[]> data;
    private TableRow tableRow;
    private TextView txtCell;
    private int r, c;
    private int idtabla;

    List<conteoTab> clc;
    String operacion;

    EditText edtConteo1, edtConteo4, total;
    TextView txtIdReg, txtSiembra, txtBloque, txtVariedad;

    SharedPreferences sp;

    public TableDinamic(TableLayout tableLayout, Context context, String operacion , List<conteoTab> clc, EditText edtConteo1, EditText edtConteo4, EditText total, TextView txtIdReg, TextView txtSiembra,TextView txtBloque,TextView txtVariedad) {
        sp = context.getSharedPreferences("share", MODE_PRIVATE);
        this.tableLayout = tableLayout;
        this.context = context;
        this.operacion = operacion;
        this.clc = clc;
        this.edtConteo1 = edtConteo1;
        this.edtConteo4 = edtConteo4;
        this.total = total;
        this.txtIdReg = txtIdReg;
        this.txtSiembra = txtSiembra;
        this.txtBloque = txtBloque;
        this.txtVariedad = txtVariedad;
    }

    public void addHeader(String[] header) {
        this.header = header;
        createHeader();
    }

    public void addData(ArrayList<String[]> data) {
        this.data = data;
        createDataTable();
    }

    private void newRow() {
        tableRow = new TableRow(context);
    }

    private void newCell() {
        txtCell = new TextView(context);
        txtCell.setGravity(Gravity.CENTER);
        txtCell.setTextColor(Color.BLACK);
        txtCell.setTextSize(15);
        txtCell.setHeight(50);
    }

    private TableRow.LayoutParams newLayoutParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.weight = 1;
        return params;
    }

    private void createHeader() {
        c = 0;
        newRow();
        while (c < header.length) {
            newCell();
            txtCell.setText(header[c++]);
            tableRow.addView(txtCell, newLayoutParams());
            tableRow.setVerticalScrollBarEnabled(false);
        }
        tableLayout.addView(tableRow);
    }

    private TableLayout createDataTable() {
        String info;

        for (r = 1; r <= data.size(); r++) {
            newRow();
            for (c = 0; c < header.length; c++) {
                newCell();
                final String[] colums = data.get(r - 1);
                info = (c < colums.length) ? colums[c] : "";
                txtCell.setText(info);
                tableRow.addView(txtCell, newLayoutParams());
                tableRow.setId(r);
                try {
                    tableRow.clearDisappearingChildren();
                    tableRow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            if(operacion.equals("cargarDetalle")){
                                cargarDetalle(view.getId());
                            }else{
                                enviarDetalle(view.getId());
                            }

                            setIdTabla(id);
                            view.setBackgroundColor(Color.parseColor("#92F0EDED"));
                            int dur = 1000;
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    view.setBackgroundColor(Color.WHITE);
                                }
                            }, dur);
                        }
                    });
                } catch (Exception E) {
                    Toast.makeText(context, "Error \n" + E.toString(), Toast.LENGTH_SHORT).show();
                }


            }
            tableRow.clearFocus();
            tableLayout.addView(tableRow);
        }

        return tableLayout;
    }


    public void cargarDetalle(int id){
        try {
            conteoTab ct = clc.get(id - 1);
            edtConteo1.setText(""+ct.getConteo1());
            edtConteo4.setText(""+ct.getConteo4());
            total.setText(""+ct.getTotal());
            txtIdReg.setText("idReg: "+ct.getIdConteo());
            txtSiembra.setText("Siembra: "+ct.getIdSiembra());
            txtBloque.setText("Bloque:"+ct.getBloque());
            txtVariedad.setText("Variedad: "+ct.getVariedad());
        }catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    public void enviarDetalle(int id){
        conteoTab ct = clc.get(id - 1);

        SharedPreferences.Editor edit = sp.edit();
        edit.putString("date", getFechita());
        edit.putInt("bloque", ct.getIdBloque());
        edit.putInt("idvariedad", ct.getIdVariedad());
        edit.putString("usulog", getFechita());
        edit.apply();

        Intent i = new Intent(context, ActivityDetalles.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public void backgroundHeader(int head) {
        c = 0;
        while (c < header.length) {
            txtCell = getCell(0, c++);
            txtCell.setBackgroundColor(Color.parseColor("#2980B9"));
            txtCell.setTextColor(Color.parseColor("#FDFEFE"));
            txtCell.setTextSize(18);
        }
    }

    public void backgroundData(int firt, int second) {
    }

    private TableRow getRow(int index) {
        return (TableRow) tableLayout.getChildAt(index);
    }

    private TextView getCell(int fil, int col) {
        tableRow = getRow(fil);
        return (TextView) tableRow.getChildAt(col);
    }

    public void setIdTabla(int idTabla) {
        this.idtabla = idTabla;
    }

    public int getIdTabla() {
        return this.idtabla;
    }

    public String getFechita(){
        String f = "";
        if(sp != null){
            f = sp.getString("fechaBusqueda", "");
        }
        return f;
    }

}
