package com.corpme.sauron.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByName(String name);

    @Query("select u from User u where u.id in (select sc.id from SCUser sc) order by u.displayName")
    Iterable<User> findAllFromServiciosCentrales();

    @Query("select u from User u where u.id in (select sc.id from SCUser sc) " +
            " and u not in (select ed.user from RfcEquipoDesarrollo ed where ed.rfc.status.id not in(6,10003,10002)) " +
            " and u not in (select ec.user from RfcEquipoCalidad ec where ec.rfc.status.id not in(6,10003,10002)) " +
            "order by u.displayName")
    Iterable<User> findDisponiblesFromServiciosCentrales();

}
