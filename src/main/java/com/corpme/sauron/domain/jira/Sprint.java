package com.corpme.sauron.domain.jira;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties
public class Sprint {

    String id;

    String name;

    String state;

    Date startDate;

    Date endDate;

    Date completeDate;

    double totalTareas = 0;

    double totalPuntosHistoria = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public double getTotalTareas() {
        return totalTareas;
    }

    public void setTotalTareas(double totalTareas) {
        this.totalTareas = totalTareas;
    }

    public double getTotalPuntosHistoria() {
        return totalPuntosHistoria;
    }

    public void setTotalPuntosHistoria(double totalPuntosHistoria) {
        this.totalPuntosHistoria = totalPuntosHistoria;
    }
}
