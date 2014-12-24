package com.corpme.sauron.domain;

import com.corpme.sauron.domain.Worklog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Repository
public interface WorklogsRepository extends Repository<Worklog, Long> {

    @Query("select w from Worklog w join fetch w.author join fetch w.updateAuthor join fetch w.issue " +
            "where started >= :fdesde and started <= :fhasta and w.author in (:users)")
    public List<Worklog> findByStartedBetweenAndAuthorIn(
            @Param("fdesde") Date fdesde,
            @Param("fhasta") Date fhasta,
            @Param("users") Iterable<User> users);

}
