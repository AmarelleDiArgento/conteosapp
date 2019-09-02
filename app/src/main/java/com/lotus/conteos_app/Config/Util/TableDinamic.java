package com.lotus.conteos_app.Config.Util;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class TableDinamic {
    private TableLayout tableLayout;
    private Context context;
    private String[] header;
    private ArrayList<String[]> data;

    private TableRow tableRow;
    private TextView txtCell;
    private int r, c;
    private boolean multicolor = false;
    private int head, firt, second;

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
        txtCell.setTextSize(16);
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
        }
        tableLayout.addView(tableRow);
    }

    private void createDataTable() {
        String info;

        for (r = 1; r <= data.size(); r++) {
            newRow();
            for (c = 0; c < header.length; c++) {
                newCell();
                String[] colums = data.get(r - 1);
                info = (c < colums.length) ? colums[c] : "";
                txtCell.setText(info);
                tableRow.addView(txtCell, newLayoutParams());
                txtCell.setBackgroundColor((multicolor) ? firt : second);
            }
            tableLayout.addView(tableRow);
        }
    }

    public void backgroundHeader(int head) {
        c = 0;
        while (c < header.length) {
            txtCell = getCell(0, c++);
            txtCell.setBackgroundColor(head);
        }
    }


    public void backgroundData(int firt, int second) {
        for (r = 1; r <= data.size(); r++) {
            multicolor = !multicolor;
            for (c = 0; c < header.length; c++) {
                txtCell = getCell(r, c);
                txtCell.setBackgroundColor((multicolor) ? firt : second);
            }
        }
        this.firt = firt;
        this.second = second;
    }


    private TableRow getRow(int index) {
        return (TableRow) tableLayout.getChildAt(index);
    }

    private TextView getCell(int fil, int col) {
        tableRow = getRow(fil);
        return (TextView) tableRow.getChildAt(col);
    }

}
