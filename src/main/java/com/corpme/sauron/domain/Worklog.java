package com.corpme.sauron.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "worklog")
public class Worklog {

    @Id
    long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    User author;

    @ManyToOne
    @JoinColumn(name = "updateauthor_id")
    User updateAuthor;

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

    @ManyToOne
    @JoinColumn(name = "issue_id")
    Issue issue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
