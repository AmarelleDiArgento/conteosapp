package com.lotus.conteos_app.Model.tab;


public class fenologiaTab {
    private long idFenologia;
    private long idVariedad;
    private String variedad;
    private double grados_dia;
    private double diametro_boton;
    private double largo_boton;
    private String imagen;


    public long getIdFenologia() {
        return idFenologia;
    }

    public fenologiaTab() {
    }

    public fenologiaTab(long idVariedad, String variedad, double grados_dia, double diametro_boton, double largo_boton, String imagen) {
        this.idVariedad = idVariedad;
        this.variedad = variedad;
        this.grados_dia = grados_dia;
        this.diametro_boton = diametro_boton;
        this.largo_boton = largo_boton;
        this.imagen = imagen;
    }

    public fenologiaTab(long idFenologia, long idVariedad, String variedad, double grados_dia, double diametro_boton, double largo_boton, String imagen) {
        this.idFenologia = idFenologia;
        this.idVariedad = idVariedad;
        this.variedad = variedad;
        this.grados_dia = grados_dia;
        this.diametro_boton = diametro_boton;
        this.largo_boton = largo_boton;
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "{" +
                "\"idFenologia\":" + idFenologia + ",\n" +
                "\"idVariedad\":" + idVariedad + ",\n" +
                "\"variedad\": \"" + variedad + "\",\n" +
                "\"grados_dia\":" + grados_dia + ",\n" +
                "\"diametro_boton\":" + diametro_boton + ",\n" +
                "\"largo_boton\":" + largo_boton + ",\n" +
                "\"imagen\": \"" + imagen + "\" \n" +
                "}";
    }

    public void setIdFenologia(long idFenologia) {
        this.idFenologia = idFenologia;
    }

    public long getIdVariedad() {
        return idVariedad;
    }

    public void setIdVariedad(long idVariedad) {
        this.idVariedad = idVariedad;
    }

    public String getVariedad() {
        return variedad;
    }

    public void setVariedad(String variedad) {
        this.variedad = variedad;
    }

    public double getGrados_dia() {
        return grados_dia;
    }

    public void setGrados_dia(double grados_dia) {
        this.grados_dia = grados_dia;
    }

    public double getDiametro_boton() {
        return diametro_boton;
    }

    public void setDiametro_boton(double diametro_boton) {
        this.diametro_boton = diametro_boton;
    }

    public double getLargo_boton() {
        return largo_boton;
    }

    public void setLargo_boton(double largo_boton) {
        this.largo_boton = largo_boton;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }


}
