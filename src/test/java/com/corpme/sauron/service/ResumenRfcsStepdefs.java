package com.corpme.sauron.service;

import com.corpme.sauron.Application;
import com.corpme.sauron.domain.*;
import com.corpme.sauron.service.bean.AnomaliaRfc;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Random;

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

    Logger logger = LoggerFactory.getLogger(getClass());

    @Before
    public void before() {

    }

    @Given("^no hay rfcs creadas$")
    public void no_hay_rfcs_creadas() throws Throwable {
        rfcRepository.deleteAll();
    }

    @Given("^una rfc con fecha de desarrollo y tareas asociadads en estado \"([^\"]*)\"$")
    public void una_rfc_con_fecha_de_desarrollo_y_tareas_asociadads_en_estado(String estado) throws Throwable {
        rfc = creaRfcWithEstadoAndAsocioUnaTarea(StatusKey.valueOf(estado));
        rfc.setfInicioDesarrollo(new Date());
        rfc.setfFinDesarrollo(new Date());
    }

    @When("^consulto el resumen de rfcs$")
    public void consulto_el_resumen_de_rfcs() throws Throwable {
       resumen = rfcService.resumenRfcs();
    }

    @Then("^debe devolver (\\d+) colecciones con un total de (\\d+) items$")
    public void debe_devolver_colecciones_con_un_total_de_items(int totalCollections, int totalItems) throws Throwable {

        assertEquals(totalCollections, resumen.size());

        for(Collection c : resumen.values()) {
            assertEquals(totalItems,c.size());
        }
    }

    @Given("^creo una rfc en estado \"([^\"]*)\"$")
    public void creo_una_rfc_en_estado(String estado) throws Throwable {
        rfc = creaRfcWithEstado(StatusKey.valueOf(estado));
    }

    @Then("^debe devolver (\\d+) colecciones con un total de (\\d+) items en todas excepto en la colección \"([^\"]*)\"" +
            " que devolverá (\\d+) item$")
    public void debe_devolver_colecciones_con_un_total_de_items_en_todas_excepto_en_la_colección(int totalCollections
            , int totalItems, String key,int itemsEsperados) throws Throwable {
        checkResumenRfcsCollection(key,totalCollections,totalItems,itemsEsperados);
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

    void checkResumenRfcsCollection(String key,int totalCollections,int totalItemsCollections,int esperado){

        assertEquals("El resumen ha de devolver 5 colecciones", totalCollections ,resumen.size());

        String [] colls = new String [] {"pendientes","paradas","encurso","vencidas","anomalias"};

        Collection c = resumen.get(key);
        assertEquals("La colección "+key+ " ha de tener 1 item", esperado, c.size());

        Object data = c.toArray()[0];
        if(data instanceof AnomaliaRfc) {
            AnomaliaRfc anomaliaRfc = (AnomaliaRfc)data;
            assertEquals("La rfc de la colección " + key + " ha de ser la misma que acabamos de crear"
                    , anomaliaRfc.getRfc().getId(), rfc.getId());
        }
        else {
            Rfc rfc = (Rfc)data;
            assertEquals("La rfc de la colección "+key+ " ha de ser la misma que acabamos de crear"
                    , rfc.getId(), rfc.getId());
        }

        for(String collKey : colls) {

            if(collKey.equals(key)) continue;;

            c = resumen.get(collKey);
            assertEquals("La colección "+collKey+ " ha de tener "+totalItemsCollections+" items"
                    ,totalItemsCollections,c.size());

        }

    }

    Rfc creaRfcWithEstadoAndAsocioUnaTarea(StatusKey estado) {
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

}
