package com.corpme.sauron.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "rfcissuelink")
public class RfcIssueLink {

    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "rfc_id")
    @JsonIgnore
    Rfc rfc;

    @ManyToOne
    @JoinColumn(name = "issue_id")
    Issue issue;

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

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }
}
