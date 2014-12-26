package com.corpme.sauron.web;

import com.corpme.sauron.config.ApplicationException;
import com.corpme.sauron.domain.Rfc;
import com.corpme.sauron.service.RfcService;
import com.corpme.sauron.service.bean.CalendarEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public String resumenRfcs(Model model) {
        return "rfcs";
    }

    @RequestMapping(method = RequestMethod.GET,value = "/rfcs-events")
    public @ResponseBody Iterable<CalendarEvent> rfcsEvents(
            @RequestParam String start, @RequestParam String end) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try {
            return rfcService.rfcsEvents(df.parse(start), df.parse(end));
        } catch (ParseException e) {
            throw new ApplicationException("Se ha producido un error al parsear las fechas: "+start + " y "+end);
        }

    }


    @RequestMapping(method = RequestMethod.GET,value = "/{key}")
    public String rfc(@PathVariable String key,Model model) {

        Rfc rfc = rfcService.rfc(key);
        if(rfc == null) {
            throw new ApplicationException("La RFC "+key+ " no existe");
        }

        rfc.setDescription(rfc.getDescription().replaceAll("(\r\n|\n)", "<br/>"));
        rfc.setPlanpasoprod(rfc.getPlanpasoprod().replaceAll("(\r\n|\n)", "<br/>"));
        rfc.setPlanmarchaatras(rfc.getPlanmarchaatras().replaceAll("(\r\n|\n)", "<br/>"));

        model.addAttribute("rfc", rfc);

        return "rfc";
    }
}
