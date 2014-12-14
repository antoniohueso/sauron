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
public class SolicitudesService {

    @Autowired
    SolicitudRepository solicitudRepository;

    @Autowired
    SolicitudRepositoryExtends solicitudRepositoryExtends;

    @Autowired
    EstadoSolicitudRepository estadoSolicitudRepository;

    @Autowired
    TipoSolicitudRepository tipoSolicitudRepository;

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

    public Iterable<Solicitud> search(Map<String,Object> filtro) {

        EstadoSolicitud estadoSolicitud = null;
        TipoSolicitud tipoSolicitud = null;

        if(filtro!=null) {
            Integer estadoId = (Integer)filtro.get("estado");
            if(estadoId != null) {
                estadoSolicitud = estadoSolicitudRepository.findOne(new Long(estadoId));
            }

            Integer tipoId = (Integer)filtro.get("tipo");
            if(tipoId != null) {
                tipoSolicitud = tipoSolicitudRepository.findOne(new Long(tipoId));
            }
        }

        return solicitudRepositoryExtends.search(estadoSolicitud,tipoSolicitud);
    }

}
