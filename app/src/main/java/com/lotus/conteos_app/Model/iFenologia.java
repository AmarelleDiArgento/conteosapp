package com.lotus.conteos_app.Model;

import com.lotus.conteos_app.Config.sqlConect;
import com.lotus.conteos_app.Model.interfaz.fenologia;
import com.lotus.conteos_app.Model.tab.fenologiaTab;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

class iFenologia extends sqlConect implements fenologia {
    Connection cn = null;



    final String ins = "INSERT INTO [dbo].[Fenologia]\n" +
            "           ([idVariedad] ,[variedad] ,[grados_dia] ,[diametro_boton] ,[largo_boton] ,[imagen])\n" +
            "     VALUES\n" +
            "           ( ?, ?, ?, ?, ?, ?, ?);";
    final String upd = "UPDATE [dbo].[Fenologia]\n" +
            "   SET [idVariedad] = ?, [variedad] = ?, ,[grados_dia] = ?, ,[diametro_boton] = ?, ,[largo_boton] = ?, ,[imagen] = ?\n" +
            "  WHERE  [idFenologia] = ?;";
    final String del = "DELETE FROM [dbo].[Fenologia]\n" +
            "  WHERE  [idFenologia] = ?;";
    final String one = "SELECT [idFenologia] ,[idVariedad] ,[variedad] ,[grados_dia] ,[diametro_boton] ,[largo_boton] ,[imagen]\n" +
            "  FROM [dbo].[Fenologia]\n" +
            "  WHERE  [idFenologia] = ?;";
    final String nam = "SELECT [idFenologia] ,[idVariedad] ,[variedad] ,[grados_dia] ,[diametro_boton] ,[largo_boton] ,[imagen]\n" +
            "  FROM [dbo].[Fenologia]\n" +
            "  WHERE  [variedad] = ?;";
    final String all = "SELECT [idFenologia] ,[idVariedad] ,[variedad] ,[grados_dia] ,[diametro_boton] ,[largo_boton] ,[imagen]\n" +
            "  FROM [dbo].[Fenologia];";


    public iFenologia() throws Exception {
        this.cn = getConexion();
    }

    @Override
    public String insert(fenologiaTab o) throws Exception {
        return null;
    }

    @Override
    public String update(fenologiaTab o) throws Exception {
        return null;
    }

    @Override
    public String delete(Long id) throws Exception {
        return null;
    }
    private fenologiaTab gift(ResultSet rs) throws Exception {
        fenologiaTab f = new fenologiaTab();
        f.setIdFenologia(rs.getLong("idFenologia"));
        f.setIdVariedad(rs.getInt("idVariedad"));
        f.setVariedad(rs.getString("variedad"));
        f.setGrados_dia(rs.getDouble("grados_dia"));
        f.setDiametro_boton(rs.getDouble("diametro_boton"));
        f.setLargo_boton(rs.getDouble("largo_boton"));
        f.setImagen(rs.getString("imagen"));
        return f;
    }
    @Override
    public fenologiaTab oneId(Long id) throws Exception {
        ResultSet rs = null;
        fenologiaTab f = null;

        PreparedStatement ps = cn.prepareStatement(one);
        ps.setLong(1, id);

        rs = ps.executeQuery();
        if (rs != null) {
            f = gift(rs);
        }

        closeConexion(cn, rs);

        return f;
    }

    @Override
    public List<fenologiaTab> all() throws Exception {

        List<fenologiaTab> fl = new ArrayList<>();
        ResultSet rs;
        PreparedStatement ps = cn.prepareStatement(all);
        rs = ps.executeQuery();
        while (rs.next()) {
            fl.add(gift(rs));
        }

        closeConexion(cn, rs);
        return fl;
    }
}
