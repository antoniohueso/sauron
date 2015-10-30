package com.corpme.sauron.domain.jira;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties
public class ResponseIssue {

    String key;

    Issue fields;

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
