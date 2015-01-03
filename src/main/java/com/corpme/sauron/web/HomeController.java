package com.corpme.sauron.web;

import com.corpme.sauron.config.ApplicationException;
import com.corpme.sauron.service.RfcService;
import com.corpme.sauron.service.TemplateUtil;
import com.corpme.sauron.service.WorklogService;
import com.corpme.sauron.service.bean.CalendarEvent;
import com.google.common.collect.Lists;
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
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by ahg on 14/12/14.
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    RfcService rfcService;

    @Autowired
    WorklogService worklogService;

    @Autowired
    TemplateUtil templateUtil;

    Logger logger = Logger.getLogger(getClass().getName());

    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {

        Map<String, Collection> resumen = rfcService.resumenRfcs();

        model.addAllAttributes(resumen);

        return "home";

    }

    @RequestMapping(method = RequestMethod.GET, value = "/home/home-events")
    public @ResponseBody
    Iterable<CalendarEvent> rfcsEvents(
            @RequestParam String start, @RequestParam String end) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Iterable<CalendarEvent> rfcs = rfcService.rfcsEvents(df.parse(start), df.parse(end));

            Iterable<CalendarEvent> vacaciones = worklogService.vacaciones(df.parse(start), df.parse(end),null);

            Collection<CalendarEvent> result = Lists.newArrayList(vacaciones);
            result.addAll(Lists.newArrayList(rfcs));

            return result;

        } catch (ParseException e) {
            throw new ApplicationException("Se ha producido un error al parsear las fechas: " + start + " y " + end);
        }

    }

}
