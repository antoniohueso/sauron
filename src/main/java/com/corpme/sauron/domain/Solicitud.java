package com.corpme.sauron.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "solicitud")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "El tipo de solicitud es obligatoria")
    @ManyToOne
    @JoinColumn(name = "tiposolicitud_id")
    TipoSolicitud tipoSolicitud;

    @ManyToOne
    @JoinColumn(name = "estadosolicitud_id")
    EstadoSolicitud estadoSolicitud;

    @NotEmpty(message = "El título es obligatorio")
    @Column(name = "titulo")
    String titulo;

    @NotEmpty(message = "La descripción es obligatoria")
    @Column(name = "descripcion")
    String descripcion;

    @Column(name="fecha_inicio_desarrollo")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy",timezone="CET")
    Date fechaIniDesarrollo;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy",timezone="CET")
    @Column(name="fecha_fin_desarrollo")
    Date fechaFinDesarrollo;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy",timezone="CET")
    @Column(name="fecha_inicio_pruebas")
    Date fechaIniPruebas;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy",timezone="CET")
    @Column(name="fecha_fin_pruebas")
    Date fechaFinPruebas;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy",timezone="CET")
    @Column(name="fecha_implantacion")
    Date fechaImplantacion;

    @Column(name = "parada")
    int parada;

    @Column(name = "solucion")
    String solucion;

    @Column(name = "plan_paso_produccion")
    String planpasoproduccion;

    @Column(name = "plan_marcha_atras")
    String planmarchaatras;

    @Column(name = "informecalidad")
    String informecalidad;

    @Column(name = "observaciones")
    String observaciones;

    @OneToMany(mappedBy = "solicitud")
    Set<Equipo> equipo;

    @OneToMany(mappedBy = "solicitud")
    Set<IssueSolicitud> issues;

    @OneToMany(mappedBy = "solicitud")
    Set<Comentario> comentarios;

    @OneToMany(mappedBy = "solicitud")
    Set<LogTrabajo> worklog;

    @OneToMany(mappedBy = "solicitud")
    Set<Actividad> actividad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoSolicitud getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(TipoSolicitud tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public EstadoSolicitud getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(EstadoSolicitud estadoSolicitud) {
        this.estadoSolicitud = estadoSolicitud;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaIniDesarrollo() {
        return fechaIniDesarrollo;
    }

    public void setFechaIniDesarrollo(Date fechaIniDesarrollo) {
        this.fechaIniDesarrollo = fechaIniDesarrollo;
    }

    public Date getFechaFinDesarrollo() {
        return fechaFinDesarrollo;
    }

    public void setFechaFinDesarrollo(Date fechaFinDesarrollo) {
        this.fechaFinDesarrollo = fechaFinDesarrollo;
    }

    public Date getFechaIniPruebas() {
        return fechaIniPruebas;
    }

    public void setFechaIniPruebas(Date fechaIniPruebas) {
        this.fechaIniPruebas = fechaIniPruebas;
    }

    public Date getFechaFinPruebas() {
        return fechaFinPruebas;
    }

    public void setFechaFinPruebas(Date fechaFinPruebas) {
        this.fechaFinPruebas = fechaFinPruebas;
    }

    public String getSolucion() {
        return solucion;
    }

    public void setSolucion(String solucion) {
        this.solucion = solucion;
    }

    public String getPlanpasoproduccion() {
        return planpasoproduccion;
    }

    public void setPlanpasoproduccion(String planpasoproduccion) {
        this.planpasoproduccion = planpasoproduccion;
    }

    public String getPlanmarchaatras() {
        return planmarchaatras;
    }

    public void setPlanmarchaatras(String planmarchaatras) {
        this.planmarchaatras = planmarchaatras;
    }

    public String getInformecalidad() {
        return informecalidad;
    }

    public void setInformecalidad(String informecalidad) {
        this.informecalidad = informecalidad;
    }


    public Set<Equipo> getEquipo() {
        return equipo;
    }

    public void setEquipo(Set<Equipo> equipo) {
        this.equipo = equipo;
    }

    public Set<IssueSolicitud> getIssues() {
        return issues;
    }

    public void setIssues(Set<IssueSolicitud> issues) {
        this.issues = issues;
    }

    public Set<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(Set<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public Set<LogTrabajo> getWorklog() {
        return worklog;
    }

    public void setWorklog(Set<LogTrabajo> worklog) {
        this.worklog = worklog;
    }

    public Set<Actividad> getActividad() {
        return actividad;
    }

    public void setActividad(Set<Actividad> actividad) {
        this.actividad = actividad;
    }

    public Date getFechaImplantacion() {
        return fechaImplantacion;
    }

    public void setFechaImplantacion(Date fechaImplantacion) {
        this.fechaImplantacion = fechaImplantacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getParada() {
        return parada;
    }

    public void setParada(int parada) {
        this.parada = parada;
    }
}
