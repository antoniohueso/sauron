package com.corpme.sauron.service.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarEvent {

    String title;
    String start;
    CalendarEventType eventType;
    String alerta;
    String comentario;
    Object data;

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public CalendarEvent(final Date fecha, final String title, final CalendarEventType eventType,final Object data) {
        this.title = title;
        this.start = df.format(fecha);
        this.eventType = eventType;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public String getStart() {
        return start;
    }

    public Object getData() {
        return data;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getClassName() {
        return eventType.getValue();
    }

    public CalendarEventType getEventType() {
        return eventType;
    }

    public void setEventType(CalendarEventType eventType) {
        this.eventType = eventType;
    }

    public String getAlerta() {
        return alerta;
    }

    public void setAlerta(String alerta) {
        this.alerta = alerta;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getComentario() {
        return comentario;
    }
}
