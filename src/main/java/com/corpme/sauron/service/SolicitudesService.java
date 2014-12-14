package com.corpme.sauron.service;

import com.corpme.sauron.domain.EstadoSolicitud;
import com.corpme.sauron.domain.EstadoSolicitudRepository;
import com.corpme.sauron.domain.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class SolicitudesService {

    @Autowired
    SolicitudRepository solicitudRepository;

    @Autowired
    EstadoSolicitudRepository estadoSolicitudRepository;

    Logger logger = Logger.getLogger(getClass().getName());

    public Collection<Object[]> resumenSolicitudes() {

        Collection<Object[]> resumen = new ArrayList();

        Map<EstadoSolicitud,Long> estados = new LinkedHashMap();

        for(EstadoSolicitud estadoSolicitud : estadoSolicitudRepository.findAll()) {
            estados.put(estadoSolicitud,0L);
        }

        Iterable<Object[]> result = solicitudRepository.resumenSolicitudes();
        for(Object[] obj : result){
            EstadoSolicitud estadoSolicitud = (EstadoSolicitud)obj[0];
            long total = (Long)obj[1];
            estados.put(estadoSolicitud,total);
        }

        for(EstadoSolicitud estadoSolicitud : estados.keySet()) {
            resumen.add(new Object[]{ estadoSolicitud, estados.get(estadoSolicitud)});
        }

        return resumen;

    }

}
