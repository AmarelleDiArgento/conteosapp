package com.lotus.conteos_app.Model.interfaz;

import com.lotus.conteos_app.Config.DAO;
import com.lotus.conteos_app.Model.tab.planoTab;

import java.util.List;

public interface plano extends DAO <Long, planoTab,String> {

    planoTab OneforIdSiembra(long idSiembra) throws Exception;
}
