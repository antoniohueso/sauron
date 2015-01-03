package com.corpme.sauron.service;

import com.corpme.sauron.Application;
import com.corpme.sauron.domain.*;
import com.corpme.sauron.service.bean.AnomaliaRfc;
import com.corpme.sauron.service.bean.CalendarEvent;
import com.google.common.collect.Lists;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
@Transactional
public class ResumenRfcsStepdefs {

    @Autowired
    RfcService rfcService;

    @Autowired
    RfcRepository rfcRepository;

    @Autowired
    RfcIssueLinkRepository rfcIssueLinkRepository;

    Map<String,Collection> resumen;

    Rfc rfc = null;

    final Logger logger = LoggerFactory.getLogger(getClass());

    final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    @Given("^no hay rfcs creadas$")
    public void no_hay_rfcs_creadas() throws Throwable {
        rfcIssueLinkRepository.deleteAll();
        rfcRepository.deleteAll();
    }

    @Given("^una rfc con fecha de desarrollo y tareas asociadas en estado \"([^\"]*)\"$")
    public void una_rfc_con_fecha_de_desarrollo_y_tareas_asociadads_en_estado(String estado) throws Throwable {
        rfc = creaRfcWithEstadoAndAsocioUnaTarea(StatusKey.valueOf(estado));
        rfc.setfInicioDesarrollo(new Date());
        rfc.setfFinDesarrollo(new Date());
    }

    @Given("^una rfc con fechas de desarrollo, calidad y tareas asociadas en estado \"([^\"]*)\"$")
    public void una_rfc_con_fechas_de_desarrollo_calidad_y_tareas_asociadas_en_estado(String estado) throws Throwable {
        rfc = creaRfcWithEstadoAndAsocioUnaTarea(StatusKey.valueOf(estado));
        rfc.setfInicioDesarrollo(new Date());
        rfc.setfFinDesarrollo(new Date());
        rfc.setfInicioCalidad(new Date());
        rfc.setfFinCalidad(new Date());
    }

    @Given("^una rfc con fechas de desarrollo, calidad y tareas asociadas y fecha de paso a producción en estado \"([^\"]*)\"$")
    public void una_rfc_con_fechas_de_desarrollo_calidad_y_tareas_asociadas_y_fecha_de_paso_a_producción_en_estado(String estado) throws Throwable {
        rfc = creaRfcWithEstadoAndAsocioUnaTarea(StatusKey.valueOf(estado));
        rfc.setfInicioDesarrollo(new Date());
        rfc.setfFinDesarrollo(new Date());
        rfc.setfInicioCalidad(new Date());
        rfc.setfFinCalidad(new Date());
        rfc.setfPasoProd(new Date());
    }

    @Given("^creo una rfc en estado \"([^\"]*)\"$")
    public void creo_una_rfc_en_estado(String estado) throws Throwable {
        rfc = creaRfcWithEstado(StatusKey.valueOf(estado));
    }


    @Given("^creo una rfc sin links en estado \"([^\"]*)\"$")
    public void creo_una_rfc_din_links_en_estado(String estado) throws Throwable {
        rfc = creaRfcWithEstado(StatusKey.valueOf(estado));
        rfc.setfInicioDesarrollo(new Date());
        rfc.setfFinDesarrollo(new Date());
        rfc.setfInicioCalidad(new Date());
        rfc.setfFinCalidad(new Date());
        rfc.setfPasoProd(new Date());
    }

    @Given("^creo una rfc sin fecha de planificación de calidad en estado \"([^\"]*)\"$")
    public void creo_una_rfc_sin_fecha_de_planificación_de_calidad_en_estado(String estado) throws Throwable {
        rfc = creaRfcWithEstadoAndAsocioUnaTarea(StatusKey.valueOf(estado));
        rfc.setfInicioDesarrollo(new Date());
        rfc.setfFinDesarrollo(new Date());
        rfc.setfPasoProd(new Date());
    }

    @Given("^creo una rfc sin fecha de planificación de desarrollo en estado \"([^\"]*)\"$")
    public void creo_una_rfc_sin_fecha_de_planificación_de_desarrollo_en_estado(String estado) throws Throwable {
        rfc = creaRfcWithEstadoAndAsocioUnaTarea(StatusKey.valueOf(estado));
        rfc.setfInicioCalidad(new Date());
        rfc.setfFinCalidad(new Date());
        rfc.setfPasoProd(new Date());
    }

