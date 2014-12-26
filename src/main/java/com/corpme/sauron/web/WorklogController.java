package com.corpme.sauron.web;

import com.corpme.sauron.config.ApplicationException;
import com.corpme.sauron.domain.User;
import com.corpme.sauron.service.UsersService;
import com.corpme.sauron.service.WorklogService;
import com.corpme.sauron.service.bean.CalendarEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

/**
 * Created by ahg on 14/12/14.
 */
@Controller
@RequestMapping("/worklogs")
public class WorklogController {

    @Autowired
    WorklogService worklogService;

    @Autowired
    UsersService usersService;

    Logger logger = Logger.getLogger(getClass().getName());

    @RequestMapping(method = RequestMethod.GET)
    public String worklogs(Model model) {

        model.addAttribute("users",usersService.usuariosServiciosCentrales());

        return "worklogs";
    }

    @RequestMapping(method = RequestMethod.GET,value = "/worklog-events")
    public @ResponseBody Iterable<CalendarEvent> worklogEvents(
            @RequestParam String start, @RequestParam String end,@RequestParam Long userid) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        User user = usersService.user(userid);

        try {
            return worklogService.worklogEvents(df.parse(start), df.parse(end), user,false);
        } catch (ParseException e) {
            throw new ApplicationException("Se ha producido un error al parsear las fechas: "+start + " y "+end);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/anomalias")
    public String anomalias(Model model) {

        model.addAttribute("users",usersService.usuariosServiciosCentrales());

        return "anomalias";
    }

    @RequestMapping(method = RequestMethod.GET,value = "/anomalias-events")
    public @ResponseBody Iterable<CalendarEvent> anomaliasEvents(
            @RequestParam String start, @RequestParam String end,@RequestParam(required = false) Long userid) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        User user = null;

        if( userid != null ) user = usersService.user(userid);

        try {
            return worklogService.anomalias(df.parse(start), df.parse(end), user);
        } catch (ParseException e) {
            throw new ApplicationException("Se ha producido un error al parsear las fechas: "+start + " y "+end);
        }
    }


    @RequestMapping(method = RequestMethod.GET,value = "/vacaciones")
    public String vacaciones(Model model) {
        model.addAttribute("users",usersService.usuariosServiciosCentrales());
        return "vacaciones";
    }

    @RequestMapping(method = RequestMethod.GET,value = "/vacaciones-events")
    public @ResponseBody Iterable<CalendarEvent> vacacionesEvents(
            @RequestParam String start, @RequestParam String end,@RequestParam(required = false) Long userid) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        User user = null;

        if( userid != null ) user = usersService.user(userid);

        try {
            return worklogService.vacaciones(df.parse(start), df.parse(end),user);
        } catch (ParseException e) {
            throw new ApplicationException("Se ha producido un error al parsear las fechas: "+start + " y "+end);
        }
    }


}
