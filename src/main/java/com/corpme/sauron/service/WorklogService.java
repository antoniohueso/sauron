package com.corpme.sauron.service;

import com.corpme.sauron.config.ApplicationException;
import com.corpme.sauron.domain.User;
import com.corpme.sauron.domain.UserRepository;
import com.corpme.sauron.domain.Worklog;
import com.corpme.sauron.domain.WorklogsRepository;
import com.corpme.sauron.service.bean.CalendarEvent;
import com.corpme.sauron.service.bean.UserEvents;
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

    @Value("${app.imputa.alarma.min}")
    double alarmaMin;

    final Logger logger = LoggerFactory.getLogger(getClass());

    public Collection<CalendarEvent> anomalias(Date fdesde,Date fhasta,User user) {

        Collection<CalendarEvent> result = worklogEvents(fdesde,fhasta,user,true);
        Iterator it = result.iterator();
        while (it.hasNext()) {
            CalendarEvent event = (CalendarEvent)it.next();
            if(!event.getClassName()[0].equalsIgnoreCase("calendar-danger") && !event.getClassName()[0].equals("calendar-produccion")) {
                it.remove();
            }
        }

        return result;
    }

    public Collection<CalendarEvent> worklogEvents(Date fdesde,Date fhasta,User user,boolean userInTitle) {

        final DateFormat df = new SimpleDateFormat("EEEE dd/MM/yyyy");

        final Calendar hasta = getComparableDate(fhasta);

        final Calendar desde = getComparableDate(fdesde);

        if(desde.after(hasta)) {
            throw new ApplicationException("La fecha desde no puede ser mayor que la fecha hasta. F.Desde="
                    +df.format(desde.getTime()) + ", F.Hasta="+df.format(hasta.getTime()));
        }

        final Collection<User> usersServiciosCentrales =
                Lists.newArrayList(userRepository.findAllFromServiciosCentrales());

        Collection<User> users = null;
        if(user != null) {
            users = new ArrayList();
            users.add(user);
        }
        else {
            users = Lists.newArrayList(usersServiciosCentrales);
        }

        final Iterable<Worklog> worklogs = worklogsRepository.findWorklogs(fdesde, fhasta, users);

        final HashMap<String,HashMap<User,UserEvents>> calendario = new HashMap();

        final Calendar fecha = getComparableDate(fdesde);
        while(!fecha.after(hasta)) {
            int dayOfWeek = fecha.get(Calendar.DAY_OF_WEEK);
            if(dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                HashMap<User,UserEvents> userevents = new HashMap();
                calendario.put(df.format(fecha.getTime()),userevents);

                for(User u : users) {
                    userevents.put(u,new UserEvents(u,fecha.getTime()));
                }
            }
            fecha.add(Calendar.DAY_OF_MONTH,1);
        }

        for(Worklog w : worklogs) {
            UserEvents ue = calendario.get(df.format(w.getStarted())).get(w.getAuthor());
            ue.addTotal(w.getTimeSpentSeconds());

            String time = String.format("%02d:%02d",
                    TimeUnit.SECONDS.toHours(w.getTimeSpentSeconds()),
                    TimeUnit.SECONDS.toMinutes(w.getTimeSpentSeconds()) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(w.getTimeSpentSeconds())));


            String title = null;

            if(!userInTitle) {
                title = "("+time+") " + w.getIssue().getIssuekey() + " - "+w.getIssue().getSummary();
            }
            else {
                title = "("+time+") " + ue.getUser().getName() + " - " + w.getIssue().getIssuekey();
            }


            String className = "calendar-normal";
            if(w.getIssue().getProject().getProjectkey().equals("GI")) {
                className = "calendar-vacaciones";
            }

            ue.getEvents().add(new CalendarEvent(
                    title,
                    w.getStarted(),
                    new String[] {className},
                    w.getIssue()
            ));
        }

        Collection<CalendarEvent> result = new ArrayList();

        Calendar hoy = getComparableDate(new Date());

        for(HashMap<User,UserEvents> userevents : calendario.values()) {

            boolean presuntoDiaDeFiesta = false;

            for (UserEvents ue : userevents.values()) {

                //--- Si hay anomalías
                if (ue.getTotal() / (60 * 60) < alarmaMin
                        && (hoy.getTime().after(ue.getFecha()) || hoy.getTime().equals(ue.getFecha()))) {

                    //--- Si no tiene eventos ese día
                    if (ue.getEvents().size() == 0) {

                        //--- Si está filtrando por usuario averigua si es un posible día de fiesta, para ello
                        //    si ese día tienes menos imputacioes que el total usuarios dividido por dos
                        //    entonces avisa de que podría tratarse de un día de fiesta
                        if(user != null) {
                            Iterable<Worklog> wlogs = worklogsRepository.findWorklogsInDay(ue.getFecha(), users);
                            Collection<Worklog> list = Lists.newArrayList(wlogs);
                            if(list.size() < (usersServiciosCentrales.size() / 2)) {
                                ue.getEvents().add(new CalendarEvent("(00:00) "+ list.size()+" imputaciones" , ue.getFecha()
                                        , new String[]{"calendar-produccion"}, null));
                            }
                        }

                        ue.getEvents().add(new CalendarEvent("(00:00) " + ue.getUser().getName(), ue.getFecha()
                                    , new String[]{"calendar-danger"}, null));

                    } else {
                        //--- Pone en rojo las anomalías
                        for (CalendarEvent e : ue.getEvents()) {
                            e.setClassName(new String[]{"calendar-danger"});
                        }
                    }
                }

                Collections.addAll(result, ue.getEvents().toArray(new CalendarEvent[ue.getEvents().size()]));


            }
        }

        return result;
    }

    public Iterable<CalendarEvent> vacaciones(Date fdesde,Date fhasta,User user) {

        final DateFormat df = new SimpleDateFormat("EEEE dd/MM/yyyy");

        final Calendar hasta = getComparableDate(fhasta);

        final Calendar desde = getComparableDate(fdesde);

        if (desde.after(hasta)) {
            throw new ApplicationException("La fecha desde no puede ser mayor que la fecha hasta. F.Desde="
                    + df.format(desde.getTime()) + ", F.Hasta=" + df.format(hasta.getTime()));
        }

        Collection<User> users = null;
        if(user != null) {
            users = new ArrayList();
            users.add(user);
        }
        else {
            users = Lists.newArrayList(userRepository.findAllFromServiciosCentrales());
        }

        final Iterable<Worklog> worklogs = worklogsRepository.findVacaciones(fdesde, fhasta, users);

        Collection<CalendarEvent> vacaciones = new ArrayList();

        for (Worklog w : worklogs) {
            String title = w.getAuthor().getName();
            final long time = w.getTimeSpentSeconds();
            final String imputado = String.format("%02d:%02d",
                    TimeUnit.SECONDS.toHours(time),
                    TimeUnit.SECONDS.toMinutes(time) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(time)));

            if(w.getIssue().getIssuekey().equals("GI-9")) {
                title += " Ausencia("+imputado+")";
            }

            vacaciones.add(new CalendarEvent(title, w.getStarted(), new String []{"calendar-vacaciones"},imputado));
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
