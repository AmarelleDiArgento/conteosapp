package com.lotus.conteos_app.Model.tab;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class _getItemsFarms{
    int index;
    int idFarm;
    String nameFarm;
    CheckBox cb;
    boolean selected;
    boolean working;
    TextView txtStatus;
    Button btnWorkingFarm;

    public _getItemsFarms(int index, int idFarm, String nameFarm, CheckBox cb, boolean selected, boolean working, TextView txtStatus, Button btnWorkingFarm) {
        this.index = index;
        this.idFarm = idFarm;
        this.nameFarm = nameFarm;
        this.cb = cb;
        this.selected = selected;
        this.txtStatus = txtStatus;
        this.btnWorkingFarm = btnWorkingFarm;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    public CheckBox getCb() {
        return cb;
    }

    public void setCb(CheckBox cb) {
        this.cb = cb;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public TextView getTxtStatus() {
        return txtStatus;
    }

    public void setTxtStatus(TextView txtStatus) {
        this.txtStatus = txtStatus;
    }

    public boolean getWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public Button getBtnWorkingFarm() {
        return btnWorkingFarm;
    }

    public void setBtnWorkingFarm(Button btnWorkingFarm) {
        this.btnWorkingFarm = btnWorkingFarm;
    }
}
