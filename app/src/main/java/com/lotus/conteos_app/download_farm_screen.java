 package com.lotus.conteos_app;

 import android.app.Activity;
 import android.content.SharedPreferences;
 import android.graphics.Color;
 import android.os.Build;
 import android.os.Bundle;
 import android.support.annotation.RequiresApi;
 import android.support.v7.app.AppCompatActivity;
 import android.util.Log;
 import android.view.Gravity;
 import android.view.View;
 import android.widget.Button;
 import android.widget.CheckBox;
 import android.widget.LinearLayout;
 import android.widget.ScrollView;
 import android.widget.TextView;
 import android.widget.Toast;

 import com.lotus.conteos_app.Config.Util.storageFarmWorking;
 import com.lotus.conteos_app.Config.Util.tasks.downloadFarms;
 import com.lotus.conteos_app.Model.iCuadrosBloque;
 import com.lotus.conteos_app.Model.iFenologia;
 import com.lotus.conteos_app.Model.iFincas;
 import com.lotus.conteos_app.Model.iPlano;
 import com.lotus.conteos_app.Model.tab._getItemsFarms;
 import com.lotus.conteos_app.Model.tab.fincasTab;
 import com.lotus.conteos_app.Model.tab.planoTab;

 import java.io.File;
 import java.util.ArrayList;
 import java.util.List;

 @RequiresApi(api = Build.VERSION_CODES.N)
 public class download_farm_screen extends AppCompatActivity {

    ScrollView scrollView;
    CheckBox cbxSelectedAllFarm;
    LinearLayout linearDownload;
    TextView txtCountFamr, txtCantidadGeneral, txtStatusC_B;

    iFincas ifincas = null;
    iPlano iplano = null;

    List<_getItemsFarms> lstItemsSelected = new ArrayList<>();
    List<Integer> lstFarmDownloader = new ArrayList<>();

    String path;
    int idFincaWork;
    String nameFarmWorking;
    long countSelectedFarms = 0;
    android.app.Dialog  dialogProgressD;
    SharedPreferences sp;



     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.activity_download_farm_screen);
            getSupportActionBar().hide();

            path = getExternalFilesDir(null) + File.separator;
            sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);

            insEntity();
            insView();
            getListAllFamrItems();
            _getPaintDownloadsFarm();

            new storageFarmWorking(this).storageIdFarmWorking(0, "", "", false);
            getFarmWorking();
            new downloadFarms(this, path, lstItemsSelected, txtStatusC_B, idFincaWork);

            Log.i("workingFarm", "idFinca para trabajar : "+idFincaWork);


        }catch (Exception e){
            Toast.makeText(this, "Exception download : "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

     public void getFarmWorking(){
         try {
             storageFarmWorking sfw = new storageFarmWorking(this);
             sfw.storageIdFarmWorking(0, "", "", false);

             for (int i = 0; i < sfw.getLstStoragefarms().size(); i++) {

                 Log.i("workingFarm", "Encontro : "+sfw.getLstStoragefarms().get(i).getNameFarm());

                 idFincaWork = sfw.getLstStoragefarms().get(i).getIdFarm();
                 nameFarmWorking = sfw.getLstStoragefarms().get(i).getNameFarm();
             }
         }catch (Exception e){
             Log.i("workingFarm", "Error : "+e.toString());
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
            dialogProgressD = new android.app.Dialog(this);
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

            lineItem.addView(_getItemCuadrosBloque());

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

    public View _getItemCuadrosBloque(){
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
        txtNameFarm.setText("Cuadros bloque y fenologias");


        txtStatusC_B = new TextView(this);
        txtStatusC_B.setText("status");

        lineNameStatus.addView(txtNameFarm);
        lineNameStatus.addView(txtStatusC_B);

        lineParent.addView(lineNameStatus);

        return lineParent;
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

        Button btn = new Button(this);
        btn.setText("Trabajar con la finca");
        btn.setOnClickListener((View v) -> {

            new storageFarmWorking(this).storageIdFarmWorking(
                    f.getId(),
                    path+"plano_"+f.getId()+".json",
                    f.getNombre(),
                    true
            );

            getFarmWorking();

            new downloadFarms(this, path,  lstItemsSelected,  txtStatusC_B, idFincaWork);
        });


        lstItemsSelected.add(new _getItemsFarms(indexCreated, f.getId(), f.getNombre(), cb, false, idFincaWork == f.getId(),  txtStatusFarm, btn));
        _funCheckSelection(indexCreated, cb, new _getItemsFarms(indexCreated, f.getId(), f.getNombre(), cb, false, idFincaWork == f.getId(), txtStatusFarm, btn), txtStatusFarm);

        lineWorkCheck.addView(btn);
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
                            itemTab.getIdFarm(),
                            itemTab.getNameFarm(),
                            itemTab.getCb(),
                            cb.isChecked(),
                            itemTab.getIdFarm() == idFincaWork,
                            txtStatus,
                            itemTab.getBtnWorkingFarm()
                    )
            );
            countSelectedFarms = 0;
            for(_getItemsFarms t : lstItemsSelected){
                if(t.isSelected()) {
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
                i.getCb().setChecked(cbx.isChecked());
            }
        });
    }

    public void _funDownloadFarm(Button btn){
        btn.setOnClickListener((view) -> {
            try {

                if (countSelectedFarms > 0) {

                    new downloadFarms(this, path, new iFenologia(path), new iCuadrosBloque(path), txtStatusC_B);

                    new downloadFarms(this, path, new iPlano(path), lstFarmDownloader, lstItemsSelected);

                } else {
                    Log.i("theadDownload", "Debes seleccionar al menos un finca para la descarga");
                }

            }catch (Exception e){
                Log.i("FINCAS",""+e);
                Toast.makeText(this, "Ocurrio un error al cargar el plano de siembra \n"+e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}




