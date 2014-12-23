package com.corpme.sauron.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "rfcriesgos")
public class RfcRiesgos {

    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "rfc_id")
    @JsonIgnore
    Rfc rfc;

    @Column(name = "name")
    String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}