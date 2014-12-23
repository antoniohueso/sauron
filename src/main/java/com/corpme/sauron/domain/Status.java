package com.corpme.sauron.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "status")
public class Status {

    @Id
    Long id;

    @Column(name = "name")
    String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
