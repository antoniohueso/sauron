package com.corpme.sauron.web;

import com.corpme.sauron.service.JiraRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by ahg on 14/12/14.
 */
@Controller
@RequestMapping("/jira")
public class JiraRestController {

    @Autowired
    JiraRestService jiraRestService;

    Logger logger = Logger.getLogger(getClass().getName());

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody Object jiraRestPost(@RequestBody Map data) {

        logger.info("Recibido: " + data.get("url") + " Params: " + data.get("params"));

        String method = (String)data.get("method");

        if(method != null && method.equalsIgnoreCase("post")) {
            return jiraRestService.post((String)data.get("url"),Object.class, data.get("params"));
        }

        return jiraRestService.get((String) data.get("url"), Object.class,  data.get("params"));
    }



}
