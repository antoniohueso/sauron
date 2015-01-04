package com.corpme.sauron.service;

import com.corpme.sauron.domain.*;
import com.corpme.sauron.service.RfcService;
import com.corpme.sauron.service.UtilsService;
import com.corpme.sauron.service.bean.AnomaliaRfc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;
import static  org.hamcrest.Matchers.*;



import javax.annotation.PostConstruct;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class TestRfcServiceResumenRfcs {

    @Mock
    RfcRepository rfcRepository;

    @InjectMocks
    RfcService rfcService;

    @Spy
    UtilsService utilsService;

    Logger logger = LoggerFactory.getLogger(getClass());

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    /**
     * Si no hay rfcs creadas en estado
     * OPEN,
     * DESARROLLANDO,
     * DISPONIBLE_PARA_PRUEBAS,
     * PROBANDO,
     * DETECTADO_ERROR_EN_PRUEBAS
     * FINALIZADA
     * No debe aparecer ningún item en las colecciones del resumen
     * @throws Throwable
     */
    @Test
    public void given_no_hay_rfcs_creadas() throws Throwable {

        Collection<Rfc> result = new ArrayList();

        when(rfcRepository.findRfcsEnCurso()).thenReturn(result);

        Map<String,Collection> resumen = rfcService.resumenRfcs();

        assertThat("El resumen ha de tener 5 colecciones",resumen.size(),is(5));

        resumen.forEach((key,coll) -> {
            assertThat("Las colección "+key+" ha de tener 0 items",coll.size(),is(1));
        });

    }

    /**
     * Las rfcs con estado OPEN deben aparecer en la colección 'pendientes' del resumen
     * @throws Throwable
     */
    @Test
    public void given_una_rfc_pendiente() throws Throwable{
        final List<Rfc> result = creaRfcWithEstado(1L, StatusKey.OPEN);
        when(rfcRepository.findRfcsEnCurso()).thenReturn(result);
        checkResumenRfcsCollection(rfcService.resumenRfcs(),"pendientes",result);
    }

    /**
     * Las rfcs con estado DETENIDA deben aparecer en la colección 'paradas' del resumen
     * @throws Throwable
     */
    @Test
    public void given_una_rfc_parada() throws Throwable{
        final List<Rfc> result = creaRfcWithEstado(1L, StatusKey.DETENIDA);
        when(rfcRepository.findRfcsEnCurso()).thenReturn(result);
        checkResumenRfcsCollection(rfcService.resumenRfcs(),"paradas",result);
    }

    /**
     * Las rfcs con estado
     * DESARROLLANDO,
     * DISPONIBLE_PARA_PRUEBAS,
     * PROBANDO,
     * DETECTADO_ERROR_EN_PRUEBAS
     * FINALIZADA
     * deben aparecer en la colección 'encurso' si se cumplen las siguientes reglas:
     * 1) La rfc ha de tener al menos 1 tarea asociada
     * 2) Desarrollo en curso (DESARROLLANDO)
     * - Debe tener una fecha válida de inicio y fin de desarrollo
     * - La fecha de inicio no puede ser mayor que la de fin
     * - Ninguna de las fecha puede ser nula
     * - La fecha de fin ha de ser menor o igual que el día de hoy
     * 3) Pruebas de calidad en curso (DISPONIBLE_PARA_PRUEBAS,PROBANDO,DETECTADO_ERROR_PRUEBAS)
     * - Deben de cumplirse las mismas reglas que para el Desarrollo en curso excepto la útima y además las siguientes.
     * - Debe tener una fecha válida de inicio y fin de calidad
     * - La fecha de inicio no puede ser mayor que la de fin
     * - Ninguna de las fecha puede ser nula
     * - La fecha de fin ha de ser menor o igual que el día de hoy
     * 3) Desarrollo finalizado pendiente de pasar a producción (FINALIZADA)
     * - Deben de cumplirse las mismas reglas que para el Desarrollo en curso y Pruebas de calidad excepto la útima
     * y además las siguientes.
     * - Debe tener una fecha válida de paso a producción
     * - Ninguna de las fechas puede ser nula
     * - La fecha de paso a producción ha de ser menor o igual
     * @throws Throwable
     */
    @Test
    public void given_una_rfc_en_curso() throws Throwable{

        final List<Rfc> result = creaRfcWithEstado(1L, StatusKey.DESARROLLANDO);
        when(rfcRepository.findRfcsEnCurso()).thenReturn(result);

        Rfc rfc = result.get(0);
        asignaTarea(1000L,rfc);
        planificaDesarrollo(new Date(),new Date(),rfc);

        checkResumenRfcsCollection(rfcService.resumenRfcs(),"encurso",result);

        planificaPruebas(new Date(),new Date(),rfc);

        rfc.getStatus().setId(StatusKey.DISPONIBLE_PARA_PRUEBAS.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "encurso", result);

        rfc.getStatus().setId(StatusKey.PROBANDO.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "encurso", result);

        rfc.getStatus().setId(StatusKey.DETECTADO_ERROR_PRUEBAS.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "encurso", result);

        planificaPasoProduccion(new Date(),rfc);
        rfc.getStatus().setId(StatusKey.FINALIZADA.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "encurso", result);
    }

    /**
     * Las Rfcs con estado en EN_PRODUCCION o CERRADAS no deben aparecer en el resumen
     * @throws Throwable
     */
    @Test
    public void given_dos_rfcs_una_En_Produccion_y_otra_Cerrada() throws Throwable{
        final List<Rfc> result = creaRfcWithEstado(1L, StatusKey.EN_PRODUCCION);
        when(rfcRepository.findRfcsEnCurso()).thenReturn(result);

        rfcService.resumenRfcs().forEach((key,coll) -> {
            assertThat("Las colección "+key+" ha de tener 0 items",coll.size(),is(0));
        });

        Rfc rfc = result.get(0);
        rfc.getStatus().setId(StatusKey.CERRADA.getValue());

        rfcService.resumenRfcs().forEach((key,coll) -> {
            assertThat("Las colección "+key+" ha de tener 0 items",coll.size(),is(0));
        });
    }

    /**
     * Las rfcs con estado
     * DESARROLLANDO,
     * 1) La rfc ha de tener al menos 1 tarea asociada
     * 2) Desarrollo en curso (DESARROLLANDO)
     * - Debe tener una fecha válida de inicio y fin de desarrollo
     * - La fecha de inicio no puede ser mayor que la de fin
     * - Ninguna de las fecha puede ser nula
     * - La fecha de fin ha de ser menor o igual que el día de hoy
     * 3) Pruebas de calidad en curso (DISPONIBLE_PARA_PRUEBAS,PROBANDO,DETECTADO_ERROR_PRUEBAS)
     * - Deben de cumplirse las mismas reglas que para el Desarrollo en curso excepto la útima y además las siguientes.
     * - Debe tener una fecha válida de inicio y fin de calidad
     * - La fecha de inicio no puede ser mayor que la de fin
     * - Ninguna de las fecha puede ser nula
     * - La fecha de fin ha de ser menor o igual que el día de hoy
     * 3) Desarrollo finalizado pendiente de pasar a producción (FINALIZADA)
     * - Deben de cumplirse las mismas reglas que para el Desarrollo en curso y Pruebas de calidad excepto la útima
     * y además las siguientes.
     * - Debe tener una fecha válida de paso a producción
     * - Las fecha puede ser nula
     * - La fecha de paso a producción ha de ser menor o igual que el día de hoy
     * @throws Throwable
     */
    @Test
    public void given_una_rfc_con_anomalia_o_vencida() throws Throwable{

        final List<Rfc> result = creaRfcWithEstado(1L, StatusKey.DESARROLLANDO);
        when(rfcRepository.findRfcsEnCurso()).thenReturn(result);
        Rfc rfc = result.get(0);

        /**
         *  La rfc ha de tener al menos 1 tarea asociada
         */
        rfc.getStatus().setId(StatusKey.DESARROLLANDO.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "anomalias", result);
        rfc.getStatus().setId(StatusKey.DISPONIBLE_PARA_PRUEBAS.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "anomalias", result);
        rfc.getStatus().setId(StatusKey.PROBANDO.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "anomalias", result);
        rfc.getStatus().setId(StatusKey.DETECTADO_ERROR_PRUEBAS.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "anomalias", result);
        rfc.getStatus().setId(StatusKey.FINALIZADA.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "anomalias", result);

        asignaTarea(100L,rfc);

        /**
         * En cualquier estado de los descritos si no tiene fechas debe aparecer como anomalía
         */
        rfc.getStatus().setId(StatusKey.DESARROLLANDO.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "anomalias", result);
        rfc.getStatus().setId(StatusKey.DISPONIBLE_PARA_PRUEBAS.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "anomalias", result);
        rfc.getStatus().setId(StatusKey.PROBANDO.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "anomalias", result);
        rfc.getStatus().setId(StatusKey.DETECTADO_ERROR_PRUEBAS.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "anomalias", result);
        rfc.getStatus().setId(StatusKey.FINALIZADA.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "anomalias", result);

        /**
         * Desarrollo en curso (DESARROLLANDO)
         * Debe tener una fecha válida de inicio y fin de desarrollo
         */
        rfc.setfInicioDesarrollo(null);
        rfc.setfFinDesarrollo(null);
        rfc.getStatus().setId(StatusKey.DESARROLLANDO.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "anomalias", result);
        /**
         * Desarrollo en curso (DESARROLLANDO)
         * La fecha de inicio no puede ser mayor que la de fin
         */
        Calendar fecha = new GregorianCalendar();
        fecha.add(Calendar.MONTH,-1);
        rfc.setfInicioDesarrollo(new Date());
        rfc.setfFinDesarrollo(fecha.getTime());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "anomalias", result);
        /**
         * Desarrollo en curso (DESARROLLANDO)
         * La fecha de fin ha de ser menor o igual que el día de hoy
         */
        fecha.add(Calendar.MONTH,-1);
        rfc.setfInicioDesarrollo(fecha.getTime());
        fecha.add(Calendar.DAY_OF_MONTH,3);
        rfc.setfFinDesarrollo(fecha.getTime());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "vencidas", result);

        rfc.setfInicioDesarrollo(new Date());
        rfc.setfFinDesarrollo(new Date());

        /**
         * Pruebas de calidad en curso (DISPONIBLE_PARA_PRUEBAS,PROBANDO,DETECTADO_ERROR_PRUEBAS)
         * Debe tener una fecha válida de inicio y fin de calidad
         */
        rfc.getStatus().setId(StatusKey.DISPONIBLE_PARA_PRUEBAS.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "anomalias", result);
        rfc.getStatus().setId(StatusKey.PROBANDO.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "anomalias", result);
        rfc.getStatus().setId(StatusKey.DETECTADO_ERROR_PRUEBAS.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "anomalias", result);

        /**
         * Pruebas de calidad en curso (DISPONIBLE_PARA_PRUEBAS,PROBANDO,DETECTADO_ERROR_PRUEBAS)
         * La fecha de inicio no puede ser mayor que la de fin
         */
        fecha = new GregorianCalendar();
        fecha.add(Calendar.MONTH, -1);
        rfc.setfInicioCalidad(new Date());
        rfc.setfFinCalidad(fecha.getTime());
        rfc.getStatus().setId(StatusKey.DISPONIBLE_PARA_PRUEBAS.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "anomalias", result);
        rfc.getStatus().setId(StatusKey.PROBANDO.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "anomalias", result);
        rfc.getStatus().setId(StatusKey.DETECTADO_ERROR_PRUEBAS.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "anomalias", result);

        /**
         * Pruebas de calidad en curso (DISPONIBLE_PARA_PRUEBAS,PROBANDO,DETECTADO_ERROR_PRUEBAS)
         * La fecha de fin ha de ser menor o igual que el día de hoy
         */
        fecha.add(Calendar.MONTH,-1);
        rfc.setfInicioCalidad(fecha.getTime());
        fecha.add(Calendar.DAY_OF_MONTH,3);
        rfc.setfFinCalidad(fecha.getTime());
        rfc.getStatus().setId(StatusKey.DISPONIBLE_PARA_PRUEBAS.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "vencidas", result);
        rfc.getStatus().setId(StatusKey.PROBANDO.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "vencidas", result);
        rfc.getStatus().setId(StatusKey.DETECTADO_ERROR_PRUEBAS.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "vencidas", result);

        rfc.setfInicioCalidad(new Date());
        rfc.setfFinCalidad(new Date());

        /**
         * Desarrollo finalizado pendiente de pasar a producción (FINALIZADA)
         * Debe tener una fecha válida de paso a producción
         */
        rfc.getStatus().setId(StatusKey.FINALIZADA.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "anomalias", result);

        /**
         * Desarrollo finalizado pendiente de pasar a producción (FINALIZADA)
         * La fecha de paso a producción ha de ser menor o igual que el día de hoy
         */
        fecha = new GregorianCalendar();
        fecha.add(Calendar.DAY_OF_MONTH,-1);
        rfc.setfPasoProd(fecha.getTime());
        rfc.getStatus().setId(StatusKey.FINALIZADA.getValue());
        checkResumenRfcsCollection(rfcService.resumenRfcs(), "vencidas", result);

    }

    List<Rfc> creaRfcWithEstado(Long id,StatusKey estado) {

        List<Rfc> result = new ArrayList();

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

        result.add(rfc);

        return result;
    }

    void asignaTarea(Long id,Rfc rfc) {
        RfcIssueLink ln = new RfcIssueLink();
        ln.setId(id);
        Issue issue = new Issue();
        issue.setId(id);
        ln.setIssue(issue);
        ln.setRfc(rfc);
        rfc.getIssuelinks().add(ln);
    }

    void planificaDesarrollo(Date fdesde, Date fhasta, Rfc rfc) {
        rfc.setfInicioDesarrollo(fdesde);
        rfc.setfFinDesarrollo(fhasta);
    }

    void planificaPruebas(Date fdesde, Date fhasta, Rfc rfc) {
        rfc.setfInicioCalidad(fdesde);
        rfc.setfFinCalidad(fhasta);
    }

    void planificaPasoProduccion(Date fecha, Rfc rfc) {
        rfc.setfPasoProd(fecha);
    }

    void checkResumenRfcsCollection(Map<String,Collection> resumen,String keyEsperada,List<Rfc> juegoDeDatos) throws Throwable {

        resumen.forEach((key, coll) -> {
            if (key.equals(keyEsperada)) {
                assertThat("Las colección " + key + " ha de tener 1 items", 1, is(coll.size()));
                final Object obj = coll.iterator().next();
                final Rfc rfc;
                if (obj instanceof AnomaliaRfc) rfc = ((AnomaliaRfc) obj).getRfc();
                else rfc = (Rfc) obj;
                assertThat("La rfc de la colección debe coincidir con la creada", juegoDeDatos.get(0).getId(), is(rfc.getId()));
            } else {
                assertThat("Las colección " + key + " ha de tener items", coll.size(), is(0));
            }
        });
    }

}
