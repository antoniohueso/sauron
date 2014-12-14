package com.corpme.sauron.domain;

import com.corpme.sauron.domain.Worklog;
import org.springframework.data.repository.Repository;

import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Repository
public interface WorklogsRepository extends Repository<Worklog, Long> {

    public List<Worklog> findByStartedBetween(Date fdesde, Date fhasta);

}
