package com.lotus.conteos_app.Model;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lotus.conteos_app.Config.Util.jsonAdmin;
import com.lotus.conteos_app.Config.sqlConect;
import com.lotus.conteos_app.Model.interfaz.fenologia;
import com.lotus.conteos_app.Model.tab.fenologiaTab;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class iFenologia extends sqlConect implements fenologia {


    public List<fenologiaTab> fl = new ArrayList<>();
    Connection cn = null;
    String path = null;
    jsonAdmin ja = null;

    final String nombre = "fenologias";

    final String one = "SELECT [idFenologia] ,[idVariedad] ,[variedad] ,[grados_dia] ,[diametro_boton] ,[largo_boton] ,[imagen]\n" +
            "  FROM [dbo].[Fenologia]\n" +
            "  WHERE  [idFenologia] = ?;";

    final String allfin = "SELECT [idFenologia_gd]\n" +
            "      ,[idFenologia]\n" +
            "      ,[grados_dia]\n" +
            "      ,[diametro_boton]\n" +
            "      ,[largo_boton]\n" +
            "      ,[imagen]\n" +
            "  FROM [Proyecciones].[dbo].[Fenologia]\n" +
            "  where idFenologia in(\n" +
            "SELECT distinct [id_fenologia]\n" +
            "  FROM [Proyecciones].[dbo].[Cuadros_Bloque])" +
            "ORDER BY IdFenologia, grados_dia";


    public iFenologia(String path) throws Exception {
        getPath(path);
    }

    public void getPath(String path) {
        ja = new jsonAdmin();
        this.path = path;
    }

    @Override
    public String insert(fenologiaTab o) throws Exception {
        return null;
    }

    @Override
    public String update(Long id,fenologiaTab o) throws Exception {
        return null;
    }

    @Override
    public String delete(Long id) throws Exception {
        return null;
    }

    private fenologiaTab gift(ResultSet rs) throws Exception {
        fenologiaTab f = new fenologiaTab();
        f.setIdFenologia_gd(rs.getLong("idFenologia_gd"));
        f.setIdFenologia(rs.getLong("idFenologia"));
        f.setGrados_dia(rs.getDouble("grados_dia"));
        f.setDiametro_boton(rs.getDouble("diametro_boton"));
        f.setLargo_boton(rs.getDouble("largo_boton"));
        f.setImagen(rs.getString("imagen")+"");
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
    public boolean local() throws Exception {
        this.cn = getConexion();
        ResultSet rs;
        PreparedStatement ps = cn.prepareStatement(allfin);
        rs = ps.executeQuery();
        while (rs.next()) {
            fl.add(gift(rs));
            Log.i("fenoloadescargada", ""+rs.getString("idFenologia"));
        }

        closeConexion(cn, rs);

        String contenido = fl.toString();

        return ja.CrearArchivo(path, nombre, contenido);
    }

    @Override
    public boolean local(int idFinca) throws Exception {
        this.cn = getConexion();
        ResultSet rs;
        PreparedStatement ps = cn.prepareStatement(allfin);
        ps.setInt(1, idFinca);
        rs = ps.executeQuery();
        while (rs.next()) {
            fl.add(gift(rs));
        }

        closeConexion(cn, rs);

        String contenido = fl.toString();

        return ja.CrearArchivo(path, nombre, contenido);
    }

    @Override
    public List<fenologiaTab> all() throws Exception {

        Gson gson = new Gson();
        fl = gson.fromJson(ja.ObtenerLista(path, nombre), new TypeToken<List<fenologiaTab>>() {
        }.getType());

        return fl;
    }


    @Override
    public List<fenologiaTab> forGrado(int dia, float gDia, long idVariedad) throws Exception {

        int d = (8 - dia);

        float[] img = new float[4];
        img[0] = (d) * gDia;
        img[1] = (d + 7) * gDia;
        img[2] = (d + 21) * gDia;
        img[3] = (d + 28) * gDia;
        int c = 0;


        Iterator<fenologiaTab> i = all().iterator();
        List<fenologiaTab> fi = new ArrayList<>();
        fenologiaTab fu = new fenologiaTab();

        while (i.hasNext()) {
            fenologiaTab f = i.next();
            if (f.getIdFenologia() == idVariedad) {
                if (img[c] <= f.getGrados_dia()) {
                    double pos = f.getGrados_dia() - img[c];
                    double pre = img[c] - fu.getGrados_dia();

                    if (pre >= pos) {
                        fi.add(f);
                    } else {
                        fi.add(fu);
                    }
                    c++;
                    if (c >= 4) {
                        break;
                    }
                }
                fu = f;
            }
        }
        if (c < 4) {
            fi.add(fu);
        }

        return fi;
    }
}