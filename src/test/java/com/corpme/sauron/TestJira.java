package com.corpme.sauron;

import com.corpme.sauron.service.JiraRestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;

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

        HashMap<String,Object> params = new HashMap<>();
        params.put("jql",jql);
        params.put("maxResults","1000");
        params.put("fields",new String[] {"*all"});


        HashMap h = jiraRestService.post("/rest/api/2/search", HashMap.class, params);

        logger.info("------------------------------------------------------------------------");
        logger.info("-----------------------------  INICIO ----------------------------------");
        logger.info("------------------------------------------------------------------------");


        logger.info(h.toString());

        List<HashMap> issues = (List)h.get("issues");

        for(HashMap issue : issues) {
            HashMap h1 =(HashMap)issue.get("fields");

            logger.info(h1.get("customfield_10328").toString());
        }



        logger.info("------------------------------------------------------------------------");
        logger.info("-----------------------------  FIN -------------------------------------");
        logger.info("------------------------------------------------------------------------");
    }

}
