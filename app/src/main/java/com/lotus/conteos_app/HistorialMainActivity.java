package com.lotus.conteos_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import com.lotus.conteos_app.Config.Util.jsonAdmin;
import com.lotus.conteos_app.Model.iPlano;
import com.lotus.conteos_app.Model.tab.planoTab;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class HistorialMainActivity extends AppCompatActivity {

    List<planoTab> pl = new ArrayList<>();

    jsonAdmin ja = null;
    EditText data;
    String path = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historial_main);

        data = (EditText) findViewById(R.id.data_tbl);
        path = getExternalFilesDir(null) + File.separator;
        ja = new jsonAdmin(path);

        consulta_Plano();
    }

    public void consulta_Plano(){

        try {
            iPlano iP = new iPlano();
            String contenido = iP.all().toString();
            data.setText(contenido);
        } catch (Exception e) {
            Toast.makeText(this, "Plano local, no hay conexion a la base de datos", Toast.LENGTH_LONG).show();
            data.setText(e.toString());
        }
    }
}
