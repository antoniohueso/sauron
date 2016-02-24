package com.corpme.sauron.domain.jira;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties
public class Issue {

    Long id;

    String key;

    String summary;

    String description;

    Project project;

    Progress progress;

    Progress aggregateprogress;

    long aggregatetimeoriginalestimate;

    long aggregatetimeestimate;

    long aggregatetimespent;

    User reporter;

    @JsonProperty("customfield_10101")
    User desarrollador;

    User author;

    User updateAuthor;

    User assignee;

    Date created;

    Date updated;

    Date duedate;

    long timespent;

    IssueType issuetype;

    Priority priority;

    List<Component> components;

    Component component;

    List<ResponseIssue> subtasks;

    List<IssueLink> issuelinks;

    List <Issue> issues = new ArrayList<>();

    Status status;

    String situacion;

    String completadaSprint = "No completado";

    boolean completada = false;

    Date inicioDesarrollo;

    Date finDesarrollo;

    Date inicioCalidad;

    Date finCalidad;

    Date implantacion;

    String solucion;

    String acuerdofuncional;

    String tablasafectadas;

    String plandepruebas;

    String plandepruebasvalidacion;

    String causadetencion;

    String observaciones;

    String planpasoproduccion;

    String planmarchaatras;

    List<Riesgo> riesgos;

    Resolution resolution;

    Date resolutiondate;

    @JsonProperty("customfield_10002")
    int puntosHistoria;

    @JsonProperty("customfield_10006")
    String epicaKey;

    Issue epica;

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

    public List<ResponseIssue> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<ResponseIssue> subtasks) {
        this.subtasks = subtasks;
    }

    public List<IssueLink> getIssuelinks() {
        return issuelinks;
    }

    public void setIssuelinks(List<IssueLink> issuelinks) {
        this.issuelinks = issuelinks;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public IssueType getIssuetype() {
        return issuetype;

    }

    public void setIssuetype(IssueType issuetype) {
        this.issuetype = issuetype;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
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

    @JsonProperty("inicioDesarrollo")
    public Date getInicioDesarrollo() {
        return inicioDesarrollo;
    }

    @JsonProperty("customfield_10312")
    public void setInicioDesarrollo(Date inicioDesarrollo) {
        this.inicioDesarrollo = inicioDesarrollo;
    }

    @JsonProperty("finDesarrollo")
    public Date getFinDesarrollo() {
        return finDesarrollo;
    }

    @JsonProperty("customfield_10313")
    public void setFinDesarrollo(Date finDesarrollo) {
        this.finDesarrollo = finDesarrollo;
    }

    @JsonProperty("inicioCalidad")
    public Date getInicioCalidad() {
        return inicioCalidad;
    }

    @JsonProperty("customfield_10314")
    public void setInicioCalidad(Date inicioCalidad) {
        this.inicioCalidad = inicioCalidad;
    }

    @JsonProperty("finCalidad")
    public Date getFinCalidad() {
        return finCalidad;
    }

    @JsonProperty("customfield_10315")
    public void setFinCalidad(Date finCalidad) {
        this.finCalidad = finCalidad;
    }

    @JsonProperty("implantacion")
    public Date getImplantacion() {
        return implantacion;
    }

    @JsonProperty("customfield_10309")
    public void setImplantacion(Date implantacion) {
        this.implantacion = implantacion;
    }

    public User getDesarrollador() {
        return desarrollador;
    }

    public void setDesarrollador(User desarrollador) {
        this.desarrollador = desarrollador;
    }

    @JsonProperty("solucion")
    public String getSolucion() {
        return solucion;
    }

    @JsonProperty("customfield_10316")
    public void setSolucion(String solucion) {
        this.solucion = solucion;
    }

    @JsonProperty("acuerdoFuncional")
    public String getAcuerdofuncional() {
        return acuerdofuncional;
    }

    @JsonProperty("customfield_10328")
    public void setAcuerdofuncional(String acuerdofuncional) {
        this.acuerdofuncional = acuerdofuncional;
    }

    @JsonProperty("tablasAfectadas")
    public String getTablasafectadas() {
        return tablasafectadas;
    }

    @JsonProperty("customfield_10327")
    public void setTablasafectadas(String tablasafectadas) {
        this.tablasafectadas = tablasafectadas;
    }

    @JsonProperty("planDePruebas")
    public String getPlandepruebas() {
        return plandepruebas;
    }

    @JsonProperty("customfield_10329")
    public void setPlandepruebas(String plandepruebas) {
        this.plandepruebas = plandepruebas;
    }

    @JsonProperty("planDePruebasValidacion")
    public String getPlandepruebasvalidacion() {
        return plandepruebasvalidacion;
    }
    @JsonProperty("customfield_10330")
    public void setPlandepruebasvalidacion(String plandepruebasvalidacion) {
        this.plandepruebasvalidacion = plandepruebasvalidacion;
    }

    @JsonProperty("causaDetencion")
    public String getCausadetencion() {
        return causadetencion;
    }

    @JsonProperty("customfield_10331")
    public void setCausadetencion(String causadetencion) {
        this.causadetencion = causadetencion;
    }

    @JsonProperty("observaciones")
    public String getObservaciones() {
        return observaciones;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    @JsonProperty("customfield_10320")
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @JsonProperty("planPasoProduccion")
    public String getPlanpasoproduccion() {
        return planpasoproduccion;
    }

    @JsonProperty("customfield_10322")
    public void setPlanpasoproduccion(String planpasoproduccion) {
        this.planpasoproduccion = planpasoproduccion;
    }

    @JsonProperty("planMarchaAtras")
    public String getPlanmarchaatras() {
        return planmarchaatras;
    }
    @JsonProperty("customfield_10323")
    public void setPlanmarchaatras(String planmarchaatras) {
        this.planmarchaatras = planmarchaatras;
    }

    @JsonProperty("riesgos")
    public List<Riesgo> getRiesgos() {
        return riesgos;
    }

    @JsonProperty("customfield_10318")
    public void setRiesgos(List<Riesgo> riesgos) {
        this.riesgos = riesgos;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public int getPuntosHistoria() {
        return puntosHistoria;
    }

    public void setPuntosHistoria(int puntosHistoria) {
        this.puntosHistoria = puntosHistoria;
    }

    public String getEpicaKey() {
        return epicaKey;
    }

    public void setEpicaKey(String epicaKey) {
        this.epicaKey = epicaKey;
    }

    public Issue getEpica() {
        return epica;
    }

    public void setEpica(Issue epica) {
        this.epica = epica;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }

    public String getCompletadaSprint() {
        return completadaSprint;
    }

    public void setCompletadaSprint(String completadaSprint) {
        this.completadaSprint = completadaSprint;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
