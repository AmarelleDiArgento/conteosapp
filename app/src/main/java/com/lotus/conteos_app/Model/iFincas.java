package com.lotus.conteos_app.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lotus.conteos_app.Config.Util.jsonAdmin;
import com.lotus.conteos_app.Config.sqlConect;
import com.lotus.conteos_app.Model.tab.fincasTab;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class iFincas extends sqlConect {

    Connection cn;
    String path;

    String query = "SELECT  idFinca, finca\n" +
                    "FROM   Plano_Siembra\n" +
                    "GROUP BY   idFinca, finca\n"+
                    "ORDER BY finca ASC";

    List<fincasTab> Lf = new ArrayList<>();

    String nombre = "finca";

    public iFincas(String path) {
        this.path = path;
        this.cn = getConexion();
    }

    public boolean local() throws Exception{
        if(cn != null){
            ResultSet rs;
            PreparedStatement ps = cn.prepareStatement(query);
            rs = ps.executeQuery();

            while(rs.next()){
                Lf.add(new fincasTab(rs.getString("finca"), rs.getInt("idFinca")));
            }

            String c = new Gson().toJson(Lf);
            return new jsonAdmin().CrearArchivo(path, nombre, c);
        }else{
            return false;
        }
    }

    public List<fincasTab> all() throws Exception{
        return Lf = new Gson().fromJson(new jsonAdmin().ObtenerLista(path, nombre),
                                        new TypeToken<List<fincasTab>>(){
                                        }.getType());
    }
}
