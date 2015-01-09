package com.corpme.sauron.service;

import com.corpme.sauron.domain.*;
import com.corpme.sauron.service.bean.AnomaliaRfc;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.persistence.PostLoad;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Created by ahg on 04/01/15.
 */
public class ResumenRfcsStepdefs {

    @Mock
    RfcRepository rfcRepository;

    @InjectMocks
    RfcService rfcService;

    @Spy
    UtilsService utilsService;

    Logger logger = LoggerFactory.getLogger(getClass());

    TestRfcsHelper rfcsHelper;

    Map<String,Collection> resumen;

    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);
        rfcsHelper = new TestRfcsHelper();
        when(rfcRepository.findRfcsEnCurso()).thenReturn(rfcsHelper.getRfcsResult());
    }

    @Given("^Dos RFCs creadas una en estado CERRADA y otra en estado EN_PRODUCCION y ninguna creada en otro estado$")
    public void Dos_RFCs_creadas_una_en_estado_CERRADA_y_otra_en_estado_EN_PRODUCCION_y_ninguna_creada_en_otro_estado() throws Throwable {
        rfcsHelper.addRfcWithEstado(StatusKey.CERRADA);
        rfcsHelper.addRfcWithEstado(StatusKey.EN_PRODUCCION);
    }

    @When("^Consulto el resumen de rfcs$")
    public void Consulto_el_resumen_de_rfcs() throws Throwable {
        resumen = rfcService.resumenRfcs();
    }

    @Then("^No debe devolver ninguna RFC$")
    public void No_debe_devolver_ninguna_RFC() throws Throwable {
        resumen.forEach((key, coll) -> {
            assertThat("Las colección " + key + " ha de tener 0 items", coll.size(), is(0));
        });
    }


    @Given("^(\\d+) rfc creada en estado \"([^\"]*)\"$")
    public void rfc_creada_en_estado(int num, String estado) throws Throwable {

        for(int i = 0 ; i < num ; i++) {
            rfcsHelper.addRfcWithEstado(StatusKey.valueOf(estado));
        }

    }

    @Then("^Debe devolver (\\d+) rfc en la clasificación \"([^\"]*)\"$")
    public void Debe_devolver_rfc_en_la_clasificación(int esperado, String clasificacion) throws Throwable {

        Collection coll = resumen.get(clasificacion);
        assertThat(coll.size(),is(esperado));

        coll.forEach((o) -> {
            Rfc rfc;
            if(o instanceof AnomaliaRfc) rfc = ((AnomaliaRfc)o).getRfc();
            else rfc = (Rfc)o;
            assertThat(rfcsHelper.exist(rfc), equalTo(true));
        });

    }

    @Given("^(\\d+) rfc valida creada en estado \"([^\"]*)\"$")
    public void rfc_valida_creada_en_estado(int num, String estado) throws Throwable {

        StatusKey statusKey = StatusKey.valueOf(estado);

        for(int i = 0 ; i < num ; i++) {

            Rfc rfc = rfcsHelper.addRfcWithEstado(statusKey);
            if (statusKey.equals(StatusKey.DESARROLLANDO)) {
                rfcsHelper.planificaDesarrollo(new Date(), new Date(), rfc);
            } else if (statusKey.equals(StatusKey.FINALIZADA)) {
                rfcsHelper.planificaDesarrollo(new Date(), new Date(), rfc);
                rfcsHelper.planificaPruebas(new Date(), new Date(), rfc);
                rfcsHelper.planificaPasoProduccion(new Date(), rfc);
            } else {
                rfcsHelper.planificaDesarrollo(new Date(), new Date(), rfc);
                rfcsHelper.planificaPruebas(new Date(), new Date(), rfc);
            }
            rfcsHelper.asignaTarea(rfc);
        }

    }

    @Given("^(\\d+) rfc creada en estado \"([^\"]*)\" con tareas asociadas con una fecha no valida$")
    public void rfc_creada_en_estado_con_tareas_asociadas_con_una_fecha_no_valida(int num, String estado) throws Throwable {
        StatusKey statusKey = StatusKey.valueOf(estado);

        for(int i = 0 ; i < num ; i++) {

            Rfc rfc = rfcsHelper.addRfcWithEstado(statusKey);
            if (statusKey.equals(StatusKey.DESARROLLANDO)) {
                rfcsHelper.planificaDesarrollo(null, null, rfc);
            } else if (statusKey.equals(StatusKey.FINALIZADA)) {
                rfcsHelper.planificaDesarrollo(new Date(), new Date(), rfc);
                rfcsHelper.planificaPruebas(new Date(), new Date(), rfc);
                rfcsHelper.planificaPasoProduccion(null, rfc);
            } else {
                rfcsHelper.planificaDesarrollo(new Date(), new Date(), rfc);
                rfcsHelper.planificaPruebas(null, null, rfc);
            }
            rfcsHelper.asignaTarea(rfc);
        }
    }

    @Given("^(\\d+) rfc creada en estado \"([^\"]*)\" con tareas asociadas con una fecha de inicio mayor que la de fin$")
    public void rfc_creada_en_estado_con_tareas_asociadas_con_una_fecha_de_inicio_mayor_que_la_de_fin(int num, String estado)
            throws Throwable {

        StatusKey statusKey = StatusKey.valueOf(estado);

        Calendar fecha = new GregorianCalendar();
        fecha.add(Calendar.MONTH, -1);

        for(int i = 0 ; i < num ; i++) {

            Rfc rfc = rfcsHelper.addRfcWithEstado(statusKey);
            if (statusKey.equals(StatusKey.DESARROLLANDO)) {
                rfcsHelper.planificaDesarrollo(new Date(), fecha.getTime(), rfc);
            } else {
                rfcsHelper.planificaDesarrollo(new Date(), new Date(), rfc);
                rfcsHelper.planificaPruebas(new Date(), fecha.getTime(), rfc);
            }
            rfcsHelper.asignaTarea(rfc);
        }
    }

    @Given("^(\\d+) rfc creada en estado \"([^\"]*)\" con fecha de fin vencida$")
    public void rfc_creada_en_estado_con_fecha_de_fin_vencida(int num, String estado) throws Throwable {
        StatusKey statusKey = StatusKey.valueOf(estado);

        Calendar desde = new GregorianCalendar();
        desde.add(Calendar.DAY_OF_MONTH, -15);
        Calendar hasta = new GregorianCalendar();
        hasta.add(Calendar.DAY_OF_MONTH, -5);

        for(int i = 0 ; i < num ; i++) {

            Rfc rfc = rfcsHelper.addRfcWithEstado(statusKey);
            if (statusKey.equals(StatusKey.DESARROLLANDO)) {
                rfcsHelper.planificaDesarrollo(desde.getTime(), hasta.getTime(), rfc);
            } else if (statusKey.equals(StatusKey.FINALIZADA)) {
                rfcsHelper.planificaDesarrollo(new Date(), new Date(), rfc);
                rfcsHelper.planificaPruebas(new Date(), new Date(), rfc);
                rfcsHelper.planificaPasoProduccion(hasta.getTime(), rfc);
            } else {
                rfcsHelper.planificaDesarrollo(new Date(), new Date(), rfc);
                rfcsHelper.planificaPruebas(desde.getTime(), hasta.getTime(), rfc);
            }
            rfcsHelper.asignaTarea(rfc);
        }
    }

    @And("^(\\d+) rfc que no tenga equipo asignado$")
    public void rfc_que_no_tenga_equipo_asignado(int arg1) throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }
}
