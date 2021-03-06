package com.lotus.conteos_app.Model.tab;

public class planoTab {
    private Long idSiembra;
    private int idFinca;
    private String finca;
    private int idBloque;
    private String bloque;
    private int idVariedad;
    private String variedad;
    private int cama;
    private String sufijo;
    private int plantas;
    private double area;

    public planoTab() {
    }

    public planoTab(Long idSiembra, int idFinca, String finca, int idBloque, String bloque, int idVariedad, String variedad, int cama, String sufijo, int plantas, double area) {
        this.idSiembra = idSiembra;
        this.idFinca = idFinca;
        this.finca = finca;
        this.idBloque = idBloque;
        this.bloque = bloque;
        this.idVariedad = idVariedad;
        this.variedad = variedad;
        this.cama = cama;
        this.sufijo = sufijo;
        this.plantas = plantas;
        this.area = area;
    }

    public Long getIdSiembra() {
        return idSiembra;
    }

    public void setIdSiembra(Long idSiembra) {
        this.idSiembra = idSiembra;
    }

    public int getIdFinca() {
        return idFinca;
    }

    public void setIdFinca(int idFinca) {
        this.idFinca = idFinca;
    }

    public String getFinca() {
        return finca;
    }

    public void setFinca(String finca) {
        this.finca = finca;
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

    public int getCama() {
        return cama;
    }

    public void setCama(int cama) {
        this.cama = cama;
    }

    public String getSufijo() {
        return sufijo;
    }

    public void setSufijo(String sufijo) {
        this.sufijo = sufijo;
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

    @Override
    public String toString() {
        String s;
        if (sufijo != null) {
            s = sufijo;
        } else {
            s = "";
        }
        return "{" +
                "\"idSiembra\":" + idSiembra + ",\n" +
                "\"idFinca\":" + idFinca + ",\n" +
                "\"finca\": \"" + finca + "\",\n" +
                "\"idBloque\": " + idBloque + ",\n" +
                "\"bloque\": \"" + bloque + "\",\n" +
                "\"idVariedad\": " + idVariedad + ",\n" +
                "\"variedad\": \"" + variedad + "\",\n" +
                "\"cama\": " + cama + ",\n" +
                "\"plantas\": " + plantas + ",\n" +
                "\"area\": " + area + ",\n" +
                "\"sufijo\": \"" + s + "\" \n" +
                "}";
    }
}