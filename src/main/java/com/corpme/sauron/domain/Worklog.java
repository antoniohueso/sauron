package com.corpme.sauron.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "worklog")
public class Worklog {

    @Id
    long id;

    @Column(name = "author")
    String author;

    @Column(name = "author_desc")
    String authorDescription;

    @Column(name = "author_email")
    String authorEmail;

    @Column(name = "updateauthor")
    String updateAuthor;

    @Column(name = "updateauthor_desc")
    String updateAuthorDescripcion;

    @Column(name = "updateauthor_email")
    String updateAuthorEmail;

    @Column(name = "comment")
    String comment;

    @Column(name = "created")
    Date created;

    @Column(name = "updated")
    Date updated;

    @Column(name = "started")
    Date started;

    @Column(name = "timespentseconds")
    long timeSpentSeconds;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "issue_id")
    Issue issue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUpdateAuthor() {
        return updateAuthor;
    }

    public void setUpdateAuthor(String updateAuthor) {
        this.updateAuthor = updateAuthor;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public long getTimeSpentSeconds() {
        return timeSpentSeconds;
    }

    public void setTimeSpentSeconds(long timeSpentSeconds) {
        this.timeSpentSeconds = timeSpentSeconds;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public String getAuthorDescription() {
        return authorDescription;
    }

    public void setAuthorDescription(String authorDescription) {
        this.authorDescription = authorDescription;
    }

    public String getUpdateAuthorDescripcion() {
        return updateAuthorDescripcion;
    }

    public void setUpdateAuthorDescripcion(String updateAuthorDescripcion) {
        this.updateAuthorDescripcion = updateAuthorDescripcion;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getUpdateAuthorEmail() {
        return updateAuthorEmail;
    }

    public void setUpdateAuthorEmail(String updateAuthorEmail) {
        this.updateAuthorEmail = updateAuthorEmail;
    }

}
