package com.corpme.sauron.domain;

import com.corpme.sauron.domain.Comentario;
import com.corpme.sauron.domain.Solicitud;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ComentarioRepository extends CrudRepository<Comentario, Long> {

    @Modifying
    @Query(value = "delete from Comentario c where c not in :lista and c.solicitud = :solicitud")
    public void deleteComentariosNotInList(@Param("solicitud") Solicitud solicitud
            , @Param("lista") Collection<Comentario> comentarios);

    @Modifying
    @Query(value = "delete from Comentario c where c.solicitud = :solicitud")
    public void deleteAllComentariosOfSolicitud(@Param("solicitud") Solicitud solicitud);

}
