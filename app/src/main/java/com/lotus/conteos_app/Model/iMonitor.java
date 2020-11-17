package com.lotus.conteos_app.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lotus.conteos_app.Config.Util.jsonAdmin;
import com.lotus.conteos_app.Config.sqlConect;
import com.lotus.conteos_app.Model.interfaz.monitor;
import com.lotus.conteos_app.Model.tab.conteoTab;
import com.lotus.conteos_app.Model.tab.monitorTab;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class iMonitor extends sqlConect implements monitor {
    private List<monitorTab> ml = new ArrayList<>();

    Connection cn = null;
    String path = null;
    jsonAdmin ja = null;

    String nombre = "monitores";

    final String ins = "USE [Proyecciones]\n" +
            "GO\n" +
            "\n" +
            "SELECT [idMonitor]\n" +
            "      ,[codigo]\n" +
            "      ,[nombres]\n" +
            "      ,[apellidos]\n" +
            "      ,[password]\n" +
            "      ,[idFinca]\n" +
            "      ,[estado]\n" +
            "  FROM [dbo].[Monitor]\n" +
            "GO\n";
    final String upd = "";
    final String del = "";
    final String one = "";
    final String nam = "";
    final String all = "SELECT TOP (1000) [idMonitor]\n" +
            "      ,[codigo]\n" +
            "      ,[nombres]\n" +
            "      ,[apellidos]\n" +
            "      ,[password]\n" +
            "      ,[idFinca]\n" +
            "      ,[estado]\n" +
            "  FROM [Proyecciones].[dbo].[Monitor]";

    public iMonitor(String path) throws Exception {
        getPath(path);
    }

    public void getPath(String path) {
        ja = new jsonAdmin();
        this.path = path;
    }

    @Override
    public monitorTab login(String user, String pass) {

        monitorTab mret = null;

        for (monitorTab m : ml) {
            if (m.getCodigo().equals(user) && m.getPassword().equals(pass)) {
                mret = m;
                break;
            }
        }
        return mret;
    }

    @Override
    public String insert(monitorTab o) throws Exception {
        return null;
    }

    @Override
    public String update(Long id,monitorTab o) throws Exception {
        return null;
    }

    @Override
    public String delete(Long id) throws Exception {
        return null;
    }

    private monitorTab gift(ResultSet rs) throws Exception {
        monitorTab m = new monitorTab();
        m.setIdMonitor(rs.getLong("idMonitor"));
        m.setCodigo(rs.getString("codigo"));
        m.setNombres(rs.getString("nombres"));
        m.setApellidos(rs.getString("apellidos"));
        m.setPassword(rs.getString("password"));
        m.setIdFinca(rs.getInt("idFinca"));
        m.setEstado(rs.getBoolean("estado"));
        return m;
    }

    @Override
    public monitorTab oneId(Long id) throws Exception {

        for (monitorTab m : ml) {
            if (m.getIdMonitor() == id) {
                return m;
            }
        }
        return null;
    }

    @Override
    public boolean local() throws Exception {
        this.cn = getConexion();
        if (cn != null) {
            List<monitorTab> po = new ArrayList<>();

            ResultSet rs;
            PreparedStatement ps = cn.prepareStatement(all);
            rs = ps.executeQuery();
            while (rs.next()) {
                po.add(gift(rs));
            }

            closeConexion(cn, rs);

            String contenido = po.toString();

            return ja.CrearArchivo(path, nombre, contenido);
        }
        return false;
    }

    @Override
    public List<monitorTab> all() throws Exception {
        Gson gson = new Gson();
        return ml = gson.fromJson(ja.ObtenerLista(path, nombre), new TypeToken<List<monitorTab>>() {
        }.getType());
    }
}
