package com.corpme.sauron.service.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarEvent {

    String title;
    String start;
    String end;
    CalendarEventType eventType;
    String alerta;
    String comentario;
    Object data;

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public CalendarEvent(final Date fstart, final Date fend, final String title, final CalendarEventType eventType,final Object data) {
        this.title = title;
        this.start = df.format(fstart);
        this.end = df.format(fend);
        //--- Si no es el mismo día suma uno al día final ya que en el calendario la fecha end es 'hasta' la fecha end no incluida
        if(!this.start.equals(this.end)) {
            Calendar cend = new GregorianCalendar();
            cend.setTime(fend);
            cend.add(Calendar.DAY_OF_MONTH,1);
            this.end = df.format(cend.getTime());
        }
        this.eventType = eventType;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
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
