package com.corpme.sauron.domain;

import com.corpme.sauron.domain.EstadoSolicitud;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoSolicitudRepository extends CrudRepository<EstadoSolicitud, Long> {

}
