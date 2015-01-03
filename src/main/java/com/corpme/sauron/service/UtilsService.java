package com.corpme.sauron.service;

import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UtilsService {

    /**
     * Reconoce las urls de una cadena de texto y la transforma en un link de HTML
     * @param cad
     * @return
     */
    public String parseUrlsToHtml(final String cad) {

        if(cad == null) return null;

        final Pattern pattern = Pattern.compile("\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]");
        final Matcher m = pattern.matcher(cad);
        final StringBuffer sb = new StringBuffer();

        while(m.find()){
            final String found = m.group(0);
            m.appendReplacement(sb,"<a target=\"_blank\" href=\"" + found + "\">" + found + "</a>");
        }

        m.appendTail(sb);

        return sb.toString().replaceAll("(\r\n|\n)", "<br/>");

    }

    /**
     * Recibe una fecha y retorna una fecha de tipo Calendar con HH:MM:SS.sss a cero
     * @param fecha
     * @return
     */
    public Calendar getComparableDate(final Date fecha) {

        if(fecha == null) return null;

        final Calendar cal = new GregorianCalendar();
        cal.setTime(fecha);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal;
    }

}
