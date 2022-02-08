 package com.lotus.conteos_app;

 import android.graphics.Color;
 import android.graphics.drawable.GradientDrawable;
 import android.os.Build;
 import android.os.Bundle;
 import android.support.annotation.RequiresApi;
 import android.support.v7.app.AppCompatActivity;
 import android.support.v7.recyclerview.extensions.ListAdapter;
 import android.util.Log;
 import android.view.Gravity;
 import android.view.View;
 import android.widget.Button;
 import android.widget.CheckBox;
 import android.widget.LinearLayout;
 import android.widget.ScrollView;
 import android.widget.TextView;
 import android.widget.Toast;

 import com.lotus.conteos_app.Model.iCuadrosBloque;
 import com.lotus.conteos_app.Model.iFenologia;
 import com.lotus.conteos_app.Model.iFincas;
 import com.lotus.conteos_app.Model.iPlano;
 import com.lotus.conteos_app.Model.tab.fincasTab;
 import com.lotus.conteos_app.Model.tab.planoTab;

 import java.io.File;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;

 @RequiresApi(api = Build.VERSION_CODES.N)
 public class download_farm_screen extends AppCompatActivity {

     ScrollView scrollView;
     CheckBox cbxSelectedAllFarm;
     LinearLayout linearDownload;
     TextView txtCountFamr, txtCantidadGeneral;

    iFincas ifincas = null;
    iPlano iplano = null;

    List<_getItemsFarms> lstItemsSelected = new ArrayList<>();

    String path;
    long countSelectedFarms = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.activity_download_farm_screen);
            getSupportActionBar().hide();

            path = getExternalFilesDir(null) + File.separator;

            insEntity();
            insView();
            getListAllFamrItems();
            _getPaintDownloadsFarm();
            updateStateItemFarm();
        }catch (Exception e){
            Toast.makeText(this, "Exception download : "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void insEntity(){
        try{
            iplano = new iPlano(path);
            ifincas = new iFincas(path);
        }catch (Exception e) {
            Toast.makeText(this, "ExceptionIns : "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void insView(){
        try {
            txtCountFamr = new TextView(this);
            txtCantidadGeneral = findViewById(R.id.txtCantidadGeneral);

            txtCantidadGeneral.setText("Cantidad de fincas : " +(ifincas.all().size()));

            scrollView = findViewById(R.id.srollView);
            cbxSelectedAllFarm = findViewById(R.id.cbxSelectedAllFarm);
            linearDownload = findViewById(R.id.linearDownload);

            _funAllSelectionFarm(cbxSelectedAllFarm);
        }catch (Exception e){
            Toast.makeText(this, "Exceptin insView : "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getListAllFamrItems(){
        try{
            LinearLayout lineItem = new LinearLayout(this);
            lineItem.setOrientation(LinearLayout.VERTICAL);

            List<fincasTab> lstFincas = ifincas.all();
            if(lstFincas != null){

                int indexCreated = 0;
                for(fincasTab f : lstFincas){
                    lineItem.addView(_getItemFarm(indexCreated, f));
                    indexCreated++;
                }

                scrollView.addView(lineItem);
            }
        }catch (Exception e){
            Toast.makeText(this, "Exception getListAllFamrItems : "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public View _getItemFarm(int indexCreated, fincasTab f){
        /** Linear principal */

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        p.setMargins(10,10,10,10);

        LinearLayout lineParent = new LinearLayout(this);
        lineParent.setGravity(View.TEXT_ALIGNMENT_CENTER);
        lineParent.setBackgroundColor(Color.parseColor("#ffffff"));
        lineParent.setPadding(1,25,0,25);
        lineParent.setOrientation(LinearLayout.HORIZONTAL);
        lineParent.setLayoutParams(p);
        lineParent.setWeightSum(2);


        /** Linear status y nombre */
        LinearLayout lineNameStatus = new LinearLayout(this);
        lineNameStatus.setOrientation(LinearLayout.VERTICAL);
        lineNameStatus.setLayoutParams(paramLine("HORIZONTAL"));
        lineNameStatus.setWeightSum(2);


        TextView txtNameFarm = new TextView(this);
        txtNameFarm.setText(f.getId()+" - "+f.getNombre());


        TextView txtStatusFarm = new TextView(this);
        txtStatusFarm.setText("status");

        lineNameStatus.addView(txtNameFarm);
        lineNameStatus.addView(txtStatusFarm);


        /** Linear actiones */
        LinearLayout lineWorkCheck = new LinearLayout(this);
        lineWorkCheck.setOrientation(LinearLayout.HORIZONTAL);
        lineWorkCheck.setLayoutParams(paramLine("HORIZONTAL"));
        lineWorkCheck.setWeightSum(2);
        lineWorkCheck.setGravity(Gravity.CENTER);

        CheckBox cb = new CheckBox(this);

        lstItemsSelected.add(new _getItemsFarms(indexCreated, f.getId(), cb, false, txtStatusFarm));
        _funCheckSelection(indexCreated, cb, new _getItemsFarms(indexCreated, f.getId(), cb, false, txtStatusFarm), txtStatusFarm);

        lineWorkCheck.addView(cb);



        lineParent.addView(lineNameStatus);
        lineParent.addView(lineWorkCheck);

        return lineParent;
    }

    public LinearLayout.LayoutParams paramLine(String orientation){
        LinearLayout.LayoutParams p;

        if(orientation.equals("HORIZONTAL")){
            p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }else{
            p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }

        p.weight = 1;
        return p;
    }

    public void _funCheckSelection(final int indexCreated, final CheckBox cb, final _getItemsFarms itemTab, final TextView txtStatus){
        /**actualiza el estado de seleccion*/
        cb.setOnCheckedChangeListener((compoundButton, b) -> {

            lstItemsSelected.set(
                    indexCreated,
                    new _getItemsFarms(
                            indexCreated,
                            itemTab.idFarm,
                            itemTab.cb,
                            cb.isChecked(),
                            txtStatus
                    )
            );
            countSelectedFarms = 0;
            for(_getItemsFarms t : lstItemsSelected){
                if(t.selected) {
                    countSelectedFarms = countSelectedFarms + 1;
                }
            }

            //countSelectedFarms = lstItemsSelected.stream().filter(f -> f.selected).count();
            linearDownload.setVisibility(countSelectedFarms > 0 ? View.VISIBLE : View.GONE);
            txtCountFamr.setText("Cantidad de fincas seleccionadas : "+countSelectedFarms);
        });
    }

    public void _getPaintDownloadsFarm(){
        LinearLayout lineParent = new LinearLayout(this);
        lineParent.setPadding(10,10,10,10);
        lineParent.setOrientation(LinearLayout.HORIZONTAL);
        lineParent.setWeightSum(2);
        lineParent.setBackgroundColor(Color.parseColor("#393F43"));
        lineParent.setGravity(Gravity.CENTER);

        txtCountFamr.setLayoutParams(paramLine(""));
        txtCountFamr.setTextColor(Color.parseColor("#ffffff"));
        txtCountFamr.setTextDirection(Gravity.CENTER);

        Button btnDownload = new Button(this);
        btnDownload.setText("Realizar descarga");
        btnDownload.setLayoutParams(paramLine(""));
        _funDownloadFarm(btnDownload);

        lineParent.addView(txtCountFamr);
        lineParent.addView(btnDownload);

        linearDownload.addView(lineParent);
        linearDownload.setVisibility(View.GONE);
    }

    public void _funAllSelectionFarm(CheckBox cbx){
        cbx.setOnCheckedChangeListener((compoundButton, b) -> {
            for(_getItemsFarms i : lstItemsSelected){
                i.cb.setChecked(cbx.isChecked());
            }
        });
    }

    public void _funDownloadFarm(Button btn){
        btn.setOnClickListener((view) -> {
            try {
                iPlano ip = new iPlano(path);
                iFenologia iFen = new iFenologia(path);
                iCuadrosBloque iCB = new iCuadrosBloque(path);

                String ids = "";

                if(countSelectedFarms > 0) {

                    for(_getItemsFarms i : lstItemsSelected){
                        if(i.selected){
                            ids = ids.isEmpty() ? ""+i.idFarm :  ids+","+i.idFarm;
                        }
                    }

                    Log.i("FINCAS","sin corchetes : "+ ids);

                    if (iCB.local() && iFen.local() && ip.crearPlano(ids)) {
                        Toast.makeText(this, "Local actualizado exitosamente", Toast.LENGTH_LONG).show();
                    }

                    updateStateItemFarm();
                }else{
                    Toast.makeText(this, "Debes seleccionar al menos un finca para la descarga", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){
                Log.i("FINCAS",""+e);
                Toast.makeText(this, "Ocurrio un error al cargar el plano de siembra \n"+e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateStateItemFarm(){
        try {
            /*cbxSelectedAllFarm.setChecked(false);

            List<Integer> lst1 = new ArrayList<>();

            for (planoTab tab : iplano.all()) {
                String key  = tab.getFinca();

            }*/



            for(int i = 0; i < lstItemsSelected.size(); i++){
                boolean finded = false;
                for(planoTab t : iplano.all()){
                    if(t.getIdFinca() == lstItemsSelected.get(i).idFarm){

                        finded = true;
                        break;
                    }

                }

                lstItemsSelected.get(i).txtStatus.setText(finded ? "Descargada" : "Sin descargar");
                lstItemsSelected.get(i).txtStatus.setTextColor(Color.parseColor(finded ? "#52BE80" : "#F1948A"));
                lstItemsSelected.get(i).cb.setChecked(false);
            }
        }catch (Exception e){
            Toast.makeText(this, "Exception updateStateItemFarm : "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}

class _getItemsFarms{
    int index;
    int idFarm;
    CheckBox cb;
    boolean selected;
    TextView txtStatus;

    public _getItemsFarms(int index, int idFarm, CheckBox cb, boolean selected, TextView txtStatus) {
        this.index = index;
        this.idFarm = idFarm;
        this.cb = cb;
        this.selected = selected;
        this.txtStatus = txtStatus;
    }
}