package com.lotus.conteos_app.Model.tab;

import java.util.Date;
import java.util.Objects;

public class conteoTab {
    private long idConteo;
    private Date fecha;
    private long idSiembra;
    private int cuadro;
    private int conteo1;
    private int conteo2;
    private int conteo3;
    private int conteo4;
    private int IdUsuario;

    public conteoTab() {
    }

    public conteoTab(long idConteo, Date fecha, long idSiembra, int cuadro, int conteo1, int conteo2, int conteo3, int conteo4, int idUsuario) {
        this.idConteo = idConteo;
        this.fecha = fecha;
        this.idSiembra = idSiembra;
        this.cuadro = cuadro;
        this.conteo1 = conteo1;
        this.conteo2 = conteo2;
        this.conteo3 = conteo3;
        this.conteo4 = conteo4;
        IdUsuario = idUsuario;
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

    public int getConteo1() {
        return conteo1;
    }

    public void setConteo1(int conteo1) {
        this.conteo1 = conteo1;
    }

    public int getConteo2() {
        return conteo2;
    }

    public void setConteo2(int conteo2) {
        this.conteo2 = conteo2;
    }


    public int getConteo3() {
        return conteo3;
    }

    public void setConteo3(int conteo2) {
        this.conteo3 = conteo2;
    }

    public int getConteo4() {
        return conteo4;
    }

    public void setConteo4(int conteo4) {
        this.conteo4 = conteo4;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }




    @Override
    public String toString() {
        return "{" +
                "\"id\":" + idConteo + ",\n" +
                "\"fecha\":" + fecha + ",\n" +
                "\"idSiembra\":" + idSiembra + ",\n" +
                "\"cuadro\":" + cuadro + ",\n" +
                "\"conteo1\":" + conteo1 + ",\n" +
                "\"conteo2\":" + conteo2 + "\n" +
                "\"conteo3\":" + conteo3 + ",\n" +
                "\"conteo4\":" + conteo4 + "\n" +
                "\"IdUsuario\":" + IdUsuario + "\n" +
                "}";
    }

}
