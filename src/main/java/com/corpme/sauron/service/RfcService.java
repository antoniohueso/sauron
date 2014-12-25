package com.corpme.sauron.service;

import com.corpme.sauron.config.ApplicationException;
import com.corpme.sauron.domain.IssueRepository;
import com.corpme.sauron.domain.Rfc;
import com.corpme.sauron.domain.RfcRepository;
import com.corpme.sauron.domain.StatusKey;
import com.corpme.sauron.service.bean.CalendarEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ahg on 22/12/14.
 */
@Service
public class RfcService {

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    RfcRepository rfcRepository;

    Logger logger = LoggerFactory.getLogger(getClass());

    public Map<String,Collection> resumenRfcs(Date fdesde,Date fhasta) {
        final DateFormat df = new SimpleDateFormat("EEEE dd/MM/yyyy");

        final Calendar hasta = getComparableDate(fhasta);
        final Calendar desde = getComparableDate(fdesde);
        final Calendar hoy = getComparableDate(new Date());

        if (desde.after(hasta)) {
            throw new ApplicationException("La fecha desde no puede ser mayor que la fecha hasta. F.Desde="
                    + df.format(desde.getTime()) + ", F.Hasta=" + df.format(hasta.getTime()));
        }
        Iterable<Rfc> rfcs = rfcRepository.findRfcsEnCurso();

        Collection<Rfc> pendientes = new ArrayList();
        Collection<Rfc> paradas = new ArrayList();
        Collection<Rfc> anomalias = new ArrayList();
        Collection<Rfc> vencidas = new ArrayList();
        Collection<Rfc> encurso = new ArrayList();

        Map<String,Collection> result = new HashMap();
        result.put("encurso",encurso);
        result.put("anomalias",anomalias);
        result.put("vencidas",vencidas);
        result.put("pendientes",pendientes);
        result.put("paradas",paradas);

        for(Rfc rfc : rfcs) {

            final Calendar fInicioDesarrollo = getComparableDate(rfc.getfInicioDesarrollo());
            final Calendar fFinDesarrollo = getComparableDate(rfc.getfFinDesarrollo());
            final Calendar fInicioCalidad = getComparableDate(rfc.getfInicioCalidad());
            final Calendar fFinCalidad = getComparableDate(rfc.getfFinCalidad());
            final Calendar fPasoProd = getComparableDate(rfc.getfPasoProd());

            if(rfc.getStatus().getId().equals(StatusKey.OPEN)){
                pendientes.add(rfc);
            }
            else if(rfc.getStatus().getId().equals(StatusKey.DETENIDA)){
                paradas.add(rfc);
            }
            else if(rfc.getStatus().getId().equals(StatusKey.DESARROLLANDO)
                    || rfc.getStatus().getId().equals(StatusKey.RESOLVED)){
                if(fInicioDesarrollo == null || fFinDesarrollo == null) {
                    anomalias.add(rfc);
                }
                else if(fInicioDesarrollo.after(fFinDesarrollo)) {
                    anomalias.add(rfc);
                }
                else {
                    if(hoy.after(fFinDesarrollo)) vencidas.add(rfc);
                    else encurso.add(rfc);
                }
            }
            else if(rfc.getStatus().getId().equals(StatusKey.DISPONIBLE_PARA_PRUEBAS)
                    || rfc.getStatus().getId().equals(StatusKey.PROBANDO)){
                if(fInicioCalidad == null || fFinCalidad == null) {
                    anomalias.add(rfc);
                }
                else if(fInicioCalidad.after(fFinCalidad)) {
                    anomalias.add(rfc);
                }
                else {
                    if(hoy.after(fFinCalidad)) vencidas.add(rfc);
                    else encurso.add(rfc);
                }
            }
            else if(rfc.getStatus().getId().equals(StatusKey.FINALIZADA)){
                if(fPasoProd == null) {
                    anomalias.add(rfc);
                }else {
                    if(hoy.after(fPasoProd)) vencidas.add(rfc);
                    else encurso.add(rfc);
                }
            }
        }

        return result;
    }

    public Collection<CalendarEvent> rfcsEvents(Date fdesde,Date fhasta) {

        final DateFormat df = new SimpleDateFormat("EEEE dd/MM/yyyy");

        final Calendar hasta = getComparableDate(fhasta);
        final Calendar desde = getComparableDate(fdesde);


        if (desde.after(hasta)) {
            throw new ApplicationException("La fecha desde no puede ser mayor que la fecha hasta. F.Desde="
                    + df.format(desde.getTime()) + ", F.Hasta=" + df.format(hasta.getTime()));
        }

        Iterable<Rfc> rfcs = rfcRepository.findRfcsByDate(desde.getTime(),hasta.getTime());

        Collection<CalendarEvent> rfcEv = new ArrayList();

        for(Rfc rfc : rfcs) {

            final Calendar fInicioDesarrollo = getComparableDate(rfc.getfInicioDesarrollo());
            final Calendar fFinDesarrollo = getComparableDate(rfc.getfFinDesarrollo());
            final Calendar fInicioCalidad = getComparableDate(rfc.getfInicioCalidad());
            final Calendar fFinCalidad = getComparableDate(rfc.getfFinCalidad());
            final Calendar fPasoProd = getComparableDate(rfc.getfPasoProd());

            Calendar fMax = null;
            if(fFinDesarrollo != null) fMax = getComparableDate(fFinDesarrollo.getTime());
            if(fFinCalidad != null) fMax = getComparableDate(fFinCalidad.getTime());
            if(fPasoProd != null) fMax = getComparableDate(fPasoProd.getTime());

            generaEventos(rfc,null,fInicioDesarrollo,fFinDesarrollo,rfcEv,fMax);
            generaEventos(rfc,"calendar-calidad",fInicioCalidad,fFinCalidad,rfcEv,fMax);
            generaEventos(rfc,"calendar-produccion",fPasoProd,fPasoProd,rfcEv,fMax);
        }

        return rfcEv;
    }

    public Rfc rfc(String key) {
        return rfcRepository.findByIssuekey(key);
    }

    void generaEventos(Rfc rfc,String className,Calendar desde,Calendar hasta
            ,Collection<CalendarEvent> events,Calendar fechaMax) {

        if(desde == null || hasta == null) return;

        final Calendar hoy = getComparableDate(new Date());

        Calendar fecha = new GregorianCalendar();
        fecha.setTime(desde.getTime());

        String title = rfc.getIssuekey() + " - "+rfc.getSummary();

        if(hoy.after(fechaMax)) title = "(V)"+title;

        while(!fecha.after(hasta)) {
            events.add(new CalendarEvent(title
                    ,fecha.getTime(),new String[]{className},rfc));
            fecha.add(Calendar.DAY_OF_MONTH,1);
        }

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
