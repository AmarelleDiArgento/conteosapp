package com.lotus.conteos_app.Config;

import android.os.StrictMode;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;


public abstract class sqlConect {
    Connection con = null;

    String url = "jdbc:jtds:sqlserver://10.50.1.123;instance=Mercedes;databaseName=Proyecciones";
    String user = "Inventarios";
    String pass = "Inventarios2016*";

    /*
    String url = "jdbc:jtds:sqlserver://192.168.9.7:1433;databaseName=Proyecciones";
    String user = "sa";
    String pass = "Arkangel88";

    */


    public Connection getConexion() throws Exception {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
        //Class.forName("net.sourceforge.jtds.jdbc.Driver");
        return DriverManager.getConnection(url, user, pass);


    }

    public void closeConexion(Connection con) throws Exception {
        if (con != null) {
            con.close();
        }
    }

    public void closeConexion(Connection con, ResultSet rs) throws Exception {
        if (con != null) {
            con.close();
        }
        if (rs != null) {
            rs.close();
        }


    }
}
