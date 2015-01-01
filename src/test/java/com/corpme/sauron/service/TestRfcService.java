package com.corpme.sauron.service;

import com.corpme.sauron.Application;
import com.corpme.sauron.domain.*;
import com.corpme.sauron.service.bean.AnomaliaRfc;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class TestRfcService {

    @Autowired(required = true)
    RfcService rfcService;

    @Autowired(required = true)
    RfcRepository rfcRepository;

    @Autowired(required = true)
    RfcIssueLinkRepository rfcIssueLinkRepository;

    Logger logger = LoggerFactory.getLogger(getClass());

    Rfc rfc = null;

    @After
    public void after() {
        rfcIssueLinkRepository.deleteAll();
        rfcRepository.deleteAll();
    }

    @Test @Transactional
    public void testResumenRfcs_Pendientes_ok() {
        checkResumenRfcsCollection("pendientes", 1, creaRfcWithEstado(StatusKey.OPEN));
     }

    @Test @Transactional
    public void testResumenRfcs_Paradas_ok() {
        checkResumenRfcsCollection("paradas", 1, creaRfcWithEstado(StatusKey.DETENIDA));
    }

    @Test @Transactional
    public void testResumenRfcs_Encurso_Estado_Desarrollando_ok() {
        Rfc rfc = creaRfcWithEstadoAndLinks(StatusKey.DESARROLLANDO);
        rfc.setfInicioDesarrollo(new Date());
        rfc.setfFinDesarrollo(new Date());
        checkResumenRfcsCollection("encurso", 1, rfc);
    }

    @Test @Transactional
    public void testResumenRfcs_Encurso_Estado_Resolved_ok() {
        Rfc rfc = creaRfcWithEstadoAndLinks(StatusKey.RESOLVED);
        rfc.setfInicioDesarrollo(new Date());
        rfc.setfFinDesarrollo(new Date());
        checkResumenRfcsCollection("encurso", 1, rfc);
    }

    @Test @Transactional
    public void testResumenRfcs_Encurso_Estado_DisponibleParaPruebas_ok() {
        Rfc rfc = creaRfcWithEstadoAndLinks(StatusKey.DISPONIBLE_PARA_PRUEBAS);
        rfc.setfInicioDesarrollo(new Date());
        rfc.setfFinDesarrollo(new Date());
        rfc.setfInicioCalidad(new Date());
        rfc.setfFinCalidad(new Date());
        checkResumenRfcsCollection("encurso", 1, rfc);
    }

    @Test @Transactional
    public void testResumenRfcs_Encurso_Estado_Probando_ok() {
        Rfc rfc = creaRfcWithEstadoAndLinks(StatusKey.PROBANDO);
        rfc.setfInicioDesarrollo(new Date());
        rfc.setfFinDesarrollo(new Date());
        rfc.setfInicioCalidad(new Date());
        rfc.setfFinCalidad(new Date());
        checkResumenRfcsCollection("encurso", 1, rfc);
    }

    @Test @Transactional
    public void testResumenRfcs_Encurso_Estado_DetectadoErrorEnPruebas_ok() {
        Rfc rfc = creaRfcWithEstadoAndLinks(StatusKey.DETECTADO_ERROR_PRUEBAS);
        rfc.setfInicioDesarrollo(new Date());
        rfc.setfFinDesarrollo(new Date());
        rfc.setfInicioCalidad(new Date());
        rfc.setfFinCalidad(new Date());
        checkResumenRfcsCollection("encurso", 1, rfc);
    }

    @Test @Transactional
    public void testResumenRfcs_Encurso_Estado_Finalizada_ok() {
        Rfc rfc = creaRfcWithEstadoAndLinks(StatusKey.FINALIZADA);
        rfc.setfInicioDesarrollo(new Date());
        rfc.setfFinDesarrollo(new Date());
        rfc.setfInicioCalidad(new Date());
        rfc.setfFinCalidad(new Date());
        rfc.setfPasoProd(new Date());
        checkResumenRfcsCollection("encurso", 1, rfc);
    }

    @Test @Transactional
    public void testResumenRfcs_Encurso_Estado_Cerrada_ok() {
        creaRfcWithEstadoAndLinks(StatusKey.CERRADA);
        for( Collection c : rfcService.resumenRfcs().values() ) {
            assertEquals("Todas las colecciones han de tener 0 items",0,c.size());
        }
    }

    @Test @Transactional
    public void testResumenRfcs_Encurso_Estado_EnProduccion_ok() {
        creaRfcWithEstadoAndLinks(StatusKey.EN_PRODUCCION);
        for( Collection c : rfcService.resumenRfcs().values() ) {
            assertEquals("Todas las colecciones han de tener 0 items",0,c.size());
        }
    }

    @Test @Transactional
    public void testResumenRfcs_Anomalias_ok() {

        //--- Fecha de inicio de desarrollo mayor que la de fin. Todos los estados.
        Rfc rfc = creaRfcWithEstadoAndLinks(StatusKey.DESARROLLANDO);
        Calendar fecha = new GregorianCalendar();
        fecha.add(Calendar.MONTH, -1);
        rfc.setfInicioDesarrollo(new Date());
        rfc.setfFinDesarrollo(fecha.getTime());
        checkResumenRfcsCollection("anomalias", 1, rfc);
        checkResumenRfcsCollection("anomalias", 1, changeStatus(rfc,StatusKey.DISPONIBLE_PARA_PRUEBAS));
        checkResumenRfcsCollection("anomalias", 1, changeStatus(rfc,StatusKey.PROBANDO));
        checkResumenRfcsCollection("anomalias", 1, changeStatus(rfc,StatusKey.DETECTADO_ERROR_PRUEBAS));
        checkResumenRfcsCollection("anomalias", 1, changeStatus(rfc,StatusKey.FINALIZADA));

        //--- Fecha de inicio y fin de desarrollo Ok pero fecha de fin de calidad menor que la de inicio
        //    Todos los estados se calidad
        rfc.setfInicioDesarrollo(new Date());
        rfc.setfFinDesarrollo(new Date());
        rfc.setfInicioCalidad(new Date());
        rfc.setfFinCalidad(fecha.getTime());
        checkResumenRfcsCollection("anomalias", 1, changeStatus(rfc,StatusKey.DISPONIBLE_PARA_PRUEBAS));
        checkResumenRfcsCollection("anomalias", 1, changeStatus(rfc,StatusKey.PROBANDO));
        checkResumenRfcsCollection("anomalias", 1, changeStatus(rfc,StatusKey.DETECTADO_ERROR_PRUEBAS));
        checkResumenRfcsCollection("anomalias", 1, changeStatus(rfc,StatusKey.FINALIZADA));


        //--- Estado Finalizada sin fecha de paso a prod.
        rfc.setfInicioDesarrollo(new Date());
        rfc.setfFinDesarrollo(new Date());
        rfc.setfInicioCalidad(new Date());
        rfc.setfFinCalidad(new Date());
        checkResumenRfcsCollection("anomalias", 1, changeStatus(rfc,StatusKey.FINALIZADA));

        //--- Estado finalizada todas las fechas correctas pero sin links
        rfcIssueLinkRepository.deleteAll();
        rfcRepository.deleteAll();
        rfc = creaRfcWithEstado(StatusKey.FINALIZADA);
        rfc.setfInicioDesarrollo(new Date());
        rfc.setfFinDesarrollo(new Date());
        rfc.setfInicioCalidad(new Date());
        rfc.setfFinCalidad(new Date());
        rfc.setfPasoProd(new Date());
        checkResumenRfcsCollection("anomalias", 1, rfc);

    }



    @Test @Transactional
    public void testResumenRfcs_Anomalias_Estado_Probando_fecha_fin_menor_f_inicio_ok() {
        Rfc rfc = creaRfcWithEstadoAndLinks(StatusKey.PROBANDO);
        Calendar fecha = new GregorianCalendar();
        fecha.add(Calendar.MONTH, -1);
        rfc.setfInicioCalidad(new Date());
        rfc.setfFinDesarrollo(fecha.getTime());
        checkResumenRfcsCollection("anomalias", 1, rfc);
    }

    @Test @Transactional
    public void testResumenRfcs_Anomalias_Estado_Desarrollando_sin_links_ok() {
        Rfc rfc = creaRfcWithEstado(StatusKey.DESARROLLANDO);
        rfc.setfInicioDesarrollo(new Date());
        rfc.setfFinDesarrollo(new Date());
        checkResumenRfcsCollection("anomalias", 1, rfc);
    }




    Rfc changeStatus(Rfc rfc, StatusKey estado) {
        Status status = new Status();
        status.setId(estado.getValue());
        rfc.setStatus(status);
        return rfcRepository.save(rfc);
    }

    Rfc creaRfcWithEstado(StatusKey estado) {

        logger.info("creaRfcWithEstado");

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

    Rfc creaRfcWithEstadoAndLinks(StatusKey estado) {
        Rfc rfc = creaRfcWithEstado(estado);

        RfcIssueLink ln = new RfcIssueLink();
        ln.setId(1L);
        Issue issue = new Issue();
        issue.setId(28949L);
        ln.setIssue(issue);
        ln.setRfc(rfc);
        rfcIssueLinkRepository.save(ln);
        rfc.getIssuelinks().add(rfcIssueLinkRepository.findOne(ln.getId()));

        return rfc;
    }

    void checkResumenRfcsCollection(String key,int esperado,Rfc rfcEsperada){

        Map<String,Collection> resumen = rfcService.resumenRfcs();

        assertEquals("El resumen ha de devolver 5 colecciones", 5 ,resumen.size());

        String [] colls = new String [] {"pendientes","paradas","encurso","vencidas","anomalias"};

        Collection c = resumen.get(key);
        assertEquals("La colección "+key+ " ha de tener 1 item", 1, c.size());

        Object data = c.toArray()[0];
        if(data instanceof AnomaliaRfc) {
            AnomaliaRfc anomaliaRfc = (AnomaliaRfc)data;
            assertEquals("La rfc de la colección " + key + " ha de ser la misma que acabamos de crear"
                    , anomaliaRfc.getRfc().getId(), rfcEsperada.getId());
        }
        else {
            Rfc rfc = (Rfc)data;
            assertEquals("La rfc de la colección "+key+ " ha de ser la misma que acabamos de crear"
                    , rfc.getId(), rfcEsperada.getId());
        }

        for(String collKey : colls) {

            if(collKey.equals(key)) continue;;

            c = resumen.get(collKey);
            assertEquals("La colección "+collKey+ " ha de tener 0 items",0,c.size());

        }

    }

}


