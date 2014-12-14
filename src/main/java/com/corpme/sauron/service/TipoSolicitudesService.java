package com.corpme.sauron.service;

import com.corpme.sauron.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class TipoSolicitudesService {

    @Autowired
    TipoSolicitudRepository tipoSolicitudRepository;

    Logger logger = Logger.getLogger(getClass().getName());

    public Iterable<TipoSolicitud> tiposSolicitud() {

        return tipoSolicitudRepository.findAll();

    }

}
