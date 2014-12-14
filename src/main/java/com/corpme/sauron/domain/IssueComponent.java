package com.corpme.sauron.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "issuecomponent")
public class IssueComponent {

    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "component_id")
    Component component;

    @ManyToOne
    @JoinColumn(name = "issue_id")
    @JsonIgnore
    Issue issue;

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
