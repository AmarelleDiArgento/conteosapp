package com.lotus.conteos_app.Config.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class jsonAdmin {

    public jsonAdmin() {
    }

    public String ObtenerLista(String path, String nombre) throws Exception {
        String jsonString = "";
        path = path + nombre + ".json";
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        jsonString = sb.toString();

        return jsonString;
    }

    public List<String> listFiles(String path) {
        List<String> list = new ArrayList<String>();
        File f = new File(path);

        //obtiene nombres de archivos dentro del directorio.
        File[] file = f.listFiles();
        for (int i = 0; i < file.length; i++) {
            //Agrega nombres de archivos a List para ser agregado a adapter.
            if (!file[i].getName().equalsIgnoreCase("plano.json")) {
                list.add(file[i].getName());
            }
        }
        return list;
    }

    // generador de archivos, requiere dos cadenas de texto nombre de archivo y contenido del mismo
    public boolean CrearArchivo(String path, String nombre, String contenido) {
        boolean ok = false;
        try {
            FileOutputStream fos = null;
            File f = new File(path + nombre + ".json");
            fos = new FileOutputStream(f);
            fos.write(contenido.getBytes());
            fos.close();
            ok = true;
        } catch (Exception e) {
            ok = false;
        }
        return ok;
    }
}
