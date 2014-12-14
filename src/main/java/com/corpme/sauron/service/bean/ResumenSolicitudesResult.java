package com.corpme.sauron.service.bean;

import org.springframework.stereotype.Component;

/**
 * Created by ahg on 14/12/14.
 */

public class ResumenSolicitudesResult {
    int pendienteAsignarEquipo = 0;
    int pendientePlanificacionDesarrollo = 0;
    int desarrollando = 0;
    int pendientePlanificacionPruebas = 0;
    int probando;
    int finalizada;

    public int getPendienteAsignarEquipo() {
        return pendienteAsignarEquipo;
    }

    public void setPendienteAsignarEquipo(int pendienteAsignarEquipo) {
        this.pendienteAsignarEquipo = pendienteAsignarEquipo;
    }

    public int getPendientePlanificacionDesarrollo() {
        return pendientePlanificacionDesarrollo;
    }

    public void setPendientePlanificacionDesarrollo(int pendientePlanificacionDesarrollo) {
        this.pendientePlanificacionDesarrollo = pendientePlanificacionDesarrollo;
    }

    public int getDesarrollando() {
        return desarrollando;
    }

    public void setDesarrollando(int desarrollando) {
        this.desarrollando = desarrollando;
    }

    public int getPendientePlanificacionPruebas() {
        return pendientePlanificacionPruebas;
    }

    public void setPendientePlanificacionPruebas(int pendientePlanificacionPruebas) {
        this.pendientePlanificacionPruebas = pendientePlanificacionPruebas;
    }

    public int getProbando() {
        return probando;
    }

    public void setProbando(int probando) {
        this.probando = probando;
    }

    public int getFinalizada() {
        return finalizada;
    }

    public void setFinalizada(int finalizada) {
        this.finalizada = finalizada;
    }
}
