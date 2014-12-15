package com.corpme.sauron.web;

import com.corpme.sauron.config.ApplicationException;
import com.corpme.sauron.domain.*;
import com.corpme.sauron.service.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
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

    - EL EDITOR HACERLO COMO DIRECTIVE PARA QUE NO SE PIERDA LA INFORMACION
    - El LISTADO DE SOLICITUDES NO CACHEA BIEN EL FILTRO

    - El filtro debe guardarse en rootScope cuando se llame a las otros forms y no cuando se haga la selección, si se
    llama desde el catálogo de solicitudes se pone a null, etc, etc


    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public @ResponseBody Solicitud getById(@PathVariable Long id) {
        return solicitudesService.findById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody Iterable<Solicitud> search(
            @RequestParam(required = false) Long estadoId,
            @RequestParam(required = false) Long tipoSolicitudId,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String titulo
            ) {
        return solicitudesService.search(estadoId,tipoSolicitudId,id,titulo);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/datosgenerales")
    @Transactional
    public @ResponseBody Solicitud datosGenerales(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) Long tipoSolicitudId) {

        return solicitudesService.saveDatosGenerales(id,titulo,descripcion,tipoSolicitudId);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/projects")
    public @ResponseBody Iterable<Project> projects() {
        return jiraService.projects();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/components")
    public @ResponseBody Iterable<Component> components(@RequestParam long projectId) {
        return jiraService.components(projectId);
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
