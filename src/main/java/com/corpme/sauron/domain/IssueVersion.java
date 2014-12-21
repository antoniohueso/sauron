package com.corpme.sauron.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "issueversion")
public class IssueVersion {

    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "issue_id")
    @JsonIgnore
    Issue issue;

    @ManyToOne
    @JoinColumn(name = "version_id")
    Version version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }
}
