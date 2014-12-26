package com.corpme.sauron.web;

import com.corpme.sauron.config.ApplicationException;
import com.corpme.sauron.domain.Issue;
import com.corpme.sauron.domain.IssueRepository;
import com.corpme.sauron.service.TemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.logging.Logger;

/**
 * Created by ahg on 14/12/14.
 */
@Controller
@RequestMapping("/issues")
public class IssueController {

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    TemplateUtil templateUtil;

    Logger logger = Logger.getLogger(getClass().getName());

    @RequestMapping(method = RequestMethod.GET, value = "/{issuekey}")
    public String  issue(@PathVariable String issuekey,Model model) {

        Issue issue = issueRepository.findByIssuekey(issuekey);
        if(issue == null) throw new ApplicationException("La página solicitada no existe");

        model.addAttribute("templateUtil",templateUtil);
        model.addAttribute("issue",issue);


        return "issue";
    }



}
