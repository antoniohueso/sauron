package com.corpme.sauron.web;

import com.corpme.sauron.domain.Issue;
import com.corpme.sauron.domain.Rfc;
import com.corpme.sauron.service.IssueService;
import com.corpme.sauron.service.JiraRestService;
import com.corpme.sauron.service.RfcService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by ahg on 14/12/14.
 */
@Controller
@RequestMapping("/rfcs")
public class RfcController {

    @Autowired
    RfcService rfcService;

    Logger logger = Logger.getLogger(getClass().getName());

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Iterable<Rfc> rfcs() {
        Iterable<Rfc> rfcIterable =  rfcService.rfcs();

        logger.info("TOTAL: "+Lists.newArrayList(rfcIterable).size());

        return rfcIterable;
    }



}
