package com.lotus.conteos_app.Model.tab;

public class conteoTab {
    private long idConteo;
    private String fecha;
    private long idSiembra;
    private int cama;
    private int idBloque;
    private String bloque;
    private int idVariedad;
    private String variedad;
    private int cuadro;
    private int conteo1;
    private int conteo2;
    private int conteo3;
    private int conteo4;
    private int total;
    private int plantas;
    private double area;
    private int cuadros;
    private int IdUsuario;

    public conteoTab() {
    }


    public long getIdConteo() {
        return idConteo;
    }

    public void setIdConteo(long idConteo) {
        this.idConteo = idConteo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public long getIdSiembra() {
        return idSiembra;
    }

    public void setIdSiembra(long idSiembra) {
        this.idSiembra = idSiembra;
    }

    public int getCama() {
        return cama;
    }

    public void setCama(int cama) {
        this.cama = cama;
    }

    public int getIdBloque() {
        return idBloque;
    }

    public void setIdBloque(int idBloque) {
        this.idBloque = idBloque;
    }

    public String getBloque() {
        return bloque;
    }

    public void setBloque(String bloque) {
        this.bloque = bloque;
    }

    public int getIdVariedad() {
        return idVariedad;
    }

    public void setIdVariedad(int idVariedad) {
        this.idVariedad = idVariedad;
    }

    public String getVariedad() {
        return variedad;
    }

    public void setVariedad(String variedad) {
        this.variedad = variedad;
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

    public void setConteo3(int conteo3) {
        this.conteo3 = conteo3;
    }

    public int getConteo4() {
        return conteo4;
    }

    public void setConteo4(int conteo4) {
        this.conteo4 = conteo4;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPlantas() {
        return plantas;
    }

    public void setPlantas(int plantas) {
        this.plantas = plantas;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getCuadros() {
        return cuadros;
    }

    public void setCuadros(int cuadros) {
        this.cuadros = cuadros;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "{\n" +
                "\"fecha\": \"" + fecha + "\",\n" +
                "\"id\":" + idConteo + ",\n" +
                "\"idSiembra\":" + idSiembra + ",\n" +
                "\"cama\":" + cama + ",\n" +
                "\"idBloque\": " + idBloque + ",\n" +
                "\"bloque\": \"" + bloque + "\",\n" +
                "\"idVariedad\":" + idVariedad + ",\n" +
                "\"variedad\": \"" + variedad + "\",\n" +
                "\"cuadro\":" + cuadro + ",\n" +
                "\"conteo1\":" + conteo1 + ",\n" +
                "\"conteo2\":" + conteo2 + ",\n" +
                "\"conteo3\":" + conteo3 + ",\n" +
                "\"conteo4\":" + conteo4 + ",\n" +
                "\"total\":" + total + ",\n" +
                "\"plantas\": " + plantas + ",\n" +
                "\"area\": " + area + ",\n" +
                "\"cuadros\": " + cuadros + ",\n" +
                "\"IdUsuario\":" + IdUsuario + "\n" +
                "}"+"\n";
    }
}
