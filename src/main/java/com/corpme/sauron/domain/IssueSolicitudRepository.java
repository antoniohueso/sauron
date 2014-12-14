package com.corpme.sauron.domain;

import com.corpme.sauron.domain.Issue;
import com.corpme.sauron.domain.IssueSolicitud;
import com.corpme.sauron.domain.Solicitud;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface IssueSolicitudRepository extends CrudRepository<IssueSolicitud, Long> {

    public Iterable<IssueSolicitud> findBySolicitud(Solicitud solicitud);

    public IssueSolicitud findByIssue(Issue issue);

    @Modifying
    @Query(value = "delete from IssueSolicitud isol where isol.issue.id not in :lista")
    public void deleteIssuesNotInPlanificacion(@Param("lista") Collection<Long> issues);

}
