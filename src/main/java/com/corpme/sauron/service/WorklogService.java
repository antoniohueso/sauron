package com.corpme.sauron.service;

import com.corpme.sauron.config.ApplicationException;
import com.corpme.sauron.domain.User;
import com.corpme.sauron.domain.UserRepository;
import com.corpme.sauron.domain.Worklog;
import com.corpme.sauron.domain.WorklogsRepository;
import com.corpme.sauron.service.bean.CalendarEvent;
import com.corpme.sauron.service.bean.UserEvents;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by ahg on 22/12/14.
 */
@Service
public class WorklogService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WorklogsRepository worklogsRepository;

    @Value("${app.imputa.alarma.min}")
    double alarmaMin;

    final Logger logger = LoggerFactory.getLogger(getClass());

    public LinkedHashMap<User,LinkedHashMap<String,String>> worklogs(Date fdesde,Date fhasta) {

        final DateFormat df = new SimpleDateFormat("EEEE dd/MM/yyyy");

        final Calendar hasta = new GregorianCalendar();
        hasta.setTime(fhasta);

        final Calendar desde = new GregorianCalendar();
        desde.setTime(fdesde);

        if(desde.after(hasta)) {
            throw new ApplicationException("La fecha desde no puede ser mayor que la fecha hasta. F.Desde="
                    +df.format(desde.getTime()) + ", F.Hasta="+df.format(hasta.getTime()));
        }


        final Iterable<User> users = userRepository.findAllFromServiciosCentrales();
        final Iterable<Worklog> worklogs = worklogsRepository.findWorklogs(fdesde, fhasta, users);


        final LinkedHashMap<String,Long> calendario = new LinkedHashMap();
        while(!desde.after(hasta)) {
            int dayOfWeek = desde.get(Calendar.DAY_OF_WEEK);
            if(dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                calendario.put(df.format(desde.getTime()),0L);
            }
            desde.add(Calendar.DAY_OF_MONTH,1);
        }

        final LinkedHashMap<User,LinkedHashMap<String,Long>> index = new LinkedHashMap();
        final LinkedHashMap<User,LinkedHashMap<String,String>> anomalias = new LinkedHashMap();
        for(User user : users) {
            index.put(user, Maps.newLinkedHashMap(calendario));
            anomalias.put(user,new LinkedHashMap<String,String>());
        }

        final Calendar started = new GregorianCalendar();

        for(Worklog w : worklogs) {

            final LinkedHashMap<String,Long> umap = index.get(w.getAuthor());
            //--- Si no encuentra el usuario entre los que pertenecen actualmente a Servicios centrales
            //    puede ser que se hubiera dado de baja en el grupo por lo que le añadimos al LinkedHashMap
            if(umap == null) {
                index.put(w.getAuthor(), Maps.newLinkedHashMap(calendario));
                anomalias.put(w.getAuthor(),new LinkedHashMap<String,String>());
            }
            started.setTime(w.getStarted());
            int dayOfWeek = started.get(Calendar.DAY_OF_WEEK);
            if(dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                String strStarted = df.format(w.getStarted());
                Long time = umap.get(strStarted);
                if(time == null) {
                    logger.warn("Atención!!!!, Esta fecha no aparece en el periodo buscado: "+strStarted);
                }
                else {
                    time += w.getTimeSpentSeconds();
                    umap.put(strStarted,time);
                }
            }
        }

        HashMap<String,Long> festivo = new HashMap();

        //--- Cuenta como festivos aquellos días que todos lo tienen a cero
        for(User user : index.keySet()) {
            final LinkedHashMap<String,Long> umap = index.get(user);
            for(String strStarted : umap.keySet()) {
                Long time = umap.get(strStarted);
                Long festivoTime = festivo.get(strStarted);
                if(festivoTime == null || festivoTime.intValue() == 0) {
                    festivo.put(strStarted,time);
                }
            }
        }
        Iterator it = festivo.keySet().iterator();
        while(it.hasNext()) {
            String key = (String)it.next();
            Long festivoTime = festivo.get(key);
            if(festivoTime.intValue() > 0) {
                it.remove();
            }
        }

        final DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        for(User user : index.keySet()) {
            final LinkedHashMap<String,Long> umap = index.get(user);
            for(String strStarted : umap.keySet()) {
                Long time = umap.get(strStarted);
                double htime = time / (60*60);
                try {
                    started.setTime(df.parse(strStarted));
                } catch (ParseException e) {
                    throw new ApplicationException(e.getMessage(),e);
                }
                if( htime <= alarmaMin && !festivo.containsKey(strStarted)) {
                    if(htime > 0.00 || (htime == 0.00 && !festivo.containsKey(strStarted))) {
                        anomalias.get(user).put(strStarted,
                                String.format("%02d:%02d",
                                        TimeUnit.SECONDS.toHours(time),
                                        TimeUnit.SECONDS.toMinutes(time) -
                                                TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(time))));
                    }

                }
            }
        }

        return anomalias;
    }

    public Collection<CalendarEvent> worklogEvents(Date fdesde,Date fhasta,User user) {

        final DateFormat df = new SimpleDateFormat("EEEE dd/MM/yyyy");

        final Calendar hasta = getComparableDate(fhasta);

        final Calendar desde = getComparableDate(fdesde);

        if(desde.after(hasta)) {
            throw new ApplicationException("La fecha desde no puede ser mayor que la fecha hasta. F.Desde="
                    +df.format(desde.getTime()) + ", F.Hasta="+df.format(hasta.getTime()));
        }


        Collection<User> users = new ArrayList();
        users.add(user);
        final Iterable<Worklog> worklogs = worklogsRepository.findWorklogs(fdesde, fhasta, users);

        final HashMap<String,UserEvents> calendario = new HashMap();

        final Calendar fecha = getComparableDate(fdesde);
        while(!fecha.after(hasta)) {
            int dayOfWeek = fecha.get(Calendar.DAY_OF_WEEK);
            if(dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {

                calendario.put(df.format(fecha.getTime()),new UserEvents(user,fecha.getTime()));

            }
            fecha.add(Calendar.DAY_OF_MONTH,1);
        }

        for(Worklog w : worklogs) {
            UserEvents ue = calendario.get(df.format(w.getStarted()));
            ue.addTotal(w.getTimeSpentSeconds());

            String time = String.format("%02d:%02d",
                    TimeUnit.SECONDS.toHours(w.getTimeSpentSeconds()),
                    TimeUnit.SECONDS.toMinutes(w.getTimeSpentSeconds()) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(w.getTimeSpentSeconds())));

            String className = null;
            if(w.getIssue().getProject().getProjectkey().equals("GI")) {
                className = "calendar-warning";
            }

            ue.getEvents().add(new CalendarEvent(
                    "("+time+") " + w.getIssue().getIssuekey() + " - "+w.getIssue().getSummary(),
                    w.getStarted(),
                    new String[] {className},
                    w.getIssue()
                    ));
        }

        Collection<CalendarEvent> result = new ArrayList();

        Calendar hoy = getComparableDate(new Date());

        Iterable<User> usersServiciosCentrales = userRepository.findAllFromServiciosCentrales();

        for(UserEvents ue : calendario.values()) {

            //--- Si hay anomalías
            if(ue.getTotal() / (60*60) < alarmaMin
                    && (hoy.getTime().after(ue.getFecha()) || hoy.getTime().equals(ue.getFecha()))) {

                //--- Si no tiene eventos ese día
                if(ue.getEvents().size() == 0) {

                    //--- Comprueba que exista alguna imputación ese día de alguien del grupo
                    Iterable<Worklog> wlogs = worklogsRepository
                            .findWorklogsInDay(ue.getFecha(), usersServiciosCentrales);
                    String className = "calendar-danger";
                    //--- Si no existe se presupone que es un día de fiesta
                    if(Lists.newArrayList(wlogs).size() == 0) {
                        className = "calendar-produccion";
                    }

                    ue.getEvents().add(new CalendarEvent("(00:00) ",ue.getFecha()
                            ,new String[]{className},null));
                }
                else {
                    //--- Pone en rojo las anomalías
                    for (CalendarEvent e : ue.getEvents()) {
                        e.setClassName(new String[]{"calendar-danger"});
                    }
                }
            }

            Collections.addAll(result,ue.getEvents().toArray(new CalendarEvent[ue.getEvents().size()]));
        }

        return result;
    }

    public Iterable<CalendarEvent> vacaciones(Date fdesde,Date fhasta) {

        final DateFormat df = new SimpleDateFormat("EEEE dd/MM/yyyy");

        final Calendar hasta = getComparableDate(fhasta);

        final Calendar desde = getComparableDate(fdesde);

        if (desde.after(hasta)) {
            throw new ApplicationException("La fecha desde no puede ser mayor que la fecha hasta. F.Desde="
                    + df.format(desde.getTime()) + ", F.Hasta=" + df.format(hasta.getTime()));
        }

        final Iterable<User> users = userRepository.findAllFromServiciosCentrales();
        final Iterable<Worklog> worklogs = worklogsRepository.findVacaciones(fdesde, fhasta, users);

        Collection<CalendarEvent> vacaciones = new ArrayList();

        for (Worklog w : worklogs) {
            String color = null;
            String title = w.getAuthor().getName();
            final long time = w.getTimeSpentSeconds();
            final String imputado = String.format("%02d:%02d",
                    TimeUnit.SECONDS.toHours(time),
                    TimeUnit.SECONDS.toMinutes(time) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(time)));

            if(w.getIssue().getIssuekey().equals("GI-9")) {
                color = "calendar-warning";
                title += "("+imputado+")";
            }

            vacaciones.add(new CalendarEvent(title, w.getStarted(), new String []{color},imputado));
        }

        return vacaciones;
    }

    Calendar getComparableDate(Date fecha) {

        if(fecha == null) return null;

        Calendar cal = new GregorianCalendar();
        cal.setTime(fecha);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal;
    }
}
