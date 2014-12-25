package com.corpme.sauron.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

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

    @Query(value = "" +
            "select i " +
            "from Rfc i " +
            "join fetch i.status " +
            "join fetch i.priority " +
            "left join fetch i.reporter " +
            "left join fetch i.assignee " +
            "left join fetch i.project " +
            "where " +
            "    i.fInicioDesarrollo between :desde and :hasta" +
            " or i.fFinDesarrollo between :desde and :hasta " +
            " or i.fInicioCalidad between :desde and :hasta " +
            " or i.fFinCalidad between :desde and :hasta " +
            " or i.fPasoProd between :desde and :hasta ")
    Iterable<Rfc> findRfcsByDate(@Param("desde") Date desde
            ,@Param("hasta") Date hasta);

    Rfc findByIssuekey(String issuekey);

}
