package com.lotus.conteos_app.Config.Util;

import java.io.File;

public class pdfAdmin {
    String path;
    String nameFenology;

    public pdfAdmin(String path, String nameFenology) {
        this.path = path;
        this.nameFenology = nameFenology;
    }

    public File pdfFile(){
        File filepdf = null;
        filepdf = new File(path + "/"+nameFenology+".pdf");

        if(filepdf.exists()){
            pdfFile();
        }

        return filepdf;
    }
}
