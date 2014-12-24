package com.corpme.sauron.service;

import com.corpme.sauron.config.ApplicationException;
import com.corpme.sauron.domain.User;
import com.corpme.sauron.domain.UserRepository;
import com.corpme.sauron.domain.Worklog;
import com.corpme.sauron.domain.WorklogsRepository;
import com.corpme.sauron.service.bean.CalendarEvent;
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

    public Iterable<CalendarEvent> vacaciones(Date fdesde,Date fhasta) {

        final DateFormat df = new SimpleDateFormat("EEEE dd/MM/yyyy");

        final Calendar hasta = new GregorianCalendar();
        hasta.setTime(fhasta);

        final Calendar desde = new GregorianCalendar();
        desde.setTime(fdesde);

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
                color = "#ff9900";
                title += "("+imputado+")";
            }

            vacaciones.add(new CalendarEvent(title, w.getStarted(), color,imputado));
        }

        return vacaciones;
    }
}
