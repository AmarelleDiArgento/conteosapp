package com.example.conteos_app.Model.tab;

import java.util.Date;

public class conteoTab {
    private long idConteo;
    private Date fecha;
    private long idSiembra;
    private int cuadro;
    private int c1;
    private int c4;

    public conteoTab() {
    }

    public conteoTab(Date fecha, long idSiembra, int cuadro, int c1, int c4) {
        this.setFecha(fecha);
        this.setIdSiembra(idSiembra);
        this.setCuadro(cuadro);
        this.setC1(c1);
        this.setC4(c4);
    }

    public conteoTab(long idConteo, Date fecha, long idSiembra, int cuadro, int c1, int c4) {
        this.setIdConteo(idConteo);
        this.setFecha(fecha);
        this.setIdSiembra(idSiembra);
        this.setCuadro(cuadro);
        this.setC1(c1);
        this.setC4(c4);
    }


    public long getIdConteo() {
        return idConteo;
    }

    public void setIdConteo(long idConteo) {
        this.idConteo = idConteo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public long getIdSiembra() {
        return idSiembra;
    }

    public void setIdSiembra(long idSiembra) {
        this.idSiembra = idSiembra;
    }

    public int getCuadro() {
        return cuadro;
    }

    public void setCuadro(int cuadro) {
        this.cuadro = cuadro;
    }

    public int getC1() {
        return c1;
    }

    public void setC1(int c1) {
        this.c1 = c1;
    }

    public int getC4() {
        return c4;
    }

    public void setC4(int c4) {
        this.c4 = c4;
    }

    @Override
    public String toString() {
        return "{" +
                "\"fecha\":" + fecha + ",\n" +
                "\"idSiembra\":" + idSiembra + ",\n" +
                "\"cuadro\":" + cuadro + ",\n" +
                "\"c1\":" + c1 + ",\n" +
                "\"c4\":" + c4 + "\n" +
                "}";
    }

    public String toStringLcl() {
        return "{" +
                "\"idConteo\":" + idConteo + ",\n" +
                "\"fecha\":" + fecha + ",\n" +
                "\"idSiembra\":" + idSiembra + ",\n" +
                "\"cuadro\":" + cuadro + ",\n" +
                "\"c1\":" + c1 + ",\n" +
                "\"c4\":" + c4 + "\n" +
                "}";
    }

}
