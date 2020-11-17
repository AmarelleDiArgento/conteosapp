package com.lotus.conteos_app.Config.Util;

public class extrapolacion {
    
    public int extrapolar(int cuadros, int cuadro, int conteo) {
        return (conteo * cuadros) / cuadro;
    }
    
    public int extrapolarFaltante(int nTotal, int n1, int n4){
        int diferencia = nTotal - (n1 + n4);
        int pendiente1 = ((n4 - n1) / 3) + n1;
        int pendiente2 = (((n4 - n1) / 3) * 2) + n1;
        
        return (diferencia * pendiente1) / (pendiente1 + pendiente2);
    }
}
