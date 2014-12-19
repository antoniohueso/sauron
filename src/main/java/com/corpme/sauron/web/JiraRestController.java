package com.corpme.sauron.web;

import com.corpme.sauron.service.JiraRestService;
import com.corpme.sauron.service.JiraService;
import com.corpme.sauron.service.SolicitudesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

        String type = (String)data.get("type");

        if(type != null && type.equalsIgnoreCase("post")) {
            return jiraRestService.post((String)data.get("url"),Object.class, (String)data.get("loginsession"),data.get("params"));
        }

        return jiraRestService.get((String) data.get("url"), Object.class, (String) data.get("loginsession"), data.get("params"));

    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Object jiraRestGet(@RequestBody Map data) {

        logger.info("Recibido GET: " + data.get("url") + " Params: " + data.get("params"));

        return jiraRestService.get((String)data.get("url"),Object.class, (String)data.get("loginsession"),data.get("params"));

    }
}
