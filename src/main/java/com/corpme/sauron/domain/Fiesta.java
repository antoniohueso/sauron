package com.corpme.sauron.domain;

import java.util.Date;

/**
 * Created by ahg on 30/12/14.
 */
public class Fiesta {

    Date fecha;

    String summary;

    String description;

    public Fiesta(Date fecha, String summary, String description) {
        this.fecha = fecha;
        this.summary = summary;
        this.description = description;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
}
