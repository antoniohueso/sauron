package com.corpme.sauron.domain;

import com.corpme.sauron.domain.Equipo;
import com.corpme.sauron.domain.Solicitud;
import com.corpme.sauron.domain.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface EquipoRepository extends CrudRepository<Equipo, Long> {

    @Query(value = "select e from Equipo e join fetch e.user join fetch e.solicitud where e.solicitud = :solicitud")
    public Iterable<Equipo> findBySolicitud(@Param("solicitud1") Solicitud solicitud);

    public Equipo findBySolicitudAndUser(Solicitud solicitud, User user);

    @Modifying
    @Query(value = "delete from Equipo e where e.user not in :lista and e.solicitud = :solicitud")
    public void deleteUsersNotInEquipo(@Param("solicitud1") Solicitud solicitud
            , @Param("lista") Collection<User> users);
}
