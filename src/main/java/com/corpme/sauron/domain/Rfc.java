package com.corpme.sauron.domain;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
public class Rfc {

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

    @OneToMany(mappedBy = "rfc")
    Collection<RfcEquipoDesarrollo> equipodesarrollo;

    @OneToMany(mappedBy = "rfc")
    Collection<RfcEquipoCalidad> equipocalidad;

    @OneToMany(mappedBy = "rfc")
    Collection<RfcRiesgos> riesgos;

    @OneToMany(mappedBy = "rfc")
    Collection<RfcIssueLink> issuelinks;


    @Column(name = "f_inicio_desa")
    Date fInicioDesarrollo;

    @Column(name = "f_fin_desa")
    Date fFinDesarrollo;

    @Column(name = "f_inicio_test")
    Date fInicioCalidad;

    @Column(name = "f_fin_test")
    Date fFinCalidad;

    @Column(name = "f_paso_prod")
    Date fPasoProd;

    @Column(name = "plan_pruebas")
    String planpruebas;

    @Column(name = "observaciones")
    String observaciones;

    @Column(name = "plan_paso_prod")
    String planpasoprod;

    @Column(name = "plan_marcha_atras")
    String planmarchaatras;

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

    public Collection<RfcEquipoDesarrollo> getEquipodesarrollo() {
        return equipodesarrollo;
    }

    public void setEquipodesarrollo(Collection<RfcEquipoDesarrollo> equipodesarrollo) {
        this.equipodesarrollo = equipodesarrollo;
    }

    public Collection<RfcEquipoCalidad> getEquipocalidad() {
        return equipocalidad;
    }

    public void setEquipocalidad(Collection<RfcEquipoCalidad> equipocalidad) {
        this.equipocalidad = equipocalidad;
    }

    public Collection<RfcRiesgos> getRiesgos() {
        return riesgos;
    }

    public void setRiesgos(Collection<RfcRiesgos> riesgos) {
        this.riesgos = riesgos;
    }

    public Collection<RfcIssueLink> getIssuelinks() {
        return issuelinks;
    }

    public void setIssuelinks(Collection<RfcIssueLink> issuelinks) {
        this.issuelinks = issuelinks;
    }

    public Date getfInicioDesarrollo() {
        return fInicioDesarrollo;
    }

    public void setfInicioDesarrollo(Date fInicioDesarrollo) {
        this.fInicioDesarrollo = fInicioDesarrollo;
    }

    public Date getfFinDesarrollo() {
        return fFinDesarrollo;
    }

    public void setfFinDesarrollo(Date fFinDesarrollo) {
        this.fFinDesarrollo = fFinDesarrollo;
    }

    public Date getfInicioCalidad() {
        return fInicioCalidad;
    }

    public void setfInicioCalidad(Date fInicioCalidad) {
        this.fInicioCalidad = fInicioCalidad;
    }

    public Date getfFinCalidad() {
        return fFinCalidad;
    }

    public void setfFinCalidad(Date fFinCalidad) {
        this.fFinCalidad = fFinCalidad;
    }

    public Date getfPasoProd() {
        return fPasoProd;
    }

    public void setfPasoProd(Date fPasoProd) {
        this.fPasoProd = fPasoProd;
    }

    public String getPlanpruebas() {
        return planpruebas;
    }

    public void setPlanpruebas(String planpruebas) {
        this.planpruebas = planpruebas;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getPlanpasoprod() {
        return planpasoprod;
    }

    public void setPlanpasoprod(String planpasoprod) {
        this.planpasoprod = planpasoprod;
    }

    public String getPlanmarchaatras() {
        return planmarchaatras;
    }

    public void setPlanmarchaatras(String planmarchaatras) {
        this.planmarchaatras = planmarchaatras;
    }
}
