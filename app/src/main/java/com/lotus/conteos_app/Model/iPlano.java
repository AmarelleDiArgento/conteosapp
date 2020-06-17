package com.lotus.conteos_app.Model;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lotus.conteos_app.Config.Util.jsonAdmin;
import com.lotus.conteos_app.Config.sqlConect;
import com.lotus.conteos_app.Model.interfaz.plano;
import com.lotus.conteos_app.Model.tab.planoTab;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class iPlano extends sqlConect implements plano {

    private List<planoTab> pl = new ArrayList<>();
    Connection cn = null;
    String path = null;
    jsonAdmin ja = null;

    String nombre = "plano";

    final String ins = "INSERT INTO Plano_Siembra\n" +
            "(idSiembra, idFinca, finca, idBloque, bloque, idVariedad, variedad, cama, sufijo)\n" +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);\n";
    final String upd = "UPDATE plano SET\\n\" +\n" +
            "idSiembra=?, idFinca=?, finca=?, idBloque=?, bloque=?, idVariedad=?, variedad=?, cama=?, sufijo=?;";
    final String del = "DELETE FROM Plano_Siembra\n" +
            "WHERE idSiembra=?";
    final String one = "SELECT idSiembra, idFinca, finca, idBloque, bloque, idVariedad, variedad, cama, sufijo \n" +
            "FROM Plano_Siembra;\n" +
            "WHERE idSiembra=?";
    final String nam = "SELECT idSiembra, idFinca, finca, idBloque, bloque, idVariedad, variedad, cama, sufijo \n" +
            "FROM Plano_Siembra;\n" +
            "WHERE finca=?";
    final String all = "SELECT [idSiembra]\n" +
            "      ,[idFinca]\n" +
            "      ,[finca]\n" +
            "      ,[idBloque]\n" +
            "      ,[bloque]\n" +
            "      ,[idVariedad]\n" +
            "      ,[variedad]\n" +
            "      ,[cama]\n" +
            "      ,[sufijo]\n" +
            "      ,[plantas]\n" +
            "      ,[area]\n" +
            "  FROM [Proyecciones].[dbo].[Plano_Siembra]"+
            "  WHERE idFinca in (1004,1005,1006) ";
            //"  and idVariedad in(1358,1101,870,284,115)";

    final String allFin = "SELECT [idSiembra]\n" +
            "      ,[idFinca]\n" +
            "      ,[finca]\n" +
            "      ,[idBloque]\n" +
            "      ,[bloque]\n" +
            "      ,[idVariedad]\n" +
            "      ,[variedad]\n" +
            "      ,[cama]\n" +
            "      ,[sufijo]\n" +
            "      ,[plantas]\n" +
            "      ,[area]\n" +
            "  FROM [Proyecciones].[dbo].[Plano_Siembra]";
            //"  WHERE [idFinca] = ?";

    public iPlano(String path) throws Exception {
        this.cn = getConexion();
        getPath(path);
    }

    public void getPath(String path) {
        ja = new jsonAdmin();
        this.path = path;
    }

    public void getDate(String fecha){
        this.nombre = fecha;
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
            ps.setInt(6, o.getIdVariedad());
            ps.setString(7, o.getVariedad());
            ps.setInt(8, o.getCama());
            ps.setString(9, o.getSufijo());
            if (ps.executeUpdate() == 0) {
                msj = "Ups, algo salio mal. No se registro la siembra #" + o.getIdSiembra();
            } else {
                msj = "El cuadro a sido registrado exitosamente";
            }
            closeConexion(cn);

        } catch (Exception e) {
            msj = e.toString();
        }
        return msj;
    }

    @Override
    public String update(Long id,planoTab o) {
        String msj = "";
        try {
            PreparedStatement ps = cn.prepareStatement(upd);
            ps.setLong(1, o.getIdSiembra());
            ps.setInt(2, o.getIdFinca());
            ps.setString(3, o.getFinca());
            ps.setInt(4, o.getIdBloque());
            ps.setString(5, o.getBloque());
            ps.setInt(6, o.getIdVariedad());
            ps.setString(7, o.getVariedad());
            ps.setInt(8, o.getCama());
            ps.setString(9, o.getSufijo());
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
        p.setIdVariedad(rs.getInt("idVariedad"));
        p.setVariedad(rs.getString("variedad"));
        p.setCama(rs.getInt("cama"));
        p.setSufijo(rs.getString("sufijo"));
        p.setPlantas(rs.getInt("plantas"));
        p.setArea(rs.getInt("area"));
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
    public boolean local() throws Exception {

        List<planoTab> po = new ArrayList<>();

        ResultSet rs;
        PreparedStatement ps = cn.prepareStatement(all);
        rs = ps.executeQuery();
        while (rs.next()) {
            po.add(gift(rs));
        }

        closeConexion(cn, rs);

        String contenido = po.toString();

        return ja.CrearArchivo(path, nombre, contenido);
    }

    @Override
    public List<planoTab> all() throws Exception {

        Gson gson = new Gson();
        pl = gson.fromJson(ja.ObtenerLista(path, nombre), new TypeToken<List<planoTab>>() {
        }.getType());

        return pl;
    }

    @Override
    public planoTab OneforIdSiembra(long idSiembra) throws Exception {
        planoTab pr = new planoTab();

        if (idSiembra != 0 && pl.size() > 0) {

            for (planoTab p : pl) {
                if (p.getIdSiembra() == idSiembra) {
                    pr = p;
                    Log.i("SIEMBRA", "llego a encontrar");
                    break;
                }else {
                    Log.i("SIEMBRA", "no llego a encontrar");
                }
            }

        }
        return pr;
    }

    @Override
    public boolean local(int idFinca)throws Exception {


        List<planoTab> po = new ArrayList<>();

        ResultSet rs;
        PreparedStatement ps = cn.prepareStatement(all);
        rs = ps.executeQuery();
        while (rs.next()) {
            po.add(gift(rs));
        }
        closeConexion(cn, rs);

        String contenido = new Gson().toJson(po);
        return ja.CrearArchivo(path, nombre, contenido);
    }

    public boolean crearPlano(List<Integer> fincas) throws Exception{
        List<planoTab> po = new ArrayList<>();

        ResultSet rs;
        for(Integer idF : fincas) {
            PreparedStatement ps = cn.prepareStatement(q(idF));
            rs = ps.executeQuery();
            while (rs.next()) {
                po.add(gift(rs));
            }
        }
        String contenido = new Gson().toJson(po);
        Log.i("FINCAS", "llego a crear");
        return ja.CrearArchivo(path, nombre, contenido);
    }

    public String q(Integer fincas){
        String d = "SELECT [idSiembra]\n" +
                "      ,[idFinca]\n" +
                "      ,[finca]\n" +
                "      ,[idBloque]\n" +
                "      ,[bloque]\n" +
                "      ,[idVariedad]\n" +
                "      ,[variedad]\n" +
                "      ,[cama]\n" +
                "      ,[sufijo]\n" +
                "      ,[plantas]\n" +
                "      ,[area]\n" +
                "  FROM [Proyecciones].[dbo].[Plano_Siembra]"+
                "  WHERE idFinca in ('"+fincas+"') ";

            Log.i("FINCAS","consulta : "+d);
        return d;
    }
}