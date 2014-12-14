package com.corpme.sauron.domain;

import com.corpme.sauron.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUserName(@Param("username") String username);

    Iterable<User> findByRol(@Param("rol") int rol);

    Iterable<User> findByRolIn(@Param("rol") Integer... roles);

}
