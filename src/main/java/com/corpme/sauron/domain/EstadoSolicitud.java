package com.corpme.sauron.domain;

import javax.persistence.*;

@Entity
@Table(name = "estadosolicitud")
public class EstadoSolicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "nombre")
    String nombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object obj) {
        EstadoSolicitud estadoSolicitud = (EstadoSolicitud)obj;
        return estadoSolicitud.getId() == this.id;
    }
}
