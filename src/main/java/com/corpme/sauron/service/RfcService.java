package com.corpme.sauron.service;

import com.corpme.sauron.domain.Issue;
import com.corpme.sauron.domain.IssueRepository;
import com.corpme.sauron.domain.Rfc;
import com.corpme.sauron.domain.RfcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ahg on 22/12/14.
 */
@Service
public class RfcService {

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    RfcRepository rfcRepository;

    Logger logger = LoggerFactory.getLogger(getClass());

    public Iterable<Rfc> rfcs() {

        Iterable<Rfc> rfcs = rfcRepository.findRfcsEnCurso();
        return rfcs;
    }

    public Iterable<Issue> issues() {

        Iterable<Issue> issues = issueRepository.findAll();
        return issues;
    }


}
