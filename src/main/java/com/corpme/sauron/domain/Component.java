package com.corpme.sauron.domain;

import javax.persistence.*;

@Entity
@Table(name = "component")
public class Component {

    @Id
    Long id;

    @Column(name = "name")
    String name;

    @ManyToOne
    @JoinColumn(name = "project_id")
    Project project;

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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
