package com.corpme.sauron.domain;

import com.corpme.sauron.domain.TipoSolicitud;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoSolicitudRepository extends CrudRepository<TipoSolicitud, Long> {

}
