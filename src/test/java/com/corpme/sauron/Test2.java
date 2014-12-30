package com.corpme.sauron;

import com.corpme.sauron.service.FiestasService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class Test2 {

    @Autowired
    FiestasService service;

    @Test
    public void test(){

        try {
            service.print();

            synchronized (service.getThread()) {
                service.getThread().wait(15000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }

}


