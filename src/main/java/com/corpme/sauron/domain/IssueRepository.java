package com.corpme.sauron.domain;

import com.corpme.sauron.domain.Issue;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends CrudRepository<Issue, Long> {

    /*
    @Query(value = "select i from Issue i join fetch i.project " +
            "where i.statusId not in(6,10002,10003) " +
            "and i not in (select iis.issue from IssueSolicitud iis)")
    Iterable<Issue> findIssuesNoPlanificadas();

    @Query(value = "select i from Issue i join fetch i.project where i.statusId not in(6,10002,10003) " +
            "and i.project.id = :projectId and i not in (select iis.issue from IssueSolicitud iis)")
    Iterable<Issue> findIssuesNoPlanificadasByProject(@Param("projectId") Long projectId);

    @Query(value = "select i from Issue i join fetch i.project " +
            "where i.statusId not in(6,10002,10003) " +
            "  and i.project.id = :projectId " +
            "  and i in (select ic.issue from IssueComponent ic where ic.component.id = :componentId) " +
            "  and i not in (select iis.issue from IssueSolicitud iis)")
    Iterable<Issue> findIssuesNoPlanificadasByProjectAndComponent(@Param("projectId") Long projectId
            , @Param("componentId") Long componentId);
*/
}
