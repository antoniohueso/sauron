package com.corpme.sauron.domain;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ausencia")
public class Ausencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotEmpty(message = "La fecha de inicio es obligatoria")
    @Column(name = "fechainicio")
    Date fechaInicio;

    @NotEmpty(message = "La fecha de fin es obligatoria")
    @Column(name = "fechafin")
    Date fechaFin;

    @NotEmpty(message = "El tipo es obligatorio")
    @Range(min = 0, max = 1, message = "El tipo ha de ser 0 - Vacaciones, 1 - Otros")
    @Column(name = "tipo")
    int tipo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
