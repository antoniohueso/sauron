package com.corpme.sauron.domain.jira;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class ResponseIssue {

    Long id;

    String key;

    Issue fields;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Issue getFields() {
        return fields;
    }

    public void setFields(Issue fields) {
        this.fields = fields;
    }
}