    @Given("^creo una rfc sin fecha de planificación de paso a producción en estado \"([^\"]*)\"$")
    public void creo_una_rfc_sin_fecha_de_planificación_de_paso_a_producción_en_estado(String estado) throws Throwable {
        rfc = creaRfcWithEstadoAndAsocioUnaTarea(StatusKey.valueOf(estado));
        rfc.setfInicioDesarrollo(new Date());
        rfc.setfFinDesarrollo(new Date());
        rfc.setfInicioCalidad(new Date());
        rfc.setfFinCalidad(new Date());
    }

    @Given("^creo una rfc con fecha de planificación inicio mayor que la de fin en estado \"([^\"]*)\"$")
    public void creo_una_rfc_con_fecha_de_planificación_inicio_mayor_que_la_de_fin_en_estado(String estado) throws Throwable {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH, -1);

        StatusKey statusKey = StatusKey.valueOf(estado);

        rfc = creaRfcWithEstadoAndAsocioUnaTarea(statusKey);
        rfc.setfInicioDesarrollo(new Date());
        rfc.setfFinDesarrollo(new Date());
        rfc.setfInicioCalidad(new Date());
        rfc.setfFinCalidad(new Date());

        if(statusKey.equals(StatusKey.DESARROLLANDO)) {
            rfc.setfFinDesarrollo(calendar.getTime());
        }
        else {
            rfc.setfFinCalidad(calendar.getTime());
        }

    }

    @Given("^creo una rfc con fecha de planificación vencida en estado \"([^\"]*)\"$")
    public void creo_una_rfc_con_fecha_de_planificación_vencida_en_estado(String estado) throws Throwable {
        Calendar inicio = new GregorianCalendar();
        inicio.add(Calendar.MONTH, -1);

        Calendar fin = new GregorianCalendar();
        fin.add(Calendar.DAY_OF_MONTH, -15);

        StatusKey statusKey = StatusKey.valueOf(estado);

        rfc = creaRfcWithEstadoAndAsocioUnaTarea(statusKey);

        if(statusKey.equals(StatusKey.DESARROLLANDO)) {
            rfc.setfInicioDesarrollo(inicio.getTime());
            rfc.setfFinDesarrollo(fin.getTime());
        }
        else if(statusKey.equals(StatusKey.FINALIZADA)) {
            rfc.setfInicioDesarrollo(new Date());
            rfc.setfFinDesarrollo(new Date());
            rfc.setfInicioCalidad(new Date());
            rfc.setfFinCalidad(new Date());
            rfc.setfPasoProd(fin.getTime());
        }
        else {
            rfc.setfInicioDesarrollo(new Date());
            rfc.setfFinDesarrollo(new Date());
            rfc.setfInicioCalidad(inicio.getTime());
            rfc.setfFinCalidad(fin.getTime());
        }

        rfcRepository.save(rfc);
    }


    @When("^consulto el resumen de rfcs$")
    public void consulto_el_resumen_de_rfcs() throws Throwable {
       resumen = rfcService.resumenRfcs();
    }

    @Then("^debe devolver (\\d+) colecciones con un total de (\\d+) items$")
    public void debe_devolver_colecciones_con_un_total_de_items(int totalCollections, int totalItems) throws Throwable {

        assertEquals(totalCollections, resumen.size());

        for(String key : resumen.keySet()) {
            Collection c = resumen.get(key);
            //logger.info(key + " " + c.size());
            assertEquals(totalItems, c.size());
        }
    }


    @Then("^debe devolver (\\d+) colecciones con un total de (\\d+) items en todas excepto en la colección \"([^\"]*)\"" +
            " que devolverá (\\d+) item$")
    public void debe_devolver_colecciones_con_un_total_de_items_en_todas_excepto_en_la_colección(int totalCollections
            , int totalItems, String key,int itemsEsperados) throws Throwable {
        checkResumenRfcsCollection(key,totalCollections,totalItems,itemsEsperados);
    }

    @Then("^debe devolver (\\d+) colecciones con un total de (\\d+) items en todas excepto en la colección \"([^\"]*)\" que devolverá (\\d+) items$")
    public void debe_devolver_colecciones_con_un_total_de_items_en_todas_excepto_en_la_colección_que_devolverá_items(int totalCollections
            , int totalItems, String key,int itemsEsperados) throws Throwable {
        checkResumenRfcsCollection(key,totalCollections,totalItems,itemsEsperados,false);
    }

    Rfc creaRfcWithEstado(StatusKey estado) {

        Random r = new Random();

        long id = r.nextInt(1000)+1;
        if(id < 0) id *= -1;

        Rfc rfc = new Rfc();
        rfc.setId(id);
        rfc.setIssuekey("RFC-"+id);
        rfc.setSummary(String.format("Test RFC-%d con estado: %d",id,estado.getValue()));
        Status status = new Status();
        status.setId(estado.getValue());
        rfc.setStatus(status);

        Priority priority = new Priority();
        priority.setId(3L);
        rfc.setPriority(priority);

        return rfcRepository.save(rfc);
    }

    void checkResumenRfcsCollection(String key,int totalCollections,int totalItemsCollections,int esperado)
            throws Throwable {
        checkResumenRfcsCollection(key,totalCollections,totalItemsCollections,esperado,true);
    }

    void checkResumenRfcsCollection(String key,int totalCollections,int totalItemsCollections,int esperado
            ,boolean checkRfc) throws Throwable {

        consulto_el_resumen_de_rfcs();

        assertEquals("El resumen ha de devolver "+totalCollections+" colecciones", totalCollections ,resumen.size());

        String [] colls = new String [] {"pendientes","paradas","encurso","vencidas","anomalias"};

        Collection c = null;

        for(String collKey : colls) {

            if(collKey.equals(key)) continue;;

            c = resumen.get(collKey);
            assertEquals("La colección "+collKey+ " ha de tener "+totalItemsCollections+" items"
                    ,totalItemsCollections,c.size());
        }

        c = resumen.get(key);
        assertEquals("La colección "+key+ " ha de tener "+esperado+" item", esperado, c.size());

        if(checkRfc) {

            Object data = c.toArray()[0];
            if (data instanceof AnomaliaRfc) {
                AnomaliaRfc anomaliaRfc = (AnomaliaRfc) data;
                assertEquals("La rfc de la colección " + key + " ha de ser la misma que acabamos de crear"
                        , anomaliaRfc.getRfc().getId(), rfc.getId());
            } else {
                Rfc rfc = (Rfc) data;
                assertEquals("La rfc de la colección " + key + " ha de ser la misma que acabamos de crear"
                        , rfc.getId(), rfc.getId());
            }
        }


    }


    Rfc creaRfcWithEstadoAndAsocioUnaTarea(StatusKey estado) {
        Rfc rfc = creaRfcWithEstado(estado);

        Random random = new Random();

        long id = random.nextInt(1000)+1;

        RfcIssueLink ln = new RfcIssueLink();
        ln.setId(id);
        Issue issue = new Issue();
        issue.setId(28949L);
        ln.setIssue(issue);
        ln.setRfc(rfc);
        rfcIssueLinkRepository.save(ln);
        rfc.getIssuelinks().add(rfcIssueLinkRepository.findOne(ln.getId()));

        return rfc;
    }

    Date getSafeDate(String strfecha) {
        if(strfecha == null || strfecha.trim().length() == 0) return null;
        try {
            return df.parse(strfecha);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Given("^Creo rfcs con el siguiente juego de datos:$")
    public void Creo_rfcs_con_el_siguiente_juego_de_datos(DataTable tableRfcs) throws Throwable {
        for(List<String> row : tableRfcs.cells(0)) {
            Rfc rfc = creaRfcWithEstadoAndAsocioUnaTarea(StatusKey.valueOf(row.get(6)));

            rfc.setIssuekey("RFC-"+row.get(0));
            rfc.setfInicioDesarrollo(getSafeDate(row.get(1)));
            rfc.setfFinDesarrollo(getSafeDate(row.get(2)));
            rfc.setfInicioCalidad(getSafeDate(row.get(3)));
            rfc.setfFinCalidad(getSafeDate(row.get(4)));
            rfc.setfPasoProd(getSafeDate(row.get(5)));
            logger.info("Crea: " + row.get(0) + " " + row.get(1));
        }
    }

    @When("^realizo la consulta de los eventos rfcs$")
    public void realizo_la_consulta_de_los_eventos_rfcs() throws Throwable {
        Collection<CalendarEvent> events = Lists.newArrayList(rfcService.rfcsEvents(df.parse("01/07/2014"), df.parse("31/07/2014")));
        assertEquals(0,events.size());
    }
}
