package com.lotus.conteos_app.Config.Util;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lotus.conteos_app.R;

import java.util.ArrayList;

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

    public TableDinamic(TableLayout tableLayout, Context context) {
        this.tableLayout = tableLayout;
        this.context = context;
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
        // params.setMargins(1, 1, 1, 1);
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

    private void createDataTable() {
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
                //txtCell.setBackgroundColor((multicolor) ? firt : second);
                try {
                    tableRow.clearDisappearingChildren();
                    tableRow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            id = view.getId();
                            setIdTabla(id);
                            // Toast.makeText(context, "Fila " + id, Toast.LENGTH_SHORT).show();
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


}
