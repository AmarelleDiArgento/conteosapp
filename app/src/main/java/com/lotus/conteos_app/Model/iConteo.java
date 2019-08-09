package com.lotus.conteos_app.Model;

import com.lotus.conteos_app.Config.sqlConect;
import com.lotus.conteos_app.Model.interfaz.conteo;
import com.lotus.conteos_app.Model.tab.conteoTab;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;


public class iConteo extends sqlConect implements conteo  {

    Connection cn = null;

    final String ins = "INSERT INTO Conteos (fecha, idSiembra, cuadro, conteo1, conteo2, conteo3, conteo4, idUsuario)\n" +
            "     VALUES (?,?,?,?,?,?,?,?)";

    public iConteo() throws Exception {
        this.cn = getConexion();
    }

    @Override
    public String insert(conteoTab o) {
        String msj = "";
        try {
            PreparedStatement ps = cn.prepareStatement(ins);
            ps.setDate(1, (Date) o.getFecha());
            ps.setLong(2, o.getIdSiembra());
            ps.setInt(3, o.getCuadro());
            ps.setInt(4, o.getConteo1());
            ps.setInt(5, o.getConteo2());
            ps.setInt(6, o.getConteo3());
            ps.setInt(7, o.getConteo4());
            ps.setInt(8, o.getIdUsuario());
            if (ps.executeUpdate() == 0) {
                msj = "Ups, algo salio mal. No se registro conteo de la siembra #" + o.getIdSiembra();
            } else {
                msj = "Conteo de la siembra #" + o.getIdSiembra() + " registrada exitosamente";
            }
            closeConexion(cn);

        } catch (Exception e) {
            msj = e.toString();
        }
        return msj;
    }

    @Override
    public String update(conteoTab o) {
        return null;
    }

    @Override
    public String delete(Long id) {
        return null;
    }

    @Override
    public conteoTab oneId(Long id) throws Exception {
        return null;
    }

    @Override
    public List<conteoTab> all() throws Exception {
        return null;
    }
}
