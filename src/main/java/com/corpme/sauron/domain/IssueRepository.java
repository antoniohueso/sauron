package com.corpme.sauron.domain;

import com.corpme.sauron.domain.Issue;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends CrudRepository<Issue, Long> {


        @Query(value = "" +
                "select i " +
                "from Issue i " +
                "join fetch i.project " +
                "join fetch i.status " +
                "join fetch i.type " +
                "join fetch i.priority " +
                "left join fetch i.reporter " +
                "left join fetch i.assignee ")
        Iterable<Issue> findAll();

}
