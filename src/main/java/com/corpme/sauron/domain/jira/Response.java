package com.corpme.sauron.domain.jira;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties
public class Response {

    int startAt;
    int maxResults;
    int total;
    List<ResponseIssue> issues;

    public int getStartAt() {
        return startAt;
    }

    public void setStartAt(int startAt) {
        this.startAt = startAt;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ResponseIssue> getIssues() {
        return issues;
    }

    public void setIssues(List<ResponseIssue> issues) {
        this.issues = issues;
    }
}
