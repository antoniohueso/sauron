package com.corpme.sauron.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RfcRepository extends CrudRepository<Rfc, Long> {

    @Query(value = "" +
            "select i " +
            "from Rfc i " +
            "join fetch i.status " +
            "join fetch i.priority " +
            "left join fetch i.reporter " +
            "left join fetch i.assignee " +
            "left join fetch i.project " +
            "where i.status.id not in(6,10003)")
    Iterable<Rfc> findRfcsEnCurso();

    Rfc findByIssuekey(String issuekey);

}
