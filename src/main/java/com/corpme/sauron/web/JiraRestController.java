package com.corpme.sauron.web;

import com.corpme.sauron.service.JiraRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
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

    @RequestMapping(method = RequestMethod.GET,value =  "/rest/**")
    public @ResponseBody
    Object get(HttpServletRequest request) throws Exception{

        Enumeration<String> params = request.getParameterNames();
        StringBuilder builder = new StringBuilder();
        while( params.hasMoreElements() ) {
            String param = params.nextElement();
            if(builder.length() == 0) builder.append(param).append("=").append(request.getParameter(param));
            else builder.append("&").append(param).append("=").append(request.getParameter(param));

        }

        StringBuilder url = new StringBuilder(request.getRequestURI().replaceFirst("\\/jira",""));

        if(builder.length() > 0) {
            url.append("?").append(builder.toString());
        }

        logger.info("Recibido: GET: " + url.toString());

        return jiraRestService.get( url.toString(), Object.class);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/rest/**")
    public @ResponseBody
    Object post(HttpServletRequest request, @RequestBody Object data) throws Exception{

        logger.info("Recibido: POST: " + request.getRequestURI() + " Params: " + data);

        return jiraRestService.post(request.getRequestURI().replaceFirst("\\/jira", ""), Object.class, data);

    }

    @RequestMapping(method = RequestMethod.PUT,value = "/rest/**")
    public @ResponseBody
    Object put(HttpServletRequest request, @RequestBody Object data) throws Exception{

        logger.info("Recibido: PUT: " + request.getRequestURI() + " Params: " + data);

        return jiraRestService.put(request.getRequestURI().replaceFirst("\\/jira", ""), Object.class, data);

    }
    

}
