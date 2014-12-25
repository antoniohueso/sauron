package com.corpme.sauron.service.bean;

import com.corpme.sauron.domain.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by ahg on 25/12/14.
 */
public class UserEvents {

    User user;
    Date fecha;
    long total = 0;
    Collection<CalendarEvent> events = new ArrayList();

    public UserEvents(User user, Date fecha) {
        this.user = user;
        this.fecha = fecha;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFecha() {
        return fecha;
    }

    public Collection<CalendarEvent> getEvents() {
        return events;
    }

    public void addTotal(Long sum) {
        total += sum;
    }

    public long getTotal() {
        return total;
    }
}
