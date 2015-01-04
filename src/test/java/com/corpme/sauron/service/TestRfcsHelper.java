package com.corpme.sauron.service;

import com.corpme.sauron.domain.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by ahg on 04/01/15.
 */
public class TestRfcsHelper {

    private final List<Rfc> rfcsResult = new ArrayList();

    private final Random random = new Random();

    public Rfc addRfcWithEstado(final StatusKey estado) {

        final Rfc rfc = new Rfc();
        rfc.setId(new Long(random.nextInt(5000) + 1));
        rfc.setIssuekey("RFC-"+rfc.getId());
        rfc.setSummary(String.format("Test RFC-%d con estado: %d",rfc.getId(),estado.getValue()));
        Status status = new Status();
        status.setId(estado.getValue());
        rfc.setStatus(status);

        Priority priority = new Priority();
        priority.setId(3L);
        rfc.setPriority(priority);

        rfcsResult.add(rfc);

        return rfc;
    }

    public Rfc asignaTarea(final Rfc rfc) {
        final RfcIssueLink ln = new RfcIssueLink();
        ln.setId(new Long(random.nextInt(5000) + 1));
        final Issue issue = new Issue();
        issue.setId(new Long(random.nextInt(5000) + 1));
        ln.setIssue(issue);
        ln.setRfc(rfc);
        rfc.getIssuelinks().add(ln);
        return rfc;
    }

    public Rfc planificaDesarrollo(final Date fdesde, final Date fhasta, final Rfc rfc) {
        rfc.setfInicioDesarrollo(fdesde);
        rfc.setfFinDesarrollo(fhasta);
        return rfc;
    }

    public Rfc planificaPruebas(final Date fdesde, final Date fhasta, final Rfc rfc) {
        rfc.setfInicioCalidad(fdesde);
        rfc.setfFinCalidad(fhasta);
        return rfc;
    }

    public Rfc planificaPasoProduccion(final Date fecha, final Rfc rfc) {
        rfc.setfPasoProd(fecha);
        return rfc;
    }

    public List<Rfc> getRfcsResult() {
        return rfcsResult;
    }

    public boolean exist(Rfc rfc) {
        return rfcsResult.stream().anyMatch((r) -> rfc.getId().equals(r.getId()) );
    }
}
