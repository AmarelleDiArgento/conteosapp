package com.lotus.conteos_app.Config.Util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class storageFarmWorking {

    Activity act;
    SharedPreferences sp;
    List<_getWorkingFarm> lstStoragefarms = new ArrayList<>();


    public storageFarmWorking(Activity act) {
        this.act = act;
        sp = act.getBaseContext().getSharedPreferences("share", act.MODE_PRIVATE);
    }

    public void storageIdFarmWorking(int idFarm, String path, String name, boolean getData){
        try{
            if(getData) {

                SharedPreferences.Editor edit = sp.edit();
                edit.putInt("idFarmWorking", idFarm);
                edit.putString("nameFarmWorking", name);
                edit.putString("pathWorkingFarm", path);
                edit.commit();
                edit.apply();

                Toast.makeText(act, "ahora puedes trabajar con la finca", Toast.LENGTH_SHORT).show();
            }else{
                if(sp != null){
                    Log.i("workingFarm", "Se consulta la finca trabajada : "+sp.getInt("idFarmWorking", 0));
                    lstStoragefarms.add( new _getWorkingFarm(
                            sp.getInt("idFarmWorking", 0),
                            sp.getString("nameFarmWorking", ""),
                            sp.getString("pathWorkingFarm", "")
                    ));

                    setLstStoragefarms(lstStoragefarms);
                }
            }
        }catch (Exception e){
            Toast.makeText(act, "Exception SharedPreferncesFarm: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public List<_getWorkingFarm> getLstStoragefarms() {
        return lstStoragefarms;
    }

    public void setLstStoragefarms(List<_getWorkingFarm> lstStoragefarms) {
        this.lstStoragefarms = lstStoragefarms;
    }

    public class _getWorkingFarm{
        int idFarm;
        String nameFarm, path;

        public _getWorkingFarm(int idFarm, String nameFarm, String path) {
            this.idFarm = idFarm;
            this.nameFarm = nameFarm;
            this.path = path;
        }

        public int getIdFarm() {
            return idFarm;
        }

        public void setIdFarm(int idFarm) {
            this.idFarm = idFarm;
        }

        public String getNameFarm() {
            return nameFarm;
        }

        public void setNameFarm(String nameFarm) {
            this.nameFarm = nameFarm;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}


