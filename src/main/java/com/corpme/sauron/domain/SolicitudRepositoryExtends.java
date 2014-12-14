package com.corpme.sauron.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;


@Repository
public class SolicitudRepositoryExtends {

    @Autowired
    EntityManager em;

    public Iterable<Solicitud> search(EstadoSolicitud estadoSolicitud,TipoSolicitud tipoSolicitud) {

        String strquery = "Select s from Solicitud s join fetch s.estadoSolicitud join fetch s.tipoSolicitud ";

        Collection<Object[]> params = new ArrayList();

        try {

            if(estadoSolicitud != null) {
                params.add(new Object[] { " s.estadoSolicitud = :estado", "estado", estadoSolicitud});
            }

            if(tipoSolicitud != null) {
                params.add(new Object[] { " s.tipoSolicitud = :tipo", "tipo", tipoSolicitud});
            }

            StringBuilder builder = new StringBuilder(strquery);

            if(params.size() > 0) {
                builder.append(" where ");
                boolean and = false;
                for(Object[] p : params) {
                    if(!and) {
                        builder.append(p[0]);
                        and = true;
                    }
                    else {
                        builder.append(" and ").append(p[0]);
                    }
                }

                Logger.getGlobal().info("Qyery-> "+builder.toString());

            }

            Query query = em.createQuery(builder.toString());
            for(Object[] p : params) {
                query.setParameter((String)p[1],p[2]);
            }

            return query.getResultList();
        }
        finally {
            em.close();
        }
    }



}
