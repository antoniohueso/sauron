package com.corpme.sauron.service.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ahg on 24/12/14.
 */
public class CalendarEvent {

    String title;
    String start;
    String className;
    String alerta;
    String comentario;
    Object data;

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public CalendarEvent(String title, Date fecha, String className,Object data) {
        this.title = title;
        this.start = df.format(fecha);
        this.className = className;
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
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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
