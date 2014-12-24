package com.corpme.sauron.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByName(String name);

    @Query("select u from User u where u.id in (select sc.id from SCUser sc) order by u.displayName")
    Iterable<User> findAllFromServiciosCentrales();

}
