package com.lotus.conteos_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lotus.conteos_app.Config.Util.jsonAdmin;
import com.lotus.conteos_app.Model.iPlano;
import com.lotus.conteos_app.Model.tab.planoTab;

import java.util.ArrayList;
import java.util.List;

public class HistorialMainActivity extends AppCompatActivity {

    List<planoTab> pl = new ArrayList<>();

    jsonAdmin ja = null;
    String path = null;
    EditText data_tbl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historial_main);

        data_tbl =(EditText) findViewById(R.id.data_tbl);

        ja = new jsonAdmin(path);

        //INICIALIZAMOS PLANOS
        actualizarPlano();
        cargarPlanoLocal();
    }

    // descarga el plano de la basde datos y lo alamcena en plano.json (Archivo local)
    public void actualizarPlano() {

        try {
            iPlano iP = new iPlano();
            String nombre = "plano";
            String contenido = iP.all().toString();
            data_tbl.setText(contenido);
            if (ja.CrearArchivo(nombre, contenido)) {
                Toast.makeText(this, "Plano generado exitosamente"+ja, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Error al generar el plano  /n"+ja, Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Plano local, error  " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    // convierte el contenido del archivo plano.json en un arreglo (REQUIERE UNA LISTA GLOBAL)
    public void cargarPlanoLocal() {
        try {
            Gson gson = new Gson();
            pl = gson.fromJson(ja.ObtenerLista("plano.json"), new TypeToken<List<planoTab>>() {
            }.getType());
        } catch (Exception e) {
            //data.setText(e.toString());
            Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }


}
