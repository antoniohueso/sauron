package com.corpme.sauron.domain;

public enum RolType {

    Desarrollador(0),
    Tester(10),
    Invitado(90),
    Supervisor(100);

    private int id = 0;

    private RolType(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }

    public static RolType get(int id) {
        for(RolType rol : RolType.values()) {
            if(rol.getValue() == id) return rol;
        }
        return null;
    }



}
