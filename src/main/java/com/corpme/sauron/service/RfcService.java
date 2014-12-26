package com.corpme.sauron.service;

import com.corpme.sauron.config.ApplicationException;
import com.corpme.sauron.domain.*;
import com.corpme.sauron.service.bean.AnomaliaRfc;
import com.corpme.sauron.service.bean.CalendarEvent;
import com.google.common.collect.Lists;
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

    public Map<String,Collection> resumenRfcs() {
        final DateFormat df = new SimpleDateFormat("EEEE dd/MM/yyyy");

        final Calendar hoy = getComparableDate(new Date());

        Iterable<Rfc> rfcs = rfcRepository.findRfcsEnCurso();

        Collection<Rfc> pendientes = new ArrayList();
        Collection<Rfc> paradas = new ArrayList();
        Collection<AnomaliaRfc> anomalias = new ArrayList();
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

            if(rfc.getStatus().getId() != StatusKey.OPEN.getValue() && rfc.getStatus().getId() != StatusKey.DETENIDA.getValue()
                    && rfc.getIssuelinks().size() == 0) {
                anomalias.add(new AnomaliaRfc(rfc,"La fecha de fin de desarrollo es mayor que la fecha de inicio"));
            }
            else if(rfc.getStatus().getId() == StatusKey.OPEN.getValue()){
                pendientes.add(rfc);
            }
            else if(rfc.getStatus().getId() == StatusKey.DETENIDA.getValue()){
                paradas.add(rfc);
            }
            else if(rfc.getStatus().getId() == StatusKey.DESARROLLANDO.getValue()
                    || rfc.getStatus().getId() == StatusKey.RESOLVED.getValue()){
                if(fInicioDesarrollo == null || fFinDesarrollo == null) {
                    anomalias.add(new AnomaliaRfc(rfc,"El desarrollo está en curso y no tiene fechas de inicio " +
                            "y/o fin de planificación desarrollo"));
                }
                else if(fInicioDesarrollo.after(fFinDesarrollo)) {
                    anomalias.add(new AnomaliaRfc(rfc,"La fecha de fin de desarrollo es mayor que la fecha de inicio"));
                }
                else {
                    if(hoy.after(fFinDesarrollo)) vencidas.add(rfc);
                    else encurso.add(rfc);
                }
            }
            else if(rfc.getStatus().getId() == StatusKey.DISPONIBLE_PARA_PRUEBAS.getValue()
                    || rfc.getStatus().getId() == StatusKey.PROBANDO.getValue()
                    || rfc.getStatus().getId() == StatusKey.DETECTADO_ERROR_PRUEBAS.getValue()){

                if(fInicioDesarrollo == null || fFinDesarrollo == null) {
                    anomalias.add(new AnomaliaRfc(rfc,"Las pruebas están en curso  y no tiene fechas de inicio " +
                            "y/o fin de planificación desarrollo"));
                }
                else if(fInicioDesarrollo.after(fFinDesarrollo)) {
                    anomalias.add(new AnomaliaRfc(rfc,"La fecha de fin de desarrollo es mayor que la fecha de inicio"));
                }
                else if(fInicioCalidad == null || fFinCalidad == null) {
                    anomalias.add(new AnomaliaRfc(rfc,"Las pruebas están en curso y no tiene fechas de inicio y/o " +
                            "fin de planificación de pruebas"));
                }
                else if(fInicioCalidad.after(fFinCalidad)) {
                    anomalias.add(new AnomaliaRfc(rfc,"La fechas de fin de pruebas es mayor que la fecha de inicio"));
                }
                else {
                    if(fInicioDesarrollo == null || fFinDesarrollo == null) {
                        anomalias.add(new AnomaliaRfc(rfc,"El desarrollo está finalizado y no tiene fechas de inicio " +
                                "y/o fin de planificación desarrollo"));
                    }
                    else if(fInicioDesarrollo.after(fFinDesarrollo)) {
                        anomalias.add(new AnomaliaRfc(rfc,"La fecha de fin de desarrollo es mayor que la fecha de inicio"));
                    }
                    else if(fInicioCalidad == null || fFinCalidad == null) {
                        anomalias.add(new AnomaliaRfc(rfc,"El desarrollo está finalizado y y no tiene fechas de inicio y/o " +
                                "fin de planificación de pruebas"));
                    }
                    else if(fInicioCalidad.after(fFinCalidad)) {
                        anomalias.add(new AnomaliaRfc(rfc,"La fechas de fin de pruebas es mayor que la fecha de inicio"));
                    }
                    else if(hoy.after(fFinCalidad)) vencidas.add(rfc);
                    else encurso.add(rfc);
                }
            }
            else if(rfc.getStatus().getId() == StatusKey.FINALIZADA.getValue()){
                if(fPasoProd == null) {
                    anomalias.add(new AnomaliaRfc(rfc," El desarrollo está finalizado " +
                            "y no tiene fecha de paso a producción"));
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

        if(rfc.getStatus().getId() == StatusKey.EN_PRODUCCION.getValue()
                || rfc.getStatus().getId() == StatusKey.CERRADA.getValue()) {
            title = "(P)"+title;
        }

        while(!fecha.after(hasta)) {
            events.add(new CalendarEvent(title
                    ,fecha.getTime(),new String[]{className},rfc));
            fecha.add(Calendar.DAY_OF_MONTH,1);
        }

    }

    /**
     * Helper que devuelve una fecha son las HH:MM:SS.SSS a cero
     * @param fecha
     * @return
     */
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

    /**
     * Devuelve las Rfcs en curso agrupadas en los siguientes grupos:
     * - Pendientes
     * - Paradas
     * - En desarrollo
     * - En pruebas
     * - Finalizadas
     * - En producción (Los últimos dos meses)
     * @return
     */
    public Map<String,Collection<Rfc>> informeRfcsEnCurso() {

        final DateFormat df = new SimpleDateFormat("EEEE dd/MM/yyyy");

        final Calendar hoy = getComparableDate(new Date());

        Iterable<Rfc> rfcs = rfcRepository.findRfcsEnCurso();

        Collection<Rfc> pendientes = new ArrayList();
        Collection<Rfc> paradas = new ArrayList();
        Collection<Rfc> desarrollo = new ArrayList();
        Collection<Rfc> pruebas = new ArrayList();
        Collection<Rfc> finalizadas = new ArrayList();
        Collection<Rfc> produccion = new ArrayList();


        LinkedHashMap<String,Collection<Rfc>> result = new LinkedHashMap();
        result.put("Pendientes",pendientes);
        result.put("Paradas",paradas);
        result.put("En desarrollo",desarrollo);
        result.put("En pruebas",pruebas);
        result.put("Finalizadas",finalizadas);
        result.put("En produccion (Últimos 60 días)",produccion);

        for(Rfc rfc : rfcs) {

            calculaPorcentajeCompletado(rfc);

            if(rfc.getStatus().getId() == StatusKey.OPEN.getValue()){
                pendientes.add(rfc);
            }
            else if(rfc.getStatus().getId() == StatusKey.DETENIDA.getValue()){
                paradas.add(rfc);
            }
            else if(rfc.getStatus().getId() == StatusKey.DESARROLLANDO.getValue()
                    || rfc.getStatus().getId() == StatusKey.RESOLVED.getValue()){
                desarrollo.add(rfc);
            }
            else if(rfc.getStatus().getId() == StatusKey.DISPONIBLE_PARA_PRUEBAS.getValue()
                    || rfc.getStatus().getId() == StatusKey.PROBANDO.getValue()
                    || rfc.getStatus().getId() == StatusKey.DETECTADO_ERROR_PRUEBAS.getValue()){
                pruebas.add(rfc);
            }
            else if(rfc.getStatus().getId() == StatusKey.FINALIZADA.getValue()){
                finalizadas.add(rfc);
            }
        }

        Calendar desde = getComparableDate(new Date());
        Calendar hasta = getComparableDate(new Date());
        desde.add(Calendar.DAY_OF_MONTH, -60);

        rfcs = rfcRepository.findRfcsEnProduccionByDate(desde.getTime(),hasta.getTime());

        finalizadas.addAll(Lists.newArrayList(rfcs));

        return result;
    }

    /**
     * Helper que calcula en la variable @Trasient porcentajeCompletado de la clase Rfc
     * @param rfc
     */
    void calculaPorcentajeCompletado(Rfc rfc) {

        int total = rfc.getIssuelinks().size();
        Double tareas = 0.0;

        if(total == 0) {
            rfc.setPorcentajeCompletado(0);
            return;
        }
        for (RfcIssueLink rfcIssueLink : rfc.getIssuelinks()) {
            Issue issue = rfcIssueLink.getIssue();
            if(issue.getStatus().getId() == StatusKey.DESARROLLANDO.getValue()) {
                tareas += 0.2;
            }
            else if(issue.getStatus().getId() == StatusKey.RESOLVED.getValue()){
                tareas += 0.35;
            }
            else if(issue.getStatus().getId() == StatusKey.DISPONIBLE_PARA_PRUEBAS.getValue()) {
                tareas += 0.5;
            }
            else if(issue.getStatus().getId() == StatusKey.PROBANDO.getValue()) {
                tareas += 0.7;
            }
            else if(issue.getStatus().getId() == StatusKey.DETECTADO_ERROR_PRUEBAS.getValue()){
                tareas += 0.5;
            }
            else if(issue.getStatus().getId() == StatusKey.FINALIZADA.getValue()
                    || issue.getStatus().getId() == StatusKey.CERRADA.getValue()
                    || issue.getStatus().getId() == StatusKey.EN_PRODUCCION.getValue()){
                tareas += 1.0;
            }
            // El resto las considero pendientes (Abierta, Detenida...)
            else {
                tareas += 0.0;
            }
        }

        Double perc = Math.floor((tareas * 100) / total);

        rfc.setPorcentajeCompletado(perc.intValue());
    }

}
