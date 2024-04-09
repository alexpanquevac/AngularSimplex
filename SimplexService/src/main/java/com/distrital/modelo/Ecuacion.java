/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distrital.modelo;

/**
 *
 * @author ALEXANDER
 */
public class Ecuacion {
    
    public double variableX1;
    public double variableX2;
    public String signo;
    public double resultado;
    public String tipoEcuacion;

    public Ecuacion() {
    }

    public double getVariableX1() {
        return variableX1;
    }

    public void setVariableX1(double variableX1) {
        this.variableX1 = variableX1;
    }

    public double getVariableX2() {
        return variableX2;
    }

    public void setVariableX2(double variableX2) {
        this.variableX2 = variableX2;
    }

    public String getSigno() {
        return signo;
    }

    public void setSigno(String signo) {
        this.signo = signo;
    }

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public String getTipoEcuacion() {
        return tipoEcuacion;
    }

    public void setTipoEcuacion(String tipoEcuacion) {
        this.tipoEcuacion = tipoEcuacion;
    }
    
    
    
}
