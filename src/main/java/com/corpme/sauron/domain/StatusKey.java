package com.corpme.sauron.domain;

/**
 * Created by ahg on 25/12/14.
 */
public enum StatusKey {

    DETENIDA(-20L),
    OPEN(1L),
    DESARROLLANDO(10004L),
    RESOLVED(5L)
    ,DETECTADO_ERROR_PRUEBAS(10005L),
    DISPONIBLE_PARA_PRUEBAS(10000L),
    PROBANDO(10001L),
    FINALIZADA(10002L),
    CERRADA(6L),
    EN_PRODUCCION(10003L);


    private Long id = 0L;

    private StatusKey(Long id) {
        this.id = id;
    }

    public long getValue() {
        return id;
    }


}
