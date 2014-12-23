package com.corpme.sauron.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "rfcequipocalidad")
public class RfcEquipoCalidad {

    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "rfc_id")
    @JsonIgnore
    Rfc rfc;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Rfc getRfc() {
        return rfc;
    }

    public void setRfc(Rfc rfc) {
        this.rfc = rfc;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}