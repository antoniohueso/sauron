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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            calculaPorcentajeCompletado(rfc);

            final Calendar fInicioDesarrollo = getComparableDate(rfc.getfInicioDesarrollo());
            final Calendar fFinDesarrollo = getComparableDate(rfc.getfFinDesarrollo());
            final Calendar fInicioCalidad = getComparableDate(rfc.getfInicioCalidad());
            final Calendar fFinCalidad = getComparableDate(rfc.getfFinCalidad());
            final Calendar fPasoProd = getComparableDate(rfc.getfPasoProd());

            if(rfc.getStatus().getId() != StatusKey.OPEN.getValue() && rfc.getStatus().getId() != StatusKey.DETENIDA.getValue()
                    && rfc.getIssuelinks().size() == 0) {
                anomalias.add(new AnomaliaRfc(rfc,"La rfc no tiene issues asociadas"));
            }
            else if(rfc.getStatus().getId() == StatusKey.OPEN.getValue()){
                pendientes.add(rfc);
            }
            else if(rfc.getStatus().getId() == StatusKey.DETENIDA.getValue()){
                paradas.add(rfc);
            }
            else if(rfc.getStatus().getId() == StatusKey.DESARROLLANDO.getValue()
                    || rfc.getStatus().getId() == StatusKey.RESOLVED.getValue()){

                String anomalia = null;

                if(fInicioDesarrollo == null || fFinDesarrollo == null) {
                    anomalia = "El desarrollo está en curso y no tiene fechas de inicio " +
                            "y/o fin de planificación desarrollo";
                }
                else if(fInicioDesarrollo.after(fFinDesarrollo)) {
                    anomalia = "La fecha de fin de desarrollo es mayor que la fecha de inicio";
                }

                if(anomalia != null) {
                    anomalias.add(new AnomaliaRfc(rfc,anomalia));
                }
                else {
                    if(hoy.after(fFinDesarrollo)) {
                        vencidas.add(rfc);
                    }
                    else {
                        encurso.add(rfc);
                    }
                }
            }
            else if(rfc.getStatus().getId() == StatusKey.DISPONIBLE_PARA_PRUEBAS.getValue()
                    || rfc.getStatus().getId() == StatusKey.PROBANDO.getValue()
                    || rfc.getStatus().getId() == StatusKey.DETECTADO_ERROR_PRUEBAS.getValue()){


                String anomalia = null;

                if(fInicioDesarrollo == null || fFinDesarrollo == null) {
                    anomalia = anomalia!=null?", ":"" + "Las pruebas están en curso  y no tiene fechas de inicio " +
                            "y/o fin de planificación desarrollo";
                }
                else if(fInicioDesarrollo.after(fFinDesarrollo)) {
                    anomalia = anomalia!=null?", ":"" + "La fecha de fin de desarrollo es mayor que la fecha de inicio";
                }

                if(fInicioCalidad == null || fFinCalidad == null) {
                    anomalia = anomalia!=null?", ":"" + "Las pruebas están en curso y no tiene fechas de inicio y/o " +
                            "fin de planificación de pruebas";
                }
                else if(fInicioCalidad.after(fFinCalidad)) {
                    anomalia = anomalia!=null?", ":"" + "La fechas de fin de pruebas es mayor que la fecha de inicio";
                }

                if(anomalia != null) {
                    anomalias.add(new AnomaliaRfc(rfc,anomalia));
                }
                else {
                    if(hoy.after(fFinCalidad)) {
                        vencidas.add(rfc);
                    }
                    else {
                        encurso.add(rfc);
                    }
                }

            }
            else if(rfc.getStatus().getId() == StatusKey.FINALIZADA.getValue()){

                String anomalia = null;

                if(fInicioDesarrollo == null || fFinDesarrollo == null) {
                    anomalia = "El desarrollo está finalizado y no tiene fechas de inicio " +
                            "y/o fin de planificación desarrollo";
                }
                else if(fInicioDesarrollo.after(fFinDesarrollo)) {
                    anomalia = "La fecha de fin de desarrollo es mayor que la fecha de inicio";
                }

                if(fInicioCalidad == null || fFinCalidad == null) {
                    anomalia = anomalia!=null?", ":"" + "El desarrollo está finalizado y y no tiene fechas de inicio y/o " +
                            "fin de planificación de pruebas";
                }
                else if(fInicioCalidad.after(fFinCalidad)) {
                    anomalia = anomalia!=null?", ":"" + "La fechas de fin de pruebas es mayor que la fecha de inicio";
                }

                if(fPasoProd == null) {
                    anomalia = anomalia!=null?", ":"" + "El desarrollo está finalizado " +
                            "y no tiene fecha de paso a producción";
                }

                if(anomalia != null) {
                    anomalias.add(new AnomaliaRfc(rfc,anomalia));
                }
                else {
                    if(hoy.after(fPasoProd)) {
                        vencidas.add(rfc);
                    }
                    else {
                        encurso.add(rfc);
                    }
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

            calculaPorcentajeCompletado(rfc);

            final Calendar fInicioDesarrollo = getComparableDate(rfc.getfInicioDesarrollo());
            final Calendar fFinDesarrollo = getComparableDate(rfc.getfFinDesarrollo());
            final Calendar fInicioCalidad = getComparableDate(rfc.getfInicioCalidad());
            final Calendar fFinCalidad = getComparableDate(rfc.getfFinCalidad());
            final Calendar fPasoProd = getComparableDate(rfc.getfPasoProd());

            Calendar fMax = null;
            if(fFinDesarrollo != null) fMax = getComparableDate(fFinDesarrollo.getTime());
            if(fFinCalidad != null) fMax = getComparableDate(fFinCalidad.getTime());
            if(fPasoProd != null) fMax = getComparableDate(fPasoProd.getTime());

            generaEventos(rfc,"calendar-normal",fInicioDesarrollo,fFinDesarrollo,rfcEv,fMax);
            generaEventos(rfc,"calendar-calidad",fInicioCalidad,fFinCalidad,rfcEv,fMax);
            generaEventos(rfc,"calendar-produccion",fPasoProd,fPasoProd,rfcEv,fMax);
        }

        return rfcEv;
    }

    public Rfc rfc(String key) {
        Rfc rfc = rfcRepository.findByIssuekey(key);
        if (rfc == null) {
            throw new ApplicationException("La RFC " + key + " no existe");
        }

        calculaPorcentajeCompletado(rfc);

        rfc.setDescription(stringToHtml(rfc.getDescription()));
        rfc.setPlanpasoprod(stringToHtml(rfc.getPlanpasoprod()));
        rfc.setPlanmarchaatras(stringToHtml(rfc.getPlanmarchaatras()));
        rfc.setSolucion(stringToHtml(rfc.getSolucion()));
        rfc.setTablasAfectadas(stringToHtml(rfc.getTablasAfectadas()));
        rfc.setAcuerdoFuncional(stringToHtml(rfc.getAcuerdoFuncional()));
        rfc.setCausaDetencion(stringToHtml(rfc.getCausaDetencion()));
        rfc.setObservaciones(stringToHtml(rfc.getObservaciones()));
        rfc.setPlanpruebas(stringToHtml(rfc.getPlanpruebas()));
        rfc.setPlanPruebasValidacion(stringToHtml(rfc.getPlanPruebasValidacion()));



        return rfc;
    }

    String stringToHtml(String cad) {
        if(cad == null) return null;

        Pattern pattern = Pattern.compile("\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]");
        Matcher m = pattern.matcher(cad);
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            String found = m.group(0);
            m.appendReplacement(sb,"<a target=\"_blank\" href=\"" + found + "\">" + found + "</a>");
        }

        m.appendTail(sb);

        return sb.toString().replaceAll("(\r\n|\n)", "<br/>");

    }



    void generaEventos(Rfc rfc,String className,Calendar desde,Calendar hasta
            ,Collection<CalendarEvent> events,Calendar fechaMax) {

        if(desde == null || hasta == null) return;

        boolean vencida = false;

        final Calendar hoy = getComparableDate(new Date());

        final Calendar fecha = new GregorianCalendar();
        fecha.setTime(desde.getTime());

        String comment = null;

        String title = rfc.getIssuekey() + " - "+rfc.getSummary();

        if(hoy.after(fechaMax)) {
            comment = "Rfc vencida";
            title = "(V)"+title;
            vencida = true;
        }

        if(rfc.getStatus().getId() == StatusKey.EN_PRODUCCION.getValue()
                || rfc.getStatus().getId() == StatusKey.CERRADA.getValue()) {
            title = "(P)"+title;
        }

        while(!fecha.after(hasta)) {
            CalendarEvent calendarEvent = new CalendarEvent(title
                    ,fecha.getTime(),className,rfc);
            if(vencida) {
                calendarEvent.setAlerta("Vencida");
            }
            events.add(calendarEvent);

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

        if(rfc.getIssuelinks() == null) return;

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
