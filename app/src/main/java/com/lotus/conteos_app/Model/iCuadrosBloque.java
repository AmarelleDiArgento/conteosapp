package com.lotus.conteos_app.Model;

import android.util.Log;
import android.widget.Toast;

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

  String query = "SELECT c.id_bloque, c.id_variedad, c.tot_cuadros, c.id_fenologia, p.idFinca\n" +
          "FROM Cuadros_Bloque AS c INNER JOIN\n" +
          "Plano_Siembra AS p ON c.id_bloque = p.idBloque AND c.id_variedad = p.idVariedad";

  public iCuadrosBloque(String path){
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
    try {
      this.cn = getConexion();
      ResultSet rs;
      PreparedStatement ps = cn.prepareStatement(query);
      rs = ps.executeQuery();

      while (rs.next()) {
        cb.add(gift(rs));
      }

      closeConexion(cn, rs);

      String contenido = cb.toString();

      return ja.CrearArchivo(path, nombre, contenido);
    }catch (Exception e){
      Log.i("descarga", ""+e.toString());
      return false;
    }
  }

  @Override
  public List<cuadros_bloqueTab> all() throws Exception {
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
    cbt.setIdFinca(rs.getLong("idFinca"));
    return cbt;
  }

  public cuadros_bloqueTab cuadroyvariedad(long idBloque, long idVariedad) throws Exception{
    cuadros_bloqueTab cbt = new cuadros_bloqueTab();

    Log.i("SIEMBRA", "Entro : "+idBloque+"  "+idVariedad);

      for(cuadros_bloqueTab c : all()){
        if(c.getIdBloque() == idBloque && c.getIdVariedad() == idVariedad) {
          cbt = c;
          Log.i("SIEMBRA","encontro en cuadros bloque : desde cuadro Bloque --> "+c.getIdBloque()+ ", Bloque llegada : "+ idBloque+" y desde cuadro Bloque variedad : -->"+c.getIdVariedad()+ ", Variedad llegada : "+ idVariedad);
          break;
        }else{
          Log.i("SIEMBRA","not cuadros bloque desde cuadro Bloque --> "+c.getIdBloque()+ ", Bloque llegada : "+ idBloque+" y desde cuadro Bloque variedad : -->"+c.getIdVariedad()+ ", Variedad llegada : "+ idVariedad);
        }
      }
    return cbt;
  }
}
