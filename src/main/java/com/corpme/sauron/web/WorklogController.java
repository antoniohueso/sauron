package com.corpme.sauron.web;

import com.corpme.sauron.domain.Issue;
import com.corpme.sauron.domain.IssueRepository;
import com.corpme.sauron.domain.WorklogsRepository;
import com.corpme.sauron.service.WorklogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.GregorianCalendar;
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
    public String anomalias(Model model) {

        Calendar fdesde = new GregorianCalendar();
        Calendar fhasta = new GregorianCalendar();
        fhasta.add(Calendar.DAY_OF_MONTH,-1);
        fhasta.set(Calendar.HOUR_OF_DAY, 0);
        fhasta.set(Calendar.MINUTE, 0);
        fhasta.set(Calendar.SECOND, 0);
        fhasta.set(Calendar.MILLISECOND, 0);

        fdesde.add(Calendar.DAY_OF_MONTH, -15);
        fdesde.set(Calendar.HOUR_OF_DAY, 0);
        fdesde.set(Calendar.HOUR_OF_DAY, 0);
        fdesde.set(Calendar.MINUTE, 0);
        fdesde.set(Calendar.SECOND, 0);
        fdesde.set(Calendar.MILLISECOND, 0);


        logger.info(fdesde.getTime()+"");

        model.addAttribute("fdesde",fdesde.getTime());
        model.addAttribute("fhasta",fhasta.getTime());

        model.addAttribute("anomalias", worklogService.worklogs(fdesde.getTime(),fhasta.getTime()));


        return "worklog";
    }



}
