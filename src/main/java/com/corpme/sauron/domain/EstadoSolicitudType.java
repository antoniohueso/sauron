package com.corpme.sauron.domain;


public enum EstadoSolicitudType {

    Pendiente_asignacion_equipo(0L),
    Pendiente_planificacion_desarrollo(10L),
    Desarrollando(20L),
    Pendiente_planificacion_pruebas(30L),
    Probando(40L),
    Pendiente_paso_produccion(50L),
    En_produccion(60L),
    Cerrado(70L),
    Parado(100L);

    private Long id = 0L;

    private EstadoSolicitudType(Long id) {
        this.id = id;
    }

    public Long getValue() {
        return id;
    }
}
