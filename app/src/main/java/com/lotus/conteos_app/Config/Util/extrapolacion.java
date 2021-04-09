package com.lotus.conteos_app.Config.Util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class extrapolacion {
    
    public int extrapolar(int cuadros, int cuadro, int conteo) {
        return (conteo * cuadros) / cuadro;
    }
    
    public int extrapolarConteo(int nTotal, int n1, int n4){
        int diferencia = nTotal - (n1 + n4);
        int pendiente1 = ((n4 - n1) / 3) + n1;
        int pendiente2 = (((n4 - n1) / 3) * 2) + n1;
        
        return (diferencia * pendiente1) / (pendiente1 + pendiente2);
    }

    public List<Float> extrapolarFaltante(int nTotal, int n1, int n4){
        List<Float> respuesta = new ArrayList();

        int pendienteRecta = (n4 - n1) / 3;
        float punto2 = pendienteRecta + n1;
        float punto3 = (pendienteRecta * 2) + n1;
        float porcentaje2 = punto2 / (punto2 + punto3);
        float porcentaje3 = punto3 / (punto2 + punto3);
        int reparticion = nTotal - n1 - n4;

        float estimado2 = porcentaje2 * reparticion;
        float estimado3 = reparticion - estimado2;


        respuesta.add(estimado2);
        respuesta.add(estimado3);

        //Log.i("Estimados", "pendienteRecta : "+pendienteRecta+" punto2 : "+punto2+" pubot3 : "+punto3+" porcentaje2 : "+porcentaje2+" reparticion : "+reparticion+" estimado2 : "+estimado2+" estimado3 : "+estimado3);
        return respuesta;

    }


}
