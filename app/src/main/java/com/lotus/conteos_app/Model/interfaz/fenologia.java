package com.lotus.conteos_app.Model.interfaz;

import com.lotus.conteos_app.Config.DAO;
import com.lotus.conteos_app.Model.tab.fenologiaTab;

import java.util.List;

public interface fenologia extends DAO<Long, fenologiaTab, String> {

    List<fenologiaTab> forGrado(int dia, float grado, long variedad) throws Exception;
    boolean local(int idFinca) throws Exception;
}
