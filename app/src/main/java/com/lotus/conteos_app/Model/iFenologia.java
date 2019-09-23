package com.lotus.conteos_app.Model;

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

    final String ins = "INSERT INTO [dbo].[Fenologia]\n" +
            "           ([idVariedad] ,[variedad] ,[grados_dia] ,[diametro_boton] ,[largo_boton] ,[imagen])\n" +
            "     VALUES\n" +
            "           ( ?, ?, ?, ?, ?, ?, ?);";
    final String upd = "UPDATE [dbo].[Fenologia]\n" +
            "   SET [idVariedad] = ?, [variedad] = ?, ,[grados_dia] = ?, [diametro_boton] = ?, [largo_boton] = ?, [imagen] = ?\n" +
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
    final String allfin = "SELECT [idFenologia]\n" +
            "      ,[idVariedad]\n" +
            "      ,[variedad]\n" +
            "      ,[grados_dia]\n" +
            "      ,[diametro_boton]\n" +
            "      ,[largo_boton]\n" +
            "      ,[imagen]\n" +
            "  FROM [dbo].[Fenologia]\n" +
            "  WHERE [idVariedad] in(SELECT distinct  [idVariedad]\n" +
            "\t  FROM [dbo].[Plano_Siembra]\n" +
            "\t  WHERE [idFinca] = ?);";


    public iFenologia(String path) throws Exception {
        this.cn = getConexion();
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
    public boolean local() throws Exception {


        ResultSet rs;
        PreparedStatement ps = cn.prepareStatement(all);
        rs = ps.executeQuery();
        while (rs.next()) {
            fl.add(gift(rs));
        }

        closeConexion(cn, rs);

        String contenido = fl.toString();

        return ja.CrearArchivo(path, nombre, contenido);
    }

    @Override
    public boolean local(int idFinca) throws Exception {


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
        List<fenologiaTab> fl = gson.fromJson(ja.ObtenerLista(path, nombre), new TypeToken<List<fenologiaTab>>() {
        }.getType());

        return fl;
    }


    @Override
    public List<fenologiaTab> forGrado(int dia, float gDia, int idVariedad) throws Exception {

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
            if (f.getIdVariedad() == idVariedad) {
                if (img[c] <= f.getGrados_dia()) {
                    double pos = f.getGrados_dia() - img[c];
                    double pre = fu.getGrados_dia() - img[c];

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
