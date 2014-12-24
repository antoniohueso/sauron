package com.corpme.sauron.service;

import com.corpme.sauron.config.ApplicationException;
import com.corpme.sauron.domain.*;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
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

    Logger logger = LoggerFactory.getLogger(getClass());

    public LinkedHashMap<User,LinkedHashMap<String,String>> worklogs(Date fdesde,Date fhasta) {

        Iterable<User> users = userRepository.findAllFromServiciosCentrales();
        Iterable<Worklog> worklogs = worklogsRepository.findByStartedBetweenAndAuthorIn(fdesde,fhasta,users);

        Calendar hasta = new GregorianCalendar();
        hasta.setTime(fhasta);

        Calendar desde = new GregorianCalendar();
        desde.setTime(fdesde);
        LinkedHashMap<String,Long> calendario = new LinkedHashMap();
        DateFormat df = new SimpleDateFormat("EEEE dd/MM/yyyy");
        while(!desde.after(hasta)) {
            int dayOfWeek = desde.get(Calendar.DAY_OF_WEEK);
            if(dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                calendario.put(df.format(desde.getTime()),0L);
            }
            desde.add(Calendar.DAY_OF_MONTH,1);
        }

        LinkedHashMap<User,LinkedHashMap<String,Long>> index = new LinkedHashMap();
        LinkedHashMap<User,LinkedHashMap<String,String>> anomalias = new LinkedHashMap();
        for(User user : users) {
            index.put(user, Maps.newLinkedHashMap(calendario));
            anomalias.put(user,new LinkedHashMap<String,String>());
        }

        Calendar started = new GregorianCalendar();

        for(Worklog w : worklogs) {

            LinkedHashMap<String,Long> umap = index.get(w.getAuthor());
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

        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        for(User user : index.keySet()) {
            LinkedHashMap<String,Long> umap = index.get(user);
            for(String strStarted : umap.keySet()) {
                Long time = umap.get(strStarted);
                double htime = time / (60*60);
                try {
                    started.setTime(df.parse(strStarted));
                } catch (ParseException e) {
                    throw new ApplicationException(e.getMessage(),e);
                }
                if((htime >= 8.00 && htime <= 9.00) || (htime >= 6.00 && htime <= 8.00 && started.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) ) {

                }
                else {
                    anomalias.get(user).put(strStarted,
                            String.format("%02d:%02d",
                                    TimeUnit.SECONDS.toHours(time),
                                    TimeUnit.SECONDS.toMinutes(time) -
                                            TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(time))));

                }
            }
        }

        return anomalias;
    }

}
