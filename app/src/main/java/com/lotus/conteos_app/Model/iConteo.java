package com.lotus.conteos_app.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lotus.conteos_app.Config.Util.jsonAdmin;
import com.lotus.conteos_app.Config.sqlConect;
import com.lotus.conteos_app.Model.interfaz.conteo;
import com.lotus.conteos_app.Model.tab.conteoTab;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class iConteo extends sqlConect implements conteo {

    public String nombre;
    final String ins = "INSERT INTO Conteos (fecha, idSiembra, cuadro, conteo1, conteo2, conteo3, conteo4, idUsuario)\n" +
            "     VALUES (?,?,?,?,?,?,?,?)";

    Connection cn = null;
    String path = null;
    jsonAdmin ja = null;

    private List<conteoTab> cl = new ArrayList<>();

    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");


    public iConteo(String path) throws Exception {
        this.cn = getConexion();
        getPath(path);
    }

    public void getPath(String path) {
        ja = new jsonAdmin();
        this.path = path;
    }


    @Override
    public String insert(conteoTab c) {

        try {
            if(cl.isEmpty()){
                c.setIdConteo(1);
            }else{
                c.setIdConteo(cl.size() + 1);
            }
            // all();
            cl.add(c);
            local();

            return "Registro realizado exitosamente";
        } catch (Exception e) {
            return "Error: " + e.toString();
        }
    }

    @Override
    public String update(conteoTab c) {

        try {
            int id = (int) c.getIdConteo();
            cl.set(id, c);
            // nombre = sdf.format(c.getFecha());
            local();

            return "actualizado conteo de la cama: " + c.getCuadro();
        } catch (Exception e) {
            return "Error: " + e.toString();
        }
    }

    @Override
    public String delete(Long id) {
        return null;
    }

    @Override
    public conteoTab oneId(Long id) throws Exception {
        conteoTab cr = new conteoTab();

        for (conteoTab c : cl) {
            if (c.getIdConteo() == id) {
                cr = c;
            }
        }

        return cr;
    }

    @Override
    public boolean local() throws Exception {
        String contenido = cl.toString();
        return ja.CrearArchivo(path, nombre, contenido);
    }

    @Override
    public List<conteoTab> all() throws Exception {

        Gson gson = new Gson();
        cl = gson.fromJson(ja.ObtenerLista(path, nombre), new TypeToken<List<conteoTab>>() {
        }.getType());

        return cl;
    }


    @Override
    public String send(List<conteoTab> ls) {

        String msj = "";
        for (conteoTab c : cl) {
            msj = msj + record(c) + "\n";
        }
        return msj;
    }

    public String record(conteoTab o) {
        String msj = "";
        try {
            PreparedStatement ps = cn.prepareStatement(ins);
            ps.setString(1, o.getFecha());
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
}
