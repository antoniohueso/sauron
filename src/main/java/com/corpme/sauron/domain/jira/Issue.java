package com.corpme.sauron.domain.jira;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties
public class Issue {

    String key;

    String summary;

    String description;

    Progress progress;

    Progress aggregateprogress;

    long aggregatetimeoriginalestimate;

    long aggregatetimeestimate;

    long aggregatetimespent;

    User reporter;

    User author;

    User updateAuthor;

    User assignee;

    Date created;

    Date updated;

    Date duedate;

    long timespent;

    IssueType issueType;

    Priority priority;

    List<Issue> subtasks;

    List<Issue> issuelinks;

    Status status;

    Resolution resolution;

    Date resolutiondate;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Progress getAggregateprogress() {
        return aggregateprogress;
    }

    public void setAggregateprogress(Progress aggregateprogress) {
        this.aggregateprogress = aggregateprogress;
    }

    public long getAggregatetimeoriginalestimate() {
        return aggregatetimeoriginalestimate;
    }

    public void setAggregatetimeoriginalestimate(long aggregatetimeoriginalestimate) {
        this.aggregatetimeoriginalestimate = aggregatetimeoriginalestimate;
    }

    public long getAggregatetimeestimate() {
        return aggregatetimeestimate;
    }

    public void setAggregatetimeestimate(long aggregatetimeestimate) {
        this.aggregatetimeestimate = aggregatetimeestimate;
    }

    public long getAggregatetimespent() {
        return aggregatetimespent;
    }

    public void setAggregatetimespent(long aggregatetimespent) {
        this.aggregatetimespent = aggregatetimespent;
    }

    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getUpdateAuthor() {
        return updateAuthor;
    }

    public void setUpdateAuthor(User updateAuthor) {
        this.updateAuthor = updateAuthor;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getDuedate() {
        return duedate;
    }

    public void setDuedate(Date duedate) {
        this.duedate = duedate;
    }

    public long getTimespent() {
        return timespent;
    }

    public void setTimespent(long timespent) {
        this.timespent = timespent;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public List<Issue> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Issue> subtasks) {
        this.subtasks = subtasks;
    }

    public List<Issue> getIssuelinks() {
        return issuelinks;
    }

    public void setIssuelinks(List<Issue> issuelinks) {
        this.issuelinks = issuelinks;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public Date getResolutiondate() {
        return resolutiondate;
    }

    public void setResolutiondate(Date resolutiondate) {
        this.resolutiondate = resolutiondate;
    }
}
