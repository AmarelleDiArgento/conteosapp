package com.lotus.conteos_app.Model.interfaz;

import com.lotus.conteos_app.Config.DAO;
import com.lotus.conteos_app.Model.tab.conteoTab;

import java.util.List;

public interface conteo extends DAO <Long, conteoTab, String>{
    String send(List<conteoTab> ls);
}
