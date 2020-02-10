package com.lotus.conteos_app.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lotus.conteos_app.Config.DAO;
import com.lotus.conteos_app.Config.Util.jsonAdmin;
import com.lotus.conteos_app.Config.sqlConect;
import com.lotus.conteos_app.Model.tab.cuadros_bloqueTab;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class iCuadrosBloque extends sqlConect implements DAO {

  public String nombre = "cuadros_bloque";

  Connection cn = null;
  String path = null;
  jsonAdmin ja = null;

  String sel = "SELECT [id_bloque]\n" +
          "      ,[id_variedad]\n" +
          "      ,[tot_cuadros]\n" +
          "      ,[id_fenologia]\n" +
          "  FROM [dbo].[Cuadros_Bloque]";

  public iCuadrosBloque(String path){
    this.cn = getConexion();
    getPath(path);
  }

  public void getPath(String path){
    ja = new jsonAdmin();
    this.path = path;
  }

  private List <cuadros_bloqueTab> cb = new ArrayList<>();

  @Override
  public String insert(Object o) throws Exception {
    return null;
  }

  @Override
  public String update(Object id, Object o) throws Exception {
    return null;
  }

  @Override
  public String delete(Object id) throws Exception {
    return null;
  }

  @Override
  public Object oneId(Object id) throws Exception {
    return null;
  }

  @Override
  public boolean local() throws Exception {
    ResultSet rs;
    PreparedStatement ps = cn.prepareStatement(sel);
    rs = ps.executeQuery();

    while (rs.next()){
      cb.add(gift(rs));
    }

    closeConexion(cn,rs);

    String contenido = cb.toString();

    return ja.CrearArchivo(path, nombre, contenido);
  }

  @Override
  public List all() throws Exception {
    Gson gson = new Gson();
    cb = gson.fromJson(ja.ObtenerLista(path, nombre), new TypeToken<List<cuadros_bloqueTab>>(){
    }.getType());
    return cb;
  }

  private cuadros_bloqueTab gift(ResultSet rs) throws Exception{
    cuadros_bloqueTab cbt = new cuadros_bloqueTab();
    cbt.setIdBloque(rs.getLong("id_bloque"));
    cbt.setIdVariedad(rs.getLong("id_variedad"));
    cbt.setNumeroCuadros(rs.getInt("tot_cuadros"));
    cbt.setIdFenologia(rs.getLong("id_fenologia"));
    return cbt;
  }

  public cuadros_bloqueTab cuadroyvariedad(long idBloque, long idVariedad){
    cuadros_bloqueTab cbt = new cuadros_bloqueTab();

    if(idBloque != 0 && idVariedad != 0 && cb.size() > 0){
      for(cuadros_bloqueTab c : cb){
        if(c.getIdBloque() == idBloque && c.getIdVariedad() == idVariedad) {
          cbt = c;
        }
      }
    }
    return cbt;
  }
}
