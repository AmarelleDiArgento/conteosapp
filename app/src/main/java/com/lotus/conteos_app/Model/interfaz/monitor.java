package com.lotus.conteos_app.Model.interfaz;

import com.lotus.conteos_app.Config.DAO;
import com.lotus.conteos_app.Model.tab.monitorTab;

public interface monitor extends DAO<Long, monitorTab, String> {
    monitorTab login(String user, String pass);
}
