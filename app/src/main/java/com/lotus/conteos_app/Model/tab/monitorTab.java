package com.lotus.conteos_app.Model.tab;

public class monitorTab {
    private long idMonitor;
    private String codigo;
    private String nombres;
    private String apellidos;
    private String password;
    private int idFinca;
    private boolean estado;


    public monitorTab() {

    }

    public monitorTab(long idMonitor, String codigo, String nombres, String apellidos, String password, int idFinca, boolean estado) {
        this.idMonitor = idMonitor;
        this.codigo = codigo;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.password = password;
        this.idFinca = idFinca;
        this.estado = estado;
    }

    public String getFullName() {
        return nombres + " " + apellidos;
    }

    @Override
    public String toString() {
        return "{" +
                "\"idMonitor\":" + idMonitor + ",\n" +
                "\"codigo\": \"" + codigo + "\",\n" +
                "\"nombres\": \"" + nombres + "\",\n" +
                "\"apellidos\": \"" + apellidos + "\",\n" +
                "\"password\": \"" + password + "\",\n" +
                "\"idFinca\":" + idFinca + ",\n" +
                "\"estado\":" + estado + "\n" +
                "}";
    }
    public long getIdMonitor() {
        return idMonitor;
    }

    public void setIdMonitor(long idMonitor) {
        this.idMonitor = idMonitor;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdFinca() {
        return idFinca;
    }

    public void setIdFinca(int idFinca) {
        this.idFinca = idFinca;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
