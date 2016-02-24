package com.corpme.sauron.domain.jira;

public enum StatusKey {

    DETENIDA(10200),
    OPEN(1),
    DESARROLLANDO(10004),
    RESOLVED(5)
    ,DETECTADO_ERROR_PRUEBAS(10005),
    DISPONIBLE_PARA_PRUEBAS(10000),
    PROBANDO(10001),
    FINALIZADA(10002),
    CERRADA(6),
    EN_PRODUCCION(10003);

    private int id = 0;

    private StatusKey(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }

    public static StatusKey statusKey(int id) {
        for(int i = 0; i < StatusKey.values().length; i++) {
            if(StatusKey.values()[i].getValue() == id) return StatusKey.values()[i];
        }
        throw new RuntimeException("No existe el tipo enum para el valor: "+id);
    }


}
