package com.corpme.sauron.web;

import com.corpme.sauron.config.ApplicationException;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by ahg on 14/12/14.
 */
@Controller
@RequestMapping("/worklogs")
public class WorklogController {

    @Autowired
    WorklogService worklogService;

    Logger logger = Logger.getLogger(getClass().getName());

    @RequestMapping(method = RequestMethod.GET,value = "/anomalias")
    public String anomalias(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Date fdesde,
            @RequestParam(required = false) Date fhasta,
            Model model) {

        helperCalculaFechas(mes,fdesde,fhasta,model);

        final Calendar fechaDesde = new GregorianCalendar();
        final Map mmodel = model.asMap();
        final Calendar fechaHasta = new GregorianCalendar();

        fechaDesde.setTime((Date)mmodel.get("fdesde"));
        fechaHasta.setTime((Date)mmodel.get("fhasta"));
        model.addAttribute("anomalias", worklogService.worklogs(fechaDesde.getTime(),fechaHasta.getTime()));

        return "worklog";
    }

    @RequestMapping(method = RequestMethod.GET,value = "/vacaciones")
    public String vacaciones(Model model) {
        return "vacaciones";
    }

    @RequestMapping(method = RequestMethod.GET,value = "/vacaciones-events")
    public @ResponseBody Iterable<CalendarEvent> vacacionesEvents(
            @RequestParam String start, @RequestParam String end,Model model) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try {
            helperCalculaFechas(null, df.parse(start), df.parse(end), model);
        } catch (ParseException e) {
            throw new ApplicationException("Se ha producido un error al parsear las fechas: "+start + " y "+end);
        }

        final Calendar fechaDesde = new GregorianCalendar();
        final Map mmodel = model.asMap();
        final Calendar fechaHasta = new GregorianCalendar();

        fechaDesde.setTime((Date)mmodel.get("fdesde"));
        fechaHasta.setTime((Date)mmodel.get("fhasta"));

        return worklogService.vacaciones(fechaDesde.getTime(),fechaHasta.getTime());
    }


    void helperCalculaFechas(Integer mes,Date fdesde, Date fhasta,Model model) {
        final Calendar fechaDesde = new GregorianCalendar();
        final Calendar fechaHasta = new GregorianCalendar();

        if(mes == null && fdesde == null && fhasta == null) {
            fechaDesde.set(Calendar.DAY_OF_MONTH,fechaDesde.getActualMinimum(Calendar.DAY_OF_MONTH));
        }
        else if(mes != null) {
            fechaDesde.set(Calendar.MONTH,mes.intValue() - 1);
            fechaHasta.set(Calendar.MONTH,mes.intValue() - 1);
            fechaDesde.set(Calendar.DAY_OF_MONTH,fechaDesde.getActualMinimum(Calendar.DAY_OF_MONTH));
            fechaHasta.set(Calendar.DAY_OF_MONTH,fechaDesde.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        else {
            if(fdesde == null) {
                fechaDesde.setTime(fhasta);
                fechaHasta.setTime(fhasta);
            }
            else if(fhasta == null) {
                fechaDesde.setTime(fdesde);
                fechaHasta.setTime(fdesde);
            }
            else {
                fechaDesde.setTime(fdesde);
                fechaHasta.setTime(fhasta);
            }
        }


        fechaHasta.set(Calendar.HOUR_OF_DAY, 0);
        fechaHasta.set(Calendar.MINUTE, 0);
        fechaHasta.set(Calendar.SECOND, 0);
        fechaHasta.set(Calendar.MILLISECOND, 0);

        fechaDesde.set(Calendar.HOUR_OF_DAY, 0);
        fechaDesde.set(Calendar.MINUTE, 0);
        fechaDesde.set(Calendar.SECOND, 0);
        fechaDesde.set(Calendar.MILLISECOND, 0);

        model.addAttribute("fdesde",fechaDesde.getTime());
        model.addAttribute("fhasta",fechaHasta.getTime());
        if(mes != null) {
            DateFormat df = new SimpleDateFormat("MMMM 'de' yyyy");
            model.addAttribute("mes",df.format(fechaDesde.getTime()));
        }
    }

}
