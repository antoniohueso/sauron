package com.corpme.sauron.service;

import com.corpme.sauron.config.ApplicationException;
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

    @Autowired UtilsService utilsService;

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

    public Iterable<Solicitud> search(
            Long estadoId,
            Long tipoSolicitudId,
            Long id,
            String titulo
            ) {

        EstadoSolicitud estadoSolicitud = null;
        TipoSolicitud tipoSolicitud = null;

        if( estadoId != null ) {
            estadoSolicitud = estadoSolicitudRepository.findOne(estadoId);
        }

        if( tipoSolicitudId != null ) {
            tipoSolicitud = tipoSolicitudRepository.findOne(tipoSolicitudId);
        }

        return solicitudRepositoryExtends.search(estadoSolicitud,tipoSolicitud);
    }

    public Solicitud saveDatosGenerales(Long id,String titulo, String descripcion, Long tipoSolicitudId) {

        utilsService.validateRequired(titulo, "El título de la solicitud es obligatorio");

        utilsService.validateRequired(tipoSolicitudId, "El tipo de solicitud es obligatorio");

        utilsService.validateRequired(descripcion, "La descripción de la solicitud es obligatoria");

        Solicitud solicitud = null;
        if(id == null) {
            Solicitud stit = solicitudRepository.findByTitulo(titulo);
            if (stit != null) {
                throw new ApplicationException("El título de la solicitud ha de ser único. "
                        + "Ya existe una solicitud creada con el mismo título(ID:" + stit.getId() + ")");
            }
            solicitud = new Solicitud();
        }
        else {
            solicitud = findById(id);
        }
        solicitud.setTitulo(titulo);
        solicitud.setDescripcion(descripcion);
        solicitud.setTipoSolicitud(tipoSolicitudRepository.findOne(tipoSolicitudId));

        if(solicitud.getEstadoSolicitud() == null) {
            solicitud.setEstadoSolicitud(estadoSolicitudRepository.findOne(EstadoSolicitudType.Pendiente_asignacion_equipo.getValue()));
        }

        return solicitudRepository.save(solicitud);
    }

    public Solicitud findById(Long id) {
        Solicitud s = solicitudRepository.findOne(id);
        if(s == null) {
            throw new ApplicationException("No se ha encontrado la solicitud con ID: " + id);
        }
        return s;
    }

}
