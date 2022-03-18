package com.lotus.conteos_app.Config.Util.tasks;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.lotus.conteos_app.Model.iCuadrosBloque;
import com.lotus.conteos_app.Model.iFenologia;
import com.lotus.conteos_app.Model.iPlano;
import com.lotus.conteos_app.Model.tab._getItemsFarms;

import java.io.File;
import java.util.List;

public class downloadFarms {
    //query
    //entidad finca

    Activity act;
    List<Integer> lstDownloads;
    List<_getItemsFarms> lstItemsFarms;
    String path;
    int  idWorkingFarm;
    TextView txtStatusC_B;

    iPlano ip;
    iFenologia iF;
    iCuadrosBloque icb;

    public downloadFarms(Activity act, String path, List<_getItemsFarms> lstItemsFarms, TextView txtStatusC_B, int idWorkingFarm){
        this.act = act;
        this.path = path;
        this.lstItemsFarms = lstItemsFarms;
        this.txtStatusC_B = txtStatusC_B;
        this.idWorkingFarm = idWorkingFarm;

        new getDataFarmsFiles(act, path, lstItemsFarms, txtStatusC_B, idWorkingFarm).start();
    }

    public downloadFarms(Activity act, String path,  iPlano ip, List<Integer> lstDownloads, List<_getItemsFarms> lstItemsFarms) {
        this.act = act;
        this.path = path;
        this.ip = ip;
        this.lstDownloads = lstDownloads;

        for (_getItemsFarms i : lstItemsFarms) {
            if (i.isSelected()) {
                Log.i("taskDownloader", "Intentando descargar la finca : "+i.getIdFarm());
                i.getTxtStatus().setText("Descargando finca...");
                i.getTxtStatus().setTextColor(Color.parseColor("#5DADE2"));
                new getDataFarms(i.getIdFarm(), lstItemsFarms).start();
            }
        }
    }

    public downloadFarms(Activity act, String path, iFenologia iF, iCuadrosBloque icb, TextView txtStatusC_B){
        this.act = act;
        this.path = path;
        this.iF = iF;
        this.icb = icb;
        this.txtStatusC_B = txtStatusC_B;

        txtStatusC_B.setText("Intentando descar datos de cuadros bloque y fenologias...");
        txtStatusC_B.setTextColor(Color.parseColor("#5DADE2"));

        new getC_B_feno(txtStatusC_B).start();
    }

    //DOWNLOAD C/B & FENOLOGIES
    public class getC_B_feno extends Thread{

        TextView txtStatus;

        public getC_B_feno(TextView txtStatus){
            this.txtStatus = txtStatus;
        }

        @Override
        public void run(){
            super.run();

            act.runOnUiThread(() -> {
                Log.i("taskDownloader", "llego al metodo de cuadros bloques");
            });

            downloadsFen();
        }


        public void downloadsFen(){
            try {
                if (iF.local() && icb.local()) {
                    act.runOnUiThread(() -> {
                        Log.i("taskDownloader", "descargo cuadros bloque con exito");
                        this.txtStatus.setText("Datos descargados con exito");
                        this.txtStatus.setTextColor(Color.parseColor("#58D68D"));
                    });
                }
            }catch (Exception e){
                act.runOnUiThread(() -> {
                    Log.i("taskDownloader", "Error al descargar cuadros bloque : "+e.toString());
                    txtStatus.setText("Ocurrio un error al descargar los datos");
                    txtStatus.setTextColor(Color.parseColor("#CD6155"));
                });
            }

        }
    }

    //DOWNLOADS FARMS
    public class getDataFarms extends Thread {

        int idFarm;
        List<_getItemsFarms> lstItemsFarms;

        public getDataFarms(int idFarm, List<_getItemsFarms> lstItemsFarms){
         this.idFarm = idFarm;
         this.lstItemsFarms = lstItemsFarms;
        }

        @Override
        public void run() {
            super.run();
            downloaderTaks();
        }


        public void downloaderTaks(){
            try {
                if(ip.crearPlano(this.idFarm + "")){
                    act.runOnUiThread(() -> {
                        lstDownloads.add(0);
                        for(_getItemsFarms i : lstItemsFarms){
                            if(i.getIdFarm() == this.idFarm){
                                Log.i("taskDownloader", "se descargo la finca : "+idFarm);
                                i.getTxtStatus().setText("finca descargada");
                                i.getTxtStatus().setTextColor(Color.parseColor("#58D68D"));
                                i.getCb().setChecked(false);
                                i.getBtnWorkingFarm().setEnabled(true);
                                i.getBtnWorkingFarm().setText("trabajar con la finca");
                                break;
                            }
                        }
                    });
                }
            }catch (Exception e){
                act.runOnUiThread(() -> {
                    for(_getItemsFarms i : lstItemsFarms){
                        if(i.getIdFarm() == this.idFarm){
                            i.getTxtStatus().setText("Ocurrio un error al descargar los datos");
                            i.getTxtStatus().setTextColor(Color.parseColor("#CD6155"));
                            i.getCb().setChecked(false);
                            i.getBtnWorkingFarm().setEnabled(false);
                            i.getBtnWorkingFarm().setText("Primero realiza la descarga");
                            break;
                        }
                    }
                });
            }
        }
    }

    //VERIFY FARMS DOWNLOASD

    public class getDataFarmsFiles extends Thread {

        String path;
        int idFarmWorking;
        Activity act;
        List<_getItemsFarms> lstItemsFarms;
        TextView txtStatusC_B;

        public getDataFarmsFiles(Activity act, String path, List<_getItemsFarms> lstItemsFarms, TextView txtStatusC_B, int idFarmWorking)  {
            this.act = act;
            this.path = path;
            this.lstItemsFarms = lstItemsFarms;
            this.txtStatusC_B = txtStatusC_B;
            this.idFarmWorking = idFarmWorking;
        }

        @Override
        public void run(){
            verifyFiles();
        }

        public void verifyFiles(){

            //VERIFICAR DATOS CUADROS BLOQUE Y FENOLOGIAS

            File fileCB = new File(path+"/cuadros_bloque.json");
            File fileFeno = new File(path+"/fenologias.json");

            boolean files_exist = fileCB.exists() && fileFeno.exists();

            act.runOnUiThread(() -> {
                txtStatusC_B.setText(files_exist ? "Datos descargados con exito" : "Datos pendientes por descargar");
                txtStatusC_B.setTextColor(Color.parseColor(files_exist ? "#58D68D" : "#85929E"));
            });



            // VERIFICAR DATOS PLANO DE SIEMBRA POR FINCA

            for(_getItemsFarms i : lstItemsFarms){

                File f = new File(path+"/plano_"+i.getIdFarm()+".json");

                boolean fileExist = f.exists();

                act.runOnUiThread(() -> {
                    i.getTxtStatus().setText(fileExist ? "finca descargada" : "finca sin descargar");
                    i.getTxtStatus().setText(i.getIdFarm() == idFarmWorking ? "Trabajando con la finca" : i.getTxtStatus().getText());
                    i.getTxtStatus().setTextColor(Color.parseColor(fileExist ? "#58D68D" : "#85929E"));
                    i.getCb().setChecked(false);
                    i.getBtnWorkingFarm().setEnabled(fileExist);
                    i.getBtnWorkingFarm().setText(fileExist ? "trabajar con la finca" : "Primero realiza la descarga");
                });
            }
        }
    }
}

