package com.lotus.conteos_app.Config.Util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lotus.conteos_app.R;

public class modalAdmin {

    Context context;
    LinearLayout linearLayout;
    android.app.Dialog d;

    String msg;

    public modalAdmin(Context contex, String msg){
        this.context = contex;
        this.msg = msg;
    }

    public void crear(){
        d = new Dialog(context);
        d.setContentView(R.layout.modalinfo);


        Window window = d.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ins();
    }

    public void cerrar(){
        d.hide();
    }

    public void ins(){
        linearLayout = d.findViewById(R.id.linearMod);
        //linearLayout.addView(panel());
    }

    public void btns(LinearLayout line){
        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        l.weight = 1;

        int backgroud = color("#D7DBDD");
        int size = 15;

        Button btna = new Button(context);
        btna.setTypeface(null, Typeface.BOLD);
        btna.setBackgroundColor(backgroud);
        btna.setText("DESCARGAR");
        btna.setTextColor(color("#52BE80"));
        btna.setTextSize(size);
        btna.setLayoutParams(l);

        Button btnc = new Button(context);
        btnc.setTypeface(null, Typeface.BOLD);
        btnc.setBackgroundColor(backgroud);
        btnc.setText("CANCELAR");
        btnc.setTextColor(color("#EC7063"));
        btna.setTextSize(size);
        btnc.setLayoutParams(l);

        //btnFun(btna, btnc);

        line.addView(btna);
        line.addView(btnc);
    }

    public int color(String c){
        int getColor = Color.parseColor(c);
        return getColor;
    }

}
