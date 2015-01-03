package com.corpme.sauron;

import com.corpme.sauron.service.FiestasService;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;


public class Test2 {

    public static void main(String args[]) {
        new Test2().test();
    }

    public void test(){


        Collection<String> coll = Lists.newArrayList("aaa","bbb","cccc");

        coll.forEach((s) -> println(s));


    }

    static void println(String s) {
        System.out.println(s);
    }
}


