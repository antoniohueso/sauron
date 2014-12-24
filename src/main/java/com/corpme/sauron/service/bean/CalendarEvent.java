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
    String color;
    String imputado;

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public CalendarEvent(String username, Date fecha, String color,String imputado) {
        this.title = username;
        this.start = df.format(fecha);
        this.color = color;
        this.imputado = imputado;
    }

    public String getTitle() {
        return title;
    }

    public String getStart() {
        return start;
    }

    public String getColor() {
        return color;
    }

    public String getImputado() {
        return imputado;
    }
}
