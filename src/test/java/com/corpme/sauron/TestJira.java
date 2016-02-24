package com.corpme.sauron;

import com.corpme.sauron.domain.jira.Response;
import com.corpme.sauron.service.JiraRestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by ahg on 27/12/14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class TestJira {

    @Autowired
    JiraRestService jiraRestService;

    Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void testJira() {

        String jql="key = 'RFC-1'";
/*
        Response resp = jiraRestService.search(jql,0);

        resp.getIssues().forEach((issue) ->{
            HashMap<String,Object> hm = (HashMap<String,Object>)issue;

            hm = (HashMap<String,Object>)hm.get("fields");

            hm.forEach((k,v) ->{
                System.out.println(k + " -> " + (v!=null?v.toString().replaceAll("\n","").replaceAll("\r",""):"Null"));
            });
        });
*/

        jiraRestService.search(jql,0).forEach(issue ->  {
            System.out.println(issue.getSummary());
            System.out.println(issue.getAssignee().getName());
        });
    }

}
