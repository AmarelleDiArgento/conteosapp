package com.lotus.conteos_app.Model;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lotus.conteos_app.Config.Util.Dialog;
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
    final String ins = "INSERT INTO Conteos (fecha, idSiembra, cuadro, conteo1, conteo2, conteo3, conteo4, total, idUsuario)\n" +
            "     VALUES (?,?,?,?,?,?,?,?,?)";

    Connection cn = null;
    String path = null;
    jsonAdmin ja = null;
    Context context;


    private List<conteoTab> cl = new ArrayList<>();


    public iConteo(String path, Context context) throws Exception {
        this.cn = getConexion();
        this.context = context;
        getPath(path);
    }

    public void getPath(String path) {
        ja = new jsonAdmin();
        this.path = path;
    }
    
    @Override
    public String insert(conteoTab c) {

        try {
            if(cl.size()==0){
                c.setIdConteo(cl.size());
                cl.add(c);
                local();
            }else {
                c.setIdConteo(cl.size());
                cl.add(c);
                local();
            }

            return "Registro realizado exitosamente";
        } catch (Exception e) {
            return "Error: " + e.toString();
        }
    }

    @Override
    public String update(Long id,conteoTab c) {
        try {
            all();
            int idparseo = id.intValue();
            cl.set(idparseo, c);
            local();
            return "se actualizo correctamente";
        } catch (Exception e) {
            return "Error: " + e.toString();
        }
    }

    public String updateEstado(Long id,conteoTab c) {

        try {
            all();
            int idparseo = id.intValue();
            c.setEstado(1);
            cl.set(idparseo, c);
            local();
            return "se actualizo correctamente";
        } catch (Exception e) {
            return "Error: " + e.toString();
        }
    }

    @Override
    public String delete(Long id) {
        String msj = "";
        try {
            all();
            int idparseo = id.intValue();
            cl.remove(idparseo);
            local();
            msj="Se elimino exitosamente";
            return msj;
        }catch (Exception ex){
            msj="Exception en delete iConteo \n"+ex;
            return msj;
        }

    }

    @Override
    public conteoTab oneId(Long id) throws Exception {
        conteoTab cr = new conteoTab();
        cl = all();
        for (conteoTab c : cl) {
            if (c.getIdConteo() == id) {
                cr = c;
            }
        }

        return cr;
    }

    @Override
    public boolean local() {
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
        return msj;
    }

    public void batch(String fechaBusqueda) throws Exception {
        if(cn != null) {
            PreparedStatement ps = cn.prepareStatement(ins);
            nombre = fechaBusqueda;

            int cant = 0;
            for (conteoTab c : all()) {
                if (c.getEstado() == 0) {
                    ps.setString(1, c.getFecha());
                    ps.setLong(2, c.getIdSiembra());
                    ps.setInt(3, c.getCuadro());
                    ps.setInt(4, c.getConteo1());
                    ps.setInt(5, c.getConteo2());
                    ps.setInt(6, c.getConteo3());
                    ps.setInt(7, c.getConteo4());
                    ps.setInt(8, c.getTotal());
                    ps.setLong(9, c.getIdUsuario());
                    ps.addBatch();

                    updateEstado((long) cant, c);
                    cant ++;
                }
            }
            if(cant > 0){
                ps.executeBatch();
            }else{
                Toast.makeText(context, "No existen registros por enviar el día de hoy", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(context, "No hay conexion", Toast.LENGTH_SHORT).show();
        }
    }
}
