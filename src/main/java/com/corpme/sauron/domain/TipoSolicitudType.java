package com.corpme.sauron.domain;

public enum TipoSolicitudType {

    Nuevo_proyecto(1L),
    Mantenimiento(2L),
    Parche(3L);

    private Long id = 0L;

    private TipoSolicitudType(long id) {
        this.id = id;
    }

    public Long getValue() {
        return id;
    }
}
