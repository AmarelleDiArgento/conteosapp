package com.example.conteos_app.Model;

import com.example.conteos_app.Model.interfaz.plano;
import com.example.conteos_app.Model.tab.planoTab;

import com.example.conteos_app.Config.sqlConect;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class iPlano extends sqlConect implements plano {
    Connection cn = null;

    final String ins = "INSERT INTO plano\n" +
            "(idSiembra, idFinca, finca, idBloque, bloque, idVariedad, variedad)\n" +
            "VALUES(?, ?, ?, ?, ?, ?, ?);\n";
    final String upd = "UPDATE plano SET\\n\" +\n" +
            "idSiembra=?, idFinca=?, finca=?, idBloque=?, bloque=?, idVariedad=?, variedad=?;";
    final String del = "DELETE FROM plano\n" +
            "WHERE idSiembra=?";
    final String one = "SELECT idSiembra, idFinca, finca, idBloque, bloque, idVariedad, variedad \n" +
            "FROM plano;\n" +
            "WHERE idSiembra=?";
    final String nam = "SELECT idSiembra, idFinca, finca, idBloque, bloque, idVariedad, variedad \n" +
            "FROM plano;\n" +
            "WHERE finca=?";
    final String all = "SELECT idSiembra, idFinca, finca, idBloque, bloque, idVariedad, variedad \n" +
            "FROM plano;\n";


    public iPlano() throws Exception {
        this.cn = getConexion();
    }

    @Override
    public String insert(planoTab o) {
        String msj = "";
        try {
            PreparedStatement ps = cn.prepareStatement(ins);
            ps.setLong(1, o.getIdSiembra());
            ps.setInt(2, o.getIdFinca());
            ps.setString(3, o.getFinca());
            ps.setInt(4, o.getIdBloque());
            ps.setString(5, o.getBloque());
            ps.setInt(6, o.getidVariedad());
            ps.setString(7, o.getVariedad());
            if (ps.executeUpdate() == 0) {
                msj = "Ups, algo salio mal. No se registro la siembra #" + o.getIdSiembra();
            } else {
                msj = "Siembra #" + o.getIdSiembra() + " registrada exitosamente";
            }
            closeConexion(cn);

        } catch (Exception e) {
            msj = e.toString();
        }
        return msj;
    }

    @Override
    public String update(planoTab o) {
        String msj = "";
        try {
            PreparedStatement ps = cn.prepareStatement(upd);
            ps.setLong(1, o.getIdSiembra());
            ps.setInt(2, o.getIdFinca());
            ps.setString(3, o.getFinca());
            ps.setInt(4, o.getIdBloque());
            ps.setString(5, o.getBloque());
            ps.setInt(6, o.getidVariedad());
            ps.setString(7, o.getVariedad());
            if (ps.executeUpdate() == 0) {
                msj = "Ups, algo salio mal. No se actualizo la siembra #" + o.getIdSiembra();
            } else {
                msj = "Siembra #" + o.getIdSiembra() + " actualizada exitosamente";
            }
            closeConexion(cn);

        } catch (Exception e) {
            msj = e.toString();
        }
        return msj;
    }

    @Override
    public String delete(Long id) {
        String msj = "";
        try {
            PreparedStatement ps = cn.prepareStatement(del);
            ps.setLong(1, id);
            if (ps.executeUpdate() == 0) {
                msj = "Ups, algo salio mal. No se elimino la siembra #" + id;
            } else {
                msj = "Siembra #" + id + " eliminada exitosamente";
            }
            closeConexion(cn);

        } catch (Exception e) {
            msj = e.toString();
        }
        return msj;
    }

    private planoTab gift(ResultSet rs) throws Exception {
        planoTab p = new planoTab();
        p.setIdSiembra(rs.getLong("idSiembra"));
        p.setIdFinca(rs.getInt("idFinca"));
        p.setFinca(rs.getString("finca"));
        p.setIdBloque(rs.getInt("idbloque"));
        p.setBloque(rs.getString("bloque"));
        p.setidVariedad(rs.getInt("idVariedad"));
        p.setVariedad(rs.getString("variedad"));
        return p;
    }

    @Override
    public planoTab oneId(Long id) throws Exception {
        ResultSet rs = null;
        planoTab p = null;

        PreparedStatement ps = cn.prepareStatement(one);
        ps.setLong(1, id);

        rs = ps.executeQuery();
        if (rs != null) {
            p = gift(rs);
        }

        closeConexion(cn, rs);

        return p;

    }

    @Override
    public List<planoTab> all() throws Exception {

        List<planoTab> pl = new ArrayList<>();
        ResultSet rs;
        PreparedStatement ps = cn.prepareStatement(all);
        rs = ps.executeQuery();
        while (rs.next()) {
            pl.add(gift(rs));
        }

        closeConexion(cn, rs);
        return pl;
    }
}
