import com.corpme.sauron.Application;
import com.corpme.sauron.service.JiraRestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by ahg on 19/12/14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class,
        initializers = ConfigFileApplicationContextInitializer.class)
public class TestJira {

    @Autowired
    JiraRestService service;

    @Test
    public void testJira() {

        service.get("/rest/api/2/issue/RFC-1", HashMap.class, null);

        HashMap map = service.get("/rest/api/2/issue/RFC-1", HashMap.class, null);

        Logger.getGlobal().info("JODER: "+map);

    }

}
