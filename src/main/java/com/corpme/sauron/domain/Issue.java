package com.corpme.sauron.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Issue {

    @Id
    long id;

    @Column(name = "issuekey")
    String issuekey;

    @Column(name = "reporter")
    String reporter;

    @Column(name = "reporter_desc")
    String reporterDescription;

    @Column(name = "reporter_email")
    String reporterEmail;

    @Column(name = "assignee")
    String assignee;

    @Column(name = "assignee_desc")
    String assigneeDescription;

    @Column(name = "assignee_email")
    String assigneeEmail;

    @Column(name = "summary")
    String summary;

    @Column(name = "description")
    String description;

    @ManyToOne
    @JoinColumn(name = "project_id")
    Project project;

    @Column(name = "centrocoste")
    String centroDeCoste;

    @Column(name = "status_id")
    Long statusId;

    @Column(name = "status")
    String status;

    @Column(name = "created")
    Date created;

    @Column(name = "updated")
    Date updated;

    @Column(name = "duedate")
    Date duedate;

    @Column(name = "issuetype_id")
    Long issuetypeId;

    @Column(name = "issuetype")
    String issuetype;

    @Column(name = "priority_id")
    Long priorityId;

    @Column(name = "priority")
    String priority;

    @Column(name = "timeestimate")
    long timeestimate;

    @Column(name = "timeoriginalestimate")
    long timeoriginalestimate;

    @Column(name = "timespent")
    long timespent;

    @Column(name = "resolutiondate")
    Date resolutionDate;

    @Column(name = "resolution")
    String resolution;

    @OneToMany(mappedBy = "issue",fetch = FetchType.EAGER)
    Set<IssueComponent> components;

    @OneToMany(mappedBy = "issue",fetch = FetchType.EAGER)
    Set<IssueVersion> versions;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIssuekey() {
        return issuekey;
    }

    public void setIssuekey(String issuekey) {
        this.issuekey = issuekey;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getReporterDescription() {
        return reporterDescription;
    }

    public void setReporterDescription(String reporterDescription) {
        this.reporterDescription = reporterDescription;
    }

    public String getReporterEmail() {
        return reporterEmail;
    }

    public void setReporterEmail(String reporterEmail) {
        this.reporterEmail = reporterEmail;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getAssigneeDescription() {
        return assigneeDescription;
    }

    public void setAssigneeDescription(String assigneeDescription) {
        this.assigneeDescription = assigneeDescription;
    }

    public String getAssigneeEmail() {
        return assigneeEmail;
    }

    public void setAssigneeEmail(String assigneeEmail) {
        this.assigneeEmail = assigneeEmail;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getCentroDeCoste() {
        return centroDeCoste;
    }

    public void setCentroDeCoste(String centroDeCoste) {
        this.centroDeCoste = centroDeCoste;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Long getIssuetypeId() {
        return issuetypeId;
    }

    public void setIssuetypeId(Long issuetypeId) {
        this.issuetypeId = issuetypeId;
    }

    public String getIssuetype() {
        return issuetype;
    }

    public void setIssuetype(String issuetype) {
        this.issuetype = issuetype;
    }

    public Long getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Long priorityId) {
        this.priorityId = priorityId;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public long getTimeestimate() {
        return timeestimate;
    }

    public void setTimeestimate(long timeestimate) {
        this.timeestimate = timeestimate;
    }

    public long getTimeoriginalestimate() {
        return timeoriginalestimate;
    }

    public void setTimeoriginalestimate(long timeoriginalestimate) {
        this.timeoriginalestimate = timeoriginalestimate;
    }

    public long getTimespent() {
        return timespent;
    }

    public void setTimespent(long timespent) {
        this.timespent = timespent;
    }

    public Date getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(Date resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Set<IssueComponent> getComponents() {
        return components;
    }

    public void setComponents(Set<IssueComponent> components) {
        this.components = components;
    }

    public Set<IssueVersion> getVersions() {
        return versions;
    }

    public void setVersions(Set<IssueVersion> versions) {
        this.versions = versions;
    }
}
