package com.lotus.conteos_app.Model.interfaz;

import com.lotus.conteos_app.Config.DAO;
import com.lotus.conteos_app.Model.tab.conteoTab;
import com.lotus.conteos_app.Model.tab.fenologiaTab;

import java.util.List;

public interface cuadrosbloque extends DAO <Long, conteoTab, String> {
  List<fenologiaTab> forGrado(int dia, float grado, long variedad) throws Exception;
}
