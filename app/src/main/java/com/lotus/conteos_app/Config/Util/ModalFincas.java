package com.lotus.conteos_app.Config.Util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lotus.conteos_app.Model.iCuadrosBloque;
import com.lotus.conteos_app.Model.iFenologia;
import com.lotus.conteos_app.Model.iFincas;
import com.lotus.conteos_app.Model.iPlano;
import com.lotus.conteos_app.Model.tab.fincasTab;
import com.lotus.conteos_app.R;

import java.util.ArrayList;
import java.util.List;

public class ModalFincas {
    Context context;
    LinearLayout linearLayout;
    android.app.Dialog d;
    String path;

    List<Integer> fincas = new ArrayList<>();

    public ModalFincas(Context context, String path) {
        this.context = context;
        this.path = path;
    }

    public void crear(){
        d = new Dialog(context);
        d.setContentView(R.layout.modalinfo);
        d.setTitle("Selecciona las fincas para descargar los datos");

        Window windowfinca = d.getWindow();
        windowfinca.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ins();

        d.show();
    }

    public void ins(){
        linearLayout = d.findViewById(R.id.linearMod);
        linearLayout.addView(panel());
    }

    public void cerrar(){
        d.hide();
    }

    public View panel(){
        LinearLayout line = new LinearLayout(context);
        line.setOrientation(LinearLayout.VERTICAL);
        //line.addView(Header());
        line.addView(Checks());
        line.addView(Footer());
        return line;
    }

    public View Header(){
        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 120);

        TextView t = new TextView(context);
        t.setText("Selecciona las fincas para descargar los datos");
        t.setTextColor(Color.parseColor("#34495E"));
        t.setTypeface(null, Typeface.BOLD);
        t.setBackgroundResource(R.drawable.border_header);
        t.setTextSize(20);
        t.setGravity(Gravity.CENTER_VERTICAL);
        t.setLayoutParams(l);

        return t;
    }

    public View Checks(){
        try {
            LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            l.setMargins(100,0,100,0);

            iFincas iF = new iFincas(path);
            LinearLayout line = new LinearLayout(context);
            line.setOrientation(LinearLayout.VERTICAL);
            line.setLayoutParams(l);

            LinearLayout.LayoutParams ls = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
            ScrollView sc = new ScrollView(context);
            sc.setLayoutParams(ls);

            CheckBox cb;
            for(fincasTab f : iF.all()){
                cb = new CheckBox(context);
                cb.setText(f.getNombre());
                cb.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                //cb.setButtonDrawable(R.drawable.boton_checkbox);
                cb.setTextColor(Color.parseColor("#34495E"));
                line.addView(cb);

                funCheck(cb, f);
            }

            sc.addView(line);
            return sc;
        }catch (Exception e) {
            Toast.makeText(context, "Posiblemente no hay datos de fincas, Error al cargar selecciones", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public View Footer(){
        LinearLayout line = new LinearLayout(context);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setBackgroundResource(R.drawable.boder_footer);
        line.setWeightSum(2);
        line.setPadding(5,5,5,5);

        btns(line);

        return line;
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

        btnFun(btna, btnc);

        line.addView(btna);
        line.addView(btnc);
    }

    public void btnFun(Button btna, Button btnc){
        btna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                try {
                    iPlano ip = new iPlano(path);
                    iFenologia iFen = new iFenologia(path);
                    iCuadrosBloque iCB = new iCuadrosBloque(path);

                    if(fincas.size() > 0) {
                        consultandooFincas();


                        String fincasSinCorchetes = fincas.toString().substring(1,fincas.toString().length()-1);

                        Log.i("FINCAS","sin corchetes : "+fincasSinCorchetes);

                        if (iCB.local()  && iFen.local() && ip.crearPlano(fincasSinCorchetes)) {
                            Toast.makeText(context, "Local actualizado exitosamente", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context, "Debes seleccionar al menos un finca para la descarga", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    Log.i("FINCAS",""+e);
                    Toast.makeText(context, "Ocurrio un error al cargar el plano de siembra \n"+e.toString(), Toast.LENGTH_LONG).show();
                }

            }
        });

        btnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrar();
            }
        });
    }

    public void funCheck(final CheckBox cb,final fincasTab f){
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb.isChecked()){
                    fincas.add(f.getId());
                }else{
                    List<Integer> newFincas = new ArrayList<>();
                    for(int id : fincas) {
                        if (id != f.getId()){ newFincas.add(id);}
                    }
                    fincas.clear();
                    fincas = newFincas;
                }
                consultandooFincas();
            }
        });
    }

    public void consultandooFincas(){
        //Toast.makeText(context, "CONSULTANDO", Toast.LENGTH_SHORT).show();
        Log.i("FINCAS","==================iterando ids======================");
        for(int id : fincas){
            Log.i("FINCAS",""+id);
        }
        Log.i("FINCAS","=============termino de iterar==============");
    }

    public int color(String c){
        int getColor = Color.parseColor(c);
        return getColor;
    }
}
