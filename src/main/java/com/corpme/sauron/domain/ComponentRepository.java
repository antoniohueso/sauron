package com.corpme.sauron.domain;

import com.corpme.sauron.domain.Component;
import com.corpme.sauron.domain.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentRepository extends CrudRepository<Component, Long> {

    Iterable<Component> findByProject(Project project);

}
