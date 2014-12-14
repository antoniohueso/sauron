package com.corpme.sauron.domain;

import com.corpme.sauron.domain.Solicitud;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface SolicitudRepository extends CrudRepository<Solicitud, Long> {

    @Query("Select s.estadoSolicitud, count(s) from Solicitud s group by s.estadoSolicitud")
    public Iterable<Object[]> resumenSolicitudes();

    @Query("Select s from Solicitud s join s.estadoSolicitud join s.tipoSolicitud " +
            "where s.estadoSolicitud = :estado")
    public Iterable<Solicitud> search(@Param("estado") EstadoSolicitud estadoSolicitud);

    public Solicitud findByTitulo(String title);

}
