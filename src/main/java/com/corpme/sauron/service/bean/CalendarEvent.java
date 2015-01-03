package com.corpme.sauron.service.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarEvent {

    String title;
    String start;
    CalendarEventType type;
    String alerta;
    String comentario;
    Object data;

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public CalendarEvent(final Date fecha, final String title, final CalendarEventType type,final Object data) {
        this.title = title;
        this.start = df.format(fecha);
        this.type = type;
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
        return type.getValue();
    }

    public CalendarEventType getType() {
        return type;
    }

    public void setType(CalendarEventType type) {
        this.type = type;
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
