package com.corpme.sauron.service.bean;

import com.corpme.sauron.domain.Rfc;

/**
 * Created by ahg on 26/12/14.
 */
public class AnomaliaRfc {

    String anomalia;

    Rfc rfc;

    public AnomaliaRfc(Rfc rfc,String anomalia) {
        this.anomalia = anomalia;
        this.rfc = rfc;
    }

    public String getAnomalia() {
        return anomalia;
    }

    public Rfc getRfc() {
        return rfc;
    }
}
