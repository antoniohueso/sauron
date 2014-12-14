package com.corpme.sauron.web;

import com.corpme.sauron.domain.Component;
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

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Map search() {

        Map result = new HashMap<String,Object>();

        result.put("estados",estadoSolicitudService.estados());
        result.put("users",usersService.assignableUsers());
        result.put("tipos", tipoSolicitudesService.tiposSolicitud());
        result.put("projects",jiraService.projects());

        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/components")
    public @ResponseBody Iterable<Component> searchComponents(@RequestBody Map<String,Object> body) {
        return jiraService.components(new Long((Integer)body.get("projectId")));
    }
}
