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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class iPlano extends sqlConect implements plano {

    private List<planoTab> pl = new ArrayList<>();
    Connection cn = null;
    String path = null;
    jsonAdmin ja = null;

    int idBloqueRest, idVariedadRest;

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
            this.cn = getConexion();
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
        p.setFinca(rs.getString("finca")+"");
        p.setIdBloque(rs.getInt("idbloque"));
        p.setBloque(rs.getString("bloque")+"");
        p.setIdVariedad(rs.getInt("idVariedad"));
        p.setVariedad(rs.getString("variedad")+"");
        p.setCama(rs.getInt("cama"));
        p.setSufijo(rs.getString("sufijo")+"");
        p.setPlantas(rs.getInt("plantas"));
        p.setArea(rs.getInt("area"));

        //Log.i("CONSULTA", rs.getLong("idSiembra")+"|"+
                //rs.getInt("idFinca")+"|"+
                //rs.getString("finca")+"|"+
                //rs.getInt("idbloque")+"|"+
                //rs.getString("bloque")+"|"+
                //rs.getInt("idVariedad")+"|"+
                //rs.getString("variedad")+"|"+
                //rs.getInt("cama")+"|"+
                //rs.getString("sufijo")+"|"+
                //rs.getInt("plantas")+"|"+
                //rs.getInt("area"));
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
        try {

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
        }catch (Exception e){
            Log.i("ErrorQuery", "Error : "+e.toString());
            return false;
        }
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

        Log.i("SIEMBRA", "Entro : "+idSiembra);

        all();

        if (idSiembra != 0 && pl.size() > 0) {
            Log.i("SIEMBRA", "Entro : "+idSiembra);
            for (planoTab p : pl) {
                if (p.getIdSiembra() == idSiembra) {
                    pr = p;
                    Log.i("SIEMBRA", "llego a encontrar , "+p.getIdBloque());
                    break;
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

    public boolean crearPlano(String fincas) throws Exception{
        try {
            Log.i("farmsSelected", fincas);

            this.cn = getConexion();
            List<planoTab> po = new ArrayList<>();

            ResultSet rs;
            PreparedStatement ps = cn.prepareStatement(q(fincas));
            rs = ps.executeQuery();
            while (rs.next()) {
                po.add(gift(rs));
            }

            String contenido = new Gson().toJson(po);
            Log.i("FINCAS", "llego a crear");
            return ja.CrearArchivo(path, nombre+"_"+fincas, contenido);
        }catch (SQLException ex){
            Log.i("ERROR_SQL",ex.toString());
            return false;
        }
    }

    public String q(String fincas){
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
                "  WHERE idFinca in ( "+fincas+" ) " +
                "  ORDER BY finca";

            Log.i("FINCAS","consulta : "+d);
        return d;
    }

    public planoTab busquedaFinca(int idBloque, int idVariedad) throws Exception{
        planoTab plano = null;
        planoTab pb = null;
        planoTab pv = null;

        idBloqueRest = idBloque;
        idVariedadRest = idVariedad;

        pb = validateBloque(idBloqueRest, "bloque");
        Log.i("VALIDARBLOQUE",(pb != null ? "si " : "no ") + "exite el bloque "+pb.getIdBloque());

        if(pb != null){
            pv = validateBloque(idVariedadRest, "variedad");
            Log.i("VALIDARBLOQUE",(pv != null ? "si " : "no ") + "exite la variedad "+ (pv != null ? pv.getIdVariedad() : ""));
        }

        if(plano == null){
            //busquedaFinca(restaDigitos(idBloque), restaDigitos(idVariedad));
        }
        return plano;
    }


    public planoTab validateBloque(int n, String tipo) throws Exception {
        planoTab b = null;
        for (planoTab p : all()) {

            switch (tipo){
                case "variedad":
                    if (p.getIdBloque() == n) {
                        b = p;
                        break;
                    }
                    break;
                case "bloque":
                    if (p.getIdVariedad() == n) {
                        b = p;
                        break;
                    }
                    break;
            }
        }

        int len =  Integer.toString( tipo.equals("bloque") ? idBloqueRest : idVariedadRest).length();
        if(b == null && len > 0){
            if(tipo.equals("bloque")) {
                idBloqueRest = restaDigitos(idBloqueRest);
                validateBloque(idBloqueRest, "bloque");
            }else{
                idVariedadRest = restaDigitos(idVariedadRest);
                validateBloque(idVariedadRest, "variedad");
            }
        }
        return b;
    }

    public planoTab getFinca(int idBloque, int idVariedad) throws Exception{
        planoTab b = null;
        for (planoTab p : all()) {
            Log.i("buscandoBloque","plano idBloque : "+p.getIdBloque()+" bloque llegada"+idBloqueRest);
            if (p.getIdBloque() == idBloque && p.getIdVariedad() == idVariedad) {
                b = p;
                break;
            }
        }
        return b;
    }

    public int restaDigitos( int nValue){
        int data = Integer.parseInt( String.valueOf(nValue).substring(0, Integer.toString(nValue).length() -1 ) );
        return data;
    }
}