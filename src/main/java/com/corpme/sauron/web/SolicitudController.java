package com.corpme.sauron.web;

import com.corpme.sauron.domain.*;
import com.corpme.sauron.service.*;
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
@RequestMapping("/solicitudes")
public class SolicitudController {

    @Autowired SolicitudesService solicitudesService;

    @Autowired EstadoSolicitudService estadoSolicitudService;

    @Autowired UsersService usersService;

    @Autowired TipoSolicitudesService tipoSolicitudesService;

    @Autowired JiraService jiraService;

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody Iterable<Solicitud> search(@RequestBody Map<String,Object> body) {
        return solicitudesService.search(body);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/projects")
    public @ResponseBody Iterable<Project> projects() {
        return jiraService.projects();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/components")
    public @ResponseBody Iterable<Component> components(@RequestBody Map<String,Object> body) {
        return jiraService.components(new Long((Integer)body.get("projectId")));
    }



    @RequestMapping(method = RequestMethod.GET,value = "/tipos")
    public @ResponseBody Iterable<TipoSolicitud> tipos() {
        return tipoSolicitudesService.tiposSolicitud();
    }

    @RequestMapping(method = RequestMethod.GET,value = "/users")
    public @ResponseBody Iterable<User> users() {
        return usersService.assignableUsers();
    }

    @RequestMapping(method = RequestMethod.GET,value = "/estados")
    public @ResponseBody Iterable<EstadoSolicitud> estados() {
        return estadoSolicitudService.estados();
    }


}
