package com.corpme.sauron.web;

import com.corpme.sauron.domain.Issue;
import com.corpme.sauron.domain.Rfc;
import com.corpme.sauron.service.IssueService;
import com.corpme.sauron.service.JiraRestService;
import com.corpme.sauron.service.RfcService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

        return rfcIterable;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/{key}")
    public @ResponseBody Rfc rfc(@PathVariable String key) {
        return rfcService.rfc(key);
    }



}
