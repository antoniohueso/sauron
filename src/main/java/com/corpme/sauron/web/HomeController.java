package com.corpme.sauron.web;

import com.corpme.sauron.service.SolicitudesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ahg on 14/12/14.
 */
@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    SolicitudesService solicitudesService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Map cuadroDeMandos() {

        Map result = new HashMap<String,Object>();

        result.put("resumenSolicitudes",solicitudesService.resumenSolicitudes());

        return result;
    }
}
