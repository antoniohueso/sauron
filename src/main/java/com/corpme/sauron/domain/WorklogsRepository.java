package com.corpme.sauron.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Repository
public interface WorklogsRepository extends Repository<Worklog, Long> {

    @Query("select w from Worklog w join fetch w.author join fetch w.updateAuthor join fetch w.issue " +
            "where w.started >= :fdesde and w.started <= :fhasta and w.author in (:users)")
    public List<Worklog> findWorklogs(
            @Param("fdesde") Date fdesde,
            @Param("fhasta") Date fhasta,
            @Param("users") Iterable<User> users);

    @Query("select w from Worklog w join fetch w.author join fetch w.updateAuthor join fetch w.issue i " +
            "where w.started >= :fdesde and w.started <= :fhasta and w.author in (:users) " +
            "and (i.issuekey = 'GI-8'or i.issuekey = 'GI-9')")
    public List<Worklog> findVacaciones(
            @Param("fdesde") Date fdesde,
            @Param("fhasta") Date fhasta,
            @Param("users") Iterable<User> users);

}
