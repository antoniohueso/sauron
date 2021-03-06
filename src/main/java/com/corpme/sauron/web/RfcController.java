package com.corpme.sauron.web;

import com.corpme.sauron.config.ApplicationException;
import com.corpme.sauron.domain.Rfc;
import com.corpme.sauron.service.RfcService;
import com.corpme.sauron.service.TemplateUtil;
import com.corpme.sauron.service.bean.CalendarEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/rfcs")
public class RfcController {

    @Autowired
    RfcService rfcService;

    @Autowired
    TemplateUtil templateUtil;

    Logger logger = Logger.getLogger(getClass().getName());

    @RequestMapping(method = RequestMethod.GET)
    public String resumenRfcs(Model model) {

        Map<String, Collection> resumen = rfcService.resumenRfcs();

        model.addAllAttributes(resumen);

        return "rfcs";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/rfcs-events")
    public @ResponseBody Iterable<CalendarEvent> rfcsEvents(
            @RequestParam String start, @RequestParam String end, @RequestParam(required = false) String filtro) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try {
            return rfcService.rfcsEvents(df.parse(start), df.parse(end), filtro);
        } catch (ParseException e) {
            throw new ApplicationException("Se ha producido un error al parsear las fechas: " + start + " y " + end);
        }

    }


    @RequestMapping(method = RequestMethod.GET, value = "/{key}")
    public String rfc(@PathVariable String key, Model model) {

        model.addAttribute("templateUtil", templateUtil);

        Rfc rfc = rfcService.rfc(key);

        model.addAttribute("rfc", rfc);

        return "rfc";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/listado/{tipo}")
    public String resumenRfcs(@PathVariable String tipo, Model model) {

        Map<String, Collection> resumen = rfcService.resumenRfcs();

        String template = "listadorfcs";

        model.addAttribute("templateUtil", templateUtil);

        if (tipo.equals("vencidas")) {
            model.addAttribute("title", "Listado de rfcs vencidas");
            model.addAttribute("rfcs", resumen.get(tipo));
        } else if (tipo.equals("paradas")) {
            model.addAttribute("title", "Listado de rfcs paradas");
            model.addAttribute("rfcs", resumen.get(tipo));
        } else if (tipo.equals("pendientes")) {
            model.addAttribute("title", "Listado de rfcs pendientes");
            model.addAttribute("rfcs", resumen.get(tipo));
        } else if (tipo.equals("encurso")) {
            model.addAttribute("title", "Listado de rfcs en curso");
            model.addAttribute("rfcs", resumen.get(tipo));
        } else if (tipo.equals("anomalias")) {
            model.addAttribute("title", "Listado de rfcs con anomalías");
            model.addAttribute("rfcs", resumen.get(tipo));
            template = "listadorfcsanomalias";
        } else {
            throw new ApplicationException("La página solicitada no existe");
        }


        return template;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/informe/rfcsencurso")
    public String informeRfcEnCurso(Model model) {


        Map<String, Collection<Rfc>> informe = rfcService.informeRfcsEnCurso();

        model.addAttribute("templateUtil", templateUtil);
        model.addAttribute("informe",informe);

        return "informerfcsencurso";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/informe/rfcsplanificadas")
    public String informeRfcPlanificadas(Model model) {


        Map<String, Collection<Rfc>> informe = rfcService.informeRfcsPlanificadas();

        model.addAttribute("templateUtil", templateUtil);
        model.addAttribute("informe",informe);

        return "informerfcsplanificadas";
    }

}