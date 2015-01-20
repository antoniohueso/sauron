package com.corpme.sauron.service;

import com.corpme.sauron.config.ApplicationException;
import com.corpme.sauron.domain.*;
import com.corpme.sauron.service.bean.AnomaliaRfc;
import com.corpme.sauron.service.bean.CalendarEvent;
import com.corpme.sauron.service.bean.CalendarEventType;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Autowired
    UtilsService utilsService;

    final Logger logger = LoggerFactory.getLogger(getClass());

    public Map<String,Collection> resumenRfcs() {
        final DateFormat df = new SimpleDateFormat("EEEE dd/MM/yyyy");


        final Collection<Rfc> pendientes = new ArrayList();
        final Collection<Rfc> paradas = new ArrayList();
        final Collection<AnomaliaRfc> anomalias = new ArrayList();
        final Collection<Rfc> vencidas = new ArrayList();
        final Collection<Rfc> encurso = new ArrayList();

        final Map<String,Collection> result = new HashMap();
        result.put("encurso",encurso);
        result.put("anomalias",anomalias);
        result.put("vencidas",vencidas);
        result.put("pendientes",pendientes);
        result.put("paradas",paradas);

        final Iterable<Rfc> rfcs = rfcRepository.findRfcsEnCurso();

        rfcs.forEach((rfc) -> {
            String anomalia = valid(rfc);
            if(anomalia != null) {
                anomalias.add(new AnomaliaRfc(rfc,anomalia));
            }
            else {
                if(isPendienteStatus(rfc.getStatus())) pendientes.add(rfc);
                else if(isDetenidaStatus(rfc.getStatus())) paradas.add(rfc);
                else if(isDesarrolloStatus(rfc.getStatus())) {
                    if(isVencida(rfc.getfFinDesarrollo())) {
                        vencidas.add(rfc);
                    }
                    else {
                        encurso.add(rfc);
                    }
                }
                else if(isCalidadStatus(rfc.getStatus())) {
                    if(isVencida(rfc.getfFinCalidad())) {
                        vencidas.add(rfc);
                    }
                    else {
                        encurso.add(rfc);
                    }
                }
                else if(isFinalizadaStatus(rfc.getStatus())) {
                    if(isVencida(rfc.getfPasoProd())) {
                        vencidas.add(rfc);
                    }
                    else {
                        encurso.add(rfc);
                    }
                }
            }
        });

        return result;
    }

    public boolean isVencida(final Date fecha) {
        return utilsService.getComparableDate(new Date()).after(utilsService.getComparableDate(fecha));
    }

    public boolean isCalidadStatus(final Status status) {
        return status.getId() == StatusKey.DISPONIBLE_PARA_PRUEBAS.getValue()
                || status.getId() == StatusKey.PROBANDO.getValue()
                || status.getId() == StatusKey.DETECTADO_ERROR_PRUEBAS.getValue();
    }

    public boolean isCerradaStatus(final Status status) {
        return status.getId() == StatusKey.CERRADA.getValue()
                || status.getId() == StatusKey.EN_PRODUCCION.getValue();
    }

    public boolean isFinalizadaStatus(final Status status) {
        return status.getId() == StatusKey.FINALIZADA.getValue();
    }

    public boolean isDesarrolloStatus(final Status status) {
        return status.getId() == StatusKey.DESARROLLANDO.getValue()
                || status.getId() == StatusKey.RESOLVED.getValue();
    }

    public boolean isPendienteStatus(final Status status) {
        return status.getId() == StatusKey.OPEN.getValue();
    }

    public boolean isDetenidaStatus(final Status status) {
        return status.getId() == StatusKey.DETENIDA.getValue();
    }

    /**
     * Comprueba si una Rfc es válida y tiene los campos necesarios según el estado
     * @param rfc
     * @return
     */
    public String valid(final Rfc rfc) {

        final Collection<String> anomalias = new ArrayList();

        final Calendar fInicioDesarrollo = utilsService.getComparableDate(rfc.getfInicioDesarrollo());
        final Calendar fFinDesarrollo = utilsService.getComparableDate(rfc.getfFinDesarrollo());
        final Calendar fInicioCalidad = utilsService.getComparableDate(rfc.getfInicioCalidad());
        final Calendar fFinCalidad = utilsService.getComparableDate(rfc.getfFinCalidad());
        final Calendar fPasoProd = utilsService.getComparableDate(rfc.getfPasoProd());


        if(isPendienteStatus(rfc.getStatus())
                || isDetenidaStatus(rfc.getStatus())
                || isCerradaStatus(rfc.getStatus())) return null;

        if(rfc.getIssuelinks().size() == 0) {
            anomalias.add("La rfc no tiene tareas asociadas");
        }

        if(fInicioDesarrollo == null || fFinDesarrollo == null) {
            anomalias.add("La rfc no tiene fechas de inicio " +
                    "y/o fin de planificación desarrollo");
        }
        else if(fInicioDesarrollo.after(fFinDesarrollo)) {
            anomalias.add("La fecha de fin de desarrollo es mayor que la fecha de inicio");
        }

        if(isCalidadStatus(rfc.getStatus()) || isFinalizadaStatus(rfc.getStatus())) {
            if(fInicioCalidad == null || fFinCalidad == null){
                anomalias.add("La rfc no tiene fechas de inicio y/o fin de planificación de pruebas");
            }
            else if(fInicioCalidad.after(fFinCalidad)) {
                anomalias.add("La fechas de fin de pruebas es mayor que la fecha de inicio");
            }
        }

        if(isFinalizadaStatus(rfc.getStatus()) && fPasoProd == null) {
            anomalias.add("la rfc está finalizada y no tiene fecha de paso a producción");
        }

        if(anomalias.size() == 0) return null;

        return String.join(",",anomalias);
    }


    public Iterable<CalendarEvent> rfcsEvents(final Date fdesde,final Date fhasta,String filtro) {

        final DateFormat df = new SimpleDateFormat("EEEE dd/MM/yyyy");

        final Calendar hasta = utilsService.getComparableDate(fhasta);
        final Calendar desde = utilsService.getComparableDate(fdesde);

        if (desde.after(hasta)) {
            throw new ApplicationException("La fecha desde no puede ser mayor que la fecha hasta. F.Desde="
                    + df.format(desde.getTime()) + ", F.Hasta=" + df.format(hasta.getTime()));
        }

        final Iterable<Rfc> rfcs = rfcRepository.findRfcsByDate(desde.getTime(),hasta.getTime());
        final CalendarService calendarService = new CalendarService();
        final Calendar hoy = utilsService.getComparableDate(new Date());

        rfcs.forEach((rfc) -> {

            if(rfcContains(rfc,filtro)) {

                final Calendar fdueDate;
                if (rfc.getfPasoProd() != null) fdueDate = utilsService.getComparableDate(rfc.getfPasoProd());
                else if (rfc.getfFinCalidad() != null) fdueDate = utilsService.getComparableDate(rfc.getfFinCalidad());
                else if (rfc.getfFinDesarrollo() != null)
                    fdueDate = utilsService.getComparableDate(rfc.getfFinDesarrollo());
                else fdueDate = null;

                final boolean vencida = (fdueDate != null && hoy.after(fdueDate));

                final StringBuilder title = new StringBuilder(vencida ? "(V)" : "")
                        .append(rfc.getIssuekey()).append(" - ").append(rfc.getSummary());

                CalendarEvent ev = calendarService.addEvent(rfc.getfInicioDesarrollo(), rfc.getfFinDesarrollo(), title.toString(), CalendarEventType.INFO, rfc);
                if (ev!=null && vencida) ev.setAlerta("Vencida");

                ev = calendarService.addEvent(rfc.getfInicioCalidad(),rfc.getfFinCalidad(), title.toString(), CalendarEventType.WARNING, rfc);
                if (ev!=null && vencida) ev.setAlerta("Vencida");

                ev = calendarService.addEvent(rfc.getfPasoProd(),rfc.getfPasoProd(), title.toString(), CalendarEventType.SUCCESS, rfc);
                if (ev!=null && vencida) ev.setAlerta("Vencida");
            }

        });

        return calendarService.getEvents();
    }

    public boolean rfcContains(Rfc rfc,final String strfiltro) {
        if(strfiltro == null || strfiltro.trim().length() == 0) return true;

        final String filtro = strfiltro.toLowerCase();

        if (rfc.getIssuekey().toLowerCase().contains(filtro)) {
            return true;
        }

        if (rfc.getSummary().toLowerCase().contains(filtro)) {
            return true;
        }

        if (rfc.getEquipodesarrollo().stream().anyMatch(u->
            u.getUser().getDisplayName().toLowerCase().contains(filtro)
                    || u.getUser().getEmailAddress().toLowerCase().contains(filtro)
                    || u.getUser().getName().toLowerCase().contains(filtro)
        )) {
            return true;
        }

        if (rfc.getEquipocalidad().stream().anyMatch(u->
            u.getUser().getDisplayName().toLowerCase().contains(filtro)
                    || u.getUser().getEmailAddress().toLowerCase().contains(filtro)
                    || u.getUser().getName().toLowerCase().contains(filtro)
        )) {
            return true;
        }

        if (rfc.getAssignee().getDisplayName().toLowerCase().contains(filtro)
                || rfc.getAssignee().getEmailAddress().toLowerCase().contains(filtro)
                || rfc.getAssignee().getName().toLowerCase().contains(filtro)) {
            return true;
        }

        return false;
    }

    public Rfc rfc(final String key) {
        final Rfc rfc = rfcRepository.findByIssuekey(key);
        if (rfc == null) {
            throw new ApplicationException("La RFC " + key + " no existe");
        }

        rfc.setDescription(utilsService.parseUrlsToHtml(rfc.getDescription()));
        rfc.setPlanpasoprod(utilsService.parseUrlsToHtml(rfc.getPlanpasoprod()));
        rfc.setPlanmarchaatras(utilsService.parseUrlsToHtml(rfc.getPlanmarchaatras()));
        rfc.setSolucion(utilsService.parseUrlsToHtml(rfc.getSolucion()));
        rfc.setTablasAfectadas(utilsService.parseUrlsToHtml(rfc.getTablasAfectadas()));
        rfc.setAcuerdoFuncional(utilsService.parseUrlsToHtml(rfc.getAcuerdoFuncional()));
        rfc.setCausaDetencion(utilsService.parseUrlsToHtml(rfc.getCausaDetencion()));
        rfc.setObservaciones(utilsService.parseUrlsToHtml(rfc.getObservaciones()));
        rfc.setPlanpruebas(utilsService.parseUrlsToHtml(rfc.getPlanpruebas()));
        rfc.setPlanPruebasValidacion(utilsService.parseUrlsToHtml(rfc.getPlanPruebasValidacion()));



        return rfc;
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

        final Calendar hoy = utilsService.getComparableDate(new Date());

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

        Calendar desde = utilsService.getComparableDate(new Date());
        Calendar hasta = utilsService.getComparableDate(new Date());
        desde.add(Calendar.DAY_OF_MONTH, -60);

        rfcs = rfcRepository.findRfcsEnProduccionByDate(desde.getTime(),hasta.getTime());

        finalizadas.addAll(Lists.newArrayList(rfcs));

        return result;
    }



}
