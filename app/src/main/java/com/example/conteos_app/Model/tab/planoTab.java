package com.example.conteos_app.Model.tab;

public class planoTab {
    private Long idSiembra;
    private int idFinca;
    private String finca;
    private int idBloque;
    private String bloque;
    private int idVariedad;
    private String variedad;

    public planoTab() {
    }

    public planoTab(long idSiembra, int idFinca, String finca, int idBloque, String bloque, int idVariedad, String variedad) {
        this.idSiembra = idSiembra;
        this.idFinca = idFinca;
        this.finca = finca;
        this.idBloque = idBloque;
        this.bloque = bloque;
        this.idVariedad = idVariedad;
        this.variedad = variedad;
    }

    public long getIdSiembra() {
        return idSiembra;
    }

    public void setIdSiembra(long idSiembra) {
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

    public int getidVariedad() {
        return idVariedad;
    }

    public void setidVariedad(int idVariedad) {
        this.idVariedad = idVariedad;
    }

    public String getVariedad() {
        return variedad;
    }

    public void setVariedad(String variedad) {
        variedad = variedad;
    }

    @Override
    public String toString() {
        return "{" +
                "\"idSiembra\":" + idSiembra + ",\n" +
                "\"idFinca\":" + idFinca + ",\n" +
                "\"finca\": \"" + finca + "\",\n" +
                "\"idBloque\":" + idBloque + ",\n" +
                "\"bloque\": \"" + bloque + "\",\n" +
                "\"idVariedad\": " + idVariedad + ",\n" +
                "\"variedad\": \"" + variedad + "\"\n" +
                "}";
    }

    /* public String message;*/


}
