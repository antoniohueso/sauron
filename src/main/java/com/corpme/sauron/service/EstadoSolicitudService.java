package com.corpme.sauron.service;

import com.corpme.sauron.domain.EstadoSolicitud;
import com.corpme.sauron.domain.EstadoSolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ahg on 14/12/14.
 */
@Service
public class EstadoSolicitudService {

    @Autowired
    EstadoSolicitudRepository estadoSolicitudRepository;

    public Iterable<EstadoSolicitud> estados() {
        return estadoSolicitudRepository.findAll();
    }

}
