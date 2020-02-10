package com.lotus.conteos_app.Model.tab;

public class cuadros_bloqueTab {
  private Long idBloque;
  private Long idVariedad;
  private int numeroCuadros;
  private Long idFenologia;

  public cuadros_bloqueTab() {
  }

  public cuadros_bloqueTab(Long idBloque, Long idVariedad, int numeroCuadros, Long idFenologia) {
    this.idBloque = idBloque;
    this.idVariedad = idVariedad;
    this.numeroCuadros = numeroCuadros;
    this.idFenologia = idFenologia;
  }

  public Long getIdBloque() {
    return idBloque;
  }

  public void setIdBloque(Long idBloque) {
    this.idBloque = idBloque;
  }

  public Long getIdVariedad() {
    return idVariedad;
  }

  public void setIdVariedad(Long idVariedad) {
    this.idVariedad = idVariedad;
  }

  public int getNumeroCuadros() {
    return numeroCuadros;
  }

  public void setNumeroCuadros(int numeroCuadros) {
    this.numeroCuadros = numeroCuadros;
  }

  public Long getIdFenologia() {
    return idFenologia;
  }

  public void setIdFenologia(Long idFenologia) {
    this.idFenologia = idFenologia;
  }

  public  String toString(){
    return "{ \n"+
            "\"idBloque\" : "+idBloque+",\n"+
            "\"idVariedad\" : "+idVariedad+",\n"+
            "\"numeroCuadros\" : "+numeroCuadros+",\n"+
            " \"idFenologia\" : "+idFenologia+" \n"+
            "}";
  }
}
