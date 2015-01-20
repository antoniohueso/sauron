package com.corpme.sauron.service;

import com.corpme.sauron.domain.Rfc;
import com.corpme.sauron.domain.StatusKey;
import com.corpme.sauron.service.bean.CalendarEvent;
import com.corpme.sauron.service.bean.CalendarEventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by ahg on 03/01/15.
 */
@Service
public class CalendarService {


    final Collection<CalendarEvent> events = new ArrayList();

    public void addEvent(final CalendarEvent ev) {
        events.add(ev);
    }

    public CalendarEvent addEvent(final Date fstart, final Date fend,final String title,final CalendarEventType type,final Object data) {

        if(fstart == null || fend == null) return null;

        final CalendarEvent ev = new CalendarEvent(fstart, fend, title, type, data);
        events.add(ev);
        return ev;
    }
/*
    public void addEvents(final Date fdesde
            ,final Date fhasta
            ,final String title
            ,final CalendarEventType type
            ,final Object data) {

        addEvents(fdesde,fhasta,(fecha) -> {
             return new CalendarEvent(fecha.getTime(), fecha.getTime(), title, type, data);
        });

    }

    public void addEvents(final Date fdesde,final Date fhasta,final Function<Calendar,CalendarEvent> fn) {

        if(fdesde == null || fhasta == null) return;

        final Calendar desde = utilsService().getComparableDate(fdesde);
        final Calendar hasta = utilsService().getComparableDate(fhasta);

        final Calendar fecha = new GregorianCalendar();
        fecha.setTime(desde.getTime());

        while(!fecha.after(hasta)) {
            events.add(fn.apply(fecha));
            fecha.add(Calendar.DAY_OF_MONTH,1);
        }
    }
*/
    public void removeAllEvents() {
        events.clear();
    }

    public Iterable<CalendarEvent> getEvents() {
        return events;
    }

    @Bean
    UtilsService utilsService() {
        return new UtilsService();
    }

}
