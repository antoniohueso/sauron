package com.corpme.sauron;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ahg on 28/12/14.
 */
public class Test2 {

    public static void main(String args[]) {

        String cad = " Hola capullo http://www.google.es \n" +
                "www.caca.com";

        Pattern pattern = Pattern.compile("\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]");
        Matcher m = pattern.matcher(cad); // put here the line you want to check
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            String found = m.group(0);

            m.appendReplacement(sb, "<a href=\"" + found + "\">" + found + "</a>");
        }

        m.appendTail(sb);

        System.out.println(sb.toString());


    }

}
