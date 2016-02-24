package com.corpme.sauron.domain.jira;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class IssueLink {

    int id;

    ResponseIssue outwardIssue;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ResponseIssue getOutwardIssue() {
        return outwardIssue;
    }

    public void setOutwardIssue(ResponseIssue outwardIssue) {
        this.outwardIssue = outwardIssue;
    }
}
