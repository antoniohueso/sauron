package com.corpme.sauron.service;

import com.corpme.sauron.config.ApplicationException;
import com.corpme.sauron.domain.User;
import com.corpme.sauron.domain.UserRepository;
import com.corpme.sauron.domain.Worklog;
import com.corpme.sauron.domain.WorklogsRepository;
import com.corpme.sauron.service.bean.CalendarEvent;
import com.corpme.sauron.service.bean.CalendarEventType;
import com.corpme.sauron.service.bean.UserTotalEvents;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
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

    @Autowired
    UtilsService utilsService;

    @Value("${app.imputa.alarma.min}")
    double alarmaMin;

    final Logger logger = LoggerFactory.getLogger(getClass());

    public Iterable<CalendarEvent> anomalias(Date fdesde,Date fhasta,User user) {

        Iterable<CalendarEvent> result = worklogEvents(fdesde,fhasta,user,true);
        Iterator it = result.iterator();
        while (it.hasNext()) {
            CalendarEvent event = (CalendarEvent)it.next();
            if(!event.getEventType().equals(CalendarEventType.DANGER) && !event.getEventType().equals(CalendarEventType.SUCCESS)) {
                it.remove();
            }
        }

        return result;
    }

    public Iterable<CalendarEvent> worklogEvents(final Date fdesde,
                                                   final Date fhasta,
                                                   final User user,
                                                   final boolean userInTitle) {


        final DateFormat df = new SimpleDateFormat("EEEE dd/MM/yyyy");

        final Calendar hasta = utilsService.getComparableDate(fhasta);

        final Calendar desde = utilsService.getComparableDate(fdesde);

        if(desde.after(hasta)) {
            throw new ApplicationException("La fecha desde no puede ser mayor que la fecha hasta. F.Desde="
                    +df.format(desde.getTime()) + ", F.Hasta="+df.format(hasta.getTime()));
        }

        final Collection<User> usersServiciosCentrales =
                Lists.newArrayList(userRepository.findAllFromServiciosCentrales());

        final Collection<User> users;
        if(user != null) {
            users = new ArrayList();
            users.add(user);
        }
        else {
            users = Lists.newArrayList(usersServiciosCentrales);
        }

        final Iterable<Worklog> worklogs = worklogsRepository.findWorklogs(fdesde, fhasta, users);

        final HashMap<String,UserTotalEvents> totalByDayUser = new HashMap();

        final Calendar fecha = utilsService.getComparableDate(fdesde);
        while(!fecha.after(hasta)) {

            final int dayOfWeek = fecha.get(Calendar.DAY_OF_WEEK);

            if(dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                for(User u : users) {
                    totalByDayUser.put(df.format(fecha.getTime())+u.getName(),new UserTotalEvents(u,fecha.getTime()));
                }
            }
            fecha.add(Calendar.DAY_OF_MONTH,1);
        }

        final CalendarService calendarService = new CalendarService();

        worklogs.forEach((w) -> {

            final UserTotalEvents ue = totalByDayUser.get(df.format(w.getStarted()) + w.getAuthor().getName());
            ue.addTotal(w.getTimeSpentSeconds());

            final String time = String.format("%02d:%02d",
                    TimeUnit.SECONDS.toHours(w.getTimeSpentSeconds()),
                    TimeUnit.SECONDS.toMinutes(w.getTimeSpentSeconds()) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(w.getTimeSpentSeconds())));


            final StringBuilder title = new StringBuilder();

            if(!userInTitle) {
                title.append("(").append(time).append(") ")
                        .append(w.getIssue().getIssuekey()).append(" - ").append(w.getIssue().getSummary());
            }
            else {
                title.append("(").append(time).append(") ")
                        .append(ue.getUser().getName()).append(" - ").append(w.getIssue().getIssuekey());
            }


            final CalendarEventType type;
            if(w.getIssue().getProject().getProjectkey().equals("GI")) {
               type = CalendarEventType.WARNING;
            }
            else {
                type = CalendarEventType.INFO;
            }

            final CalendarEvent ev = calendarService.addEvent(w.getStarted(), w.getStarted(),title.toString(), type, w.getIssue());

            ev.setComentario(w.getComment() == null || w.getComment().trim().length() == 0?null:w.getComment());
        });

        Calendar hoy = utilsService.getComparableDate(new Date());

        for(UserTotalEvents ue : totalByDayUser.values()) {

            //--- Si hay anomalías
            if (ue.getTotal() / (60 * 60) < alarmaMin
                    && (hoy.getTime().after(ue.getFecha()) || hoy.getTime().equals(ue.getFecha()))) {

                //--- Si no tiene eventos ese día
                if (ue.getTotal() == 0) {

                    //--- Si está filtrando por usuario averigua si es un posible día de fiesta, para ello
                    //    si ese día tienes menos imputacioes que el total usuarios dividido por dos
                    //    entonces avisa de que podría tratarse de un día de fiesta
                    if(user != null) {
                        Iterable<Worklog> wlogs = worklogsRepository.findWorklogsInDay(ue.getFecha(), users);
                        Collection<Worklog> list = Lists.newArrayList(wlogs);

                        if(list.size() < (usersServiciosCentrales.size() / 2)) {
                            calendarService.addEvent(ue.getFecha(),ue.getFecha(), "(00:00) "+ list.size()+" imputaciones"
                                    , CalendarEventType.INFO.SUCCESS,null);
                        }
                    }

                    calendarService.addEvent(ue.getFecha(),ue.getFecha(), "(00:00) " + ue.getUser().getName()
                            , CalendarEventType.DANGER, null);

                } else {
                    calendarService.addEvent(ue.getFecha(),ue.getFecha(), "(Incompleto) " + ue.getUser().getName()
                            , CalendarEventType.DANGER, null);
                }
            }

        }

        return calendarService.getEvents();

    }

    public Iterable<CalendarEvent> vacaciones(final Date fdesde,final Date fhasta,final User user) {

        final DateFormat df = new SimpleDateFormat("EEEE dd/MM/yyyy");

        final Calendar hasta = utilsService.getComparableDate(fhasta);

        final Calendar desde = utilsService.getComparableDate(fdesde);

        if (desde.after(hasta)) {
            throw new ApplicationException("La fecha desde no puede ser mayor que la fecha hasta. F.Desde="
                    + df.format(desde.getTime()) + ", F.Hasta=" + df.format(hasta.getTime()));
        }

        final Collection<User> users;
        if(user != null) {
            users = new ArrayList();
            users.add(user);
        }
        else {
            users = Lists.newArrayList(userRepository.findAllFromServiciosCentrales());
        }

        final Iterable<Worklog> worklogs = worklogsRepository.findVacaciones(fdesde, fhasta, users);

        final CalendarService calendarService = new CalendarService();

        worklogs.forEach((w) -> {
            final StringBuilder title = new StringBuilder(w.getAuthor().getName());
            final long time = w.getTimeSpentSeconds();
            final String imputado = String.format("%02d:%02d",
                    TimeUnit.SECONDS.toHours(time),
                    TimeUnit.SECONDS.toMinutes(time) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(time)));

            if(w.getIssue().getIssuekey().equals("GI-9")) {
                title.append(" Ausencia(").append(imputado).append(")");
            }

            calendarService.addEvent(w.getStarted(), w.getStarted(), title.toString(), CalendarEventType.GRAY, imputado);
        });

        return calendarService.getEvents();
    }


}
