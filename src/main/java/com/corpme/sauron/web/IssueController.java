package com.corpme.sauron.web;

import com.corpme.sauron.domain.Issue;
import com.corpme.sauron.domain.IssueRepository;
import com.corpme.sauron.domain.Rfc;
import com.corpme.sauron.service.RfcService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.logging.Logger;

/**
 * Created by ahg on 14/12/14.
 */
@Controller
@RequestMapping("/issues")
public class IssueController {

    @Autowired
    IssueRepository issueRepository;

    Logger logger = Logger.getLogger(getClass().getName());

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Iterable<Issue> issues() {
        Iterable<Issue> issueIterable =  issueRepository.findAll();

        logger.info("TOTAL: "+Lists.newArrayList(issueIterable).size());

        return issueIterable;
    }



}
