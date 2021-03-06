package com.corpme.sauron.domain;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;


@Entity
public class Issue {

    @Id
    long id;

    @Column(name = "issuekey")
    String issuekey;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    User reporter;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    User assignee;

    @Column(name = "summary")
    String summary;

    @Column(name = "description")
    String description;

    @ManyToOne
    @JoinColumn(name = "project_id")
    Project project;

    @Column(name = "projectcategory_id")
    Long category_id;

    @Column(name = "created")
    Date created;

    @Column(name = "updated")
    Date updated;

    @Column(name = "duedate")
    Date duedate;

    @ManyToOne
    @JoinColumn(name = "status_id")
    Status status;

    @ManyToOne
    @JoinColumn(name = "issuetype_id")
    Type type;

    @ManyToOne
    @JoinColumn(name = "priority_id")
    Priority priority;

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

    @Column(name = "centro_de_coste")
    String centroDeCoste;

    @OneToMany(mappedBy = "issue")
    Collection<IssueComponent> components;

    @OneToMany(mappedBy = "issue")
    Collection<IssueVersion> versions;

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

    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
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

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
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

    public String getCentroDeCoste() {
        return centroDeCoste;
    }

    public void setCentroDeCoste(String centroDeCoste) {
        this.centroDeCoste = centroDeCoste;
    }

    public Collection<IssueComponent> getComponents() {
        return components;
    }

    public void setComponents(Collection<IssueComponent> components) {
        this.components = components;
    }

    public Collection<IssueVersion> getVersions() {
        return versions;
    }

    public void setVersions(Collection<IssueVersion> versions) {
        this.versions = versions;
    }
}
