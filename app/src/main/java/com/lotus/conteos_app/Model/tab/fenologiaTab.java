package com.lotus.conteos_app.Model.tab;


public class fenologiaTab {
    private long idFenologia_gd;
    private long idFenologia;
    private double grados_dia;
    private double diametro_boton;
    private double largo_boton;
    private String imagen;


    public fenologiaTab() {
    }

    public fenologiaTab(long idFenologia_gd, long idFenologia, double grados_dia, double diametro_boton, double largo_boton, String imagen) {
        this.idFenologia_gd = idFenologia_gd;
        this.idFenologia = idFenologia;
        this.grados_dia = grados_dia;
        this.diametro_boton = diametro_boton;
        this.largo_boton = largo_boton;
        this.imagen = imagen;
    }

    public long getIdFenologia_gd() {
        return idFenologia_gd;
    }

    public void setIdFenologia_gd(long idFenologia_gd) {
        this.idFenologia_gd = idFenologia_gd;
    }

    public long getIdFenologia() {
        return idFenologia;
    }

    public void setIdFenologia(long idFenologia) {
        this.idFenologia = idFenologia;
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

    @Override
    public String toString() {
        return "{ \n" +
                "\"idFenologia_dg\":" + idFenologia_gd + ",\n" +
                "\"idFenologia\":" + idFenologia + ",\n" +
                "\"grados_dia\":" + grados_dia + ",\n" +
                "\"diametro_boton\":" + diametro_boton + ",\n" +
                "\"largo_boton\":" + largo_boton + ",\n" +
                "\"imagen\": \"" + imagen + "\" \n" +
                "}";
    }
}
