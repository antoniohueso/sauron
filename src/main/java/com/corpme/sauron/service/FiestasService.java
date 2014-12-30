package com.corpme.sauron.service;

import com.corpme.sauron.domain.Fiesta;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.component.VEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ahg on 30/12/14.
 */
@Service
@Scope
public class FiestasService {

    @Value("${app.fiestas.file}")
    String fiestasFile;

    Logger logger = LoggerFactory.getLogger(getClass());

    Map<String,List<Fiesta>> fiestas = Collections.synchronizedMap(new TreeMap());

    Thread thread = null;

    DateFormat df = new SimpleDateFormat("yyyMmdd");

    @PostConstruct
    void init() {
        thread = new Thread(new LoadIcal());
        thread.start();
    }

    public Thread getThread() {
        return thread;
    }

    class LoadIcal implements Runnable {

        FileTime lastLoadTime = null;

        boolean fin = false;

        @Override
        public void run() {

            while(!fin) {

                try {
                    Path file = FileSystems.getDefault().getPath(fiestasFile);
                    FileTime time = Files.getLastModifiedTime(file);

                    if(lastLoadTime == null || lastLoadTime.compareTo(time) != 0) {
                        load();
                        lastLoadTime = time;
                    }

                    Thread.currentThread().sleep(5000L);
                } catch (Exception e) {
                    logger.error("Error: ", e);
                    fin = true;
                }
            }


        }

        public void finalizar() {
            fin = true;
        }

        void load() {

            FileInputStream fin = null;

            try {

                logger.info("Cargando calendario de fiestas: "+fiestasFile);

                fiestas.clear();

                fin = new FileInputStream(fiestasFile);

                CalendarBuilder builder = new CalendarBuilder();

                net.fortuna.ical4j.model.Calendar calendar = builder.build(fin);

                for (Object ocal : calendar.getComponents()) {
                    if (ocal instanceof VEvent) {

                        VEvent event = (VEvent) ocal;

                        if(event.getDescription() == null
                          || event.getDescription().getValue().trim().length() >= 0
                          || (event.getDescription().getValue().toLowerCase().indexOf("madrid") >= 0)
                              /*&& event.getDescription().getValue().toLowerCase().indexOf("observado") < 0*/) {

                            Date fecha = event.getStartDate().getDate();

                            List<Fiesta> afiestas = fiestas.get(df.format(fecha));
                            if (afiestas == null) {
                                afiestas = new ArrayList<Fiesta>();
                                fiestas.put(df.format(fecha), afiestas);
                            }

                            String summary = null;
                            if (event.getSummary() != null) summary = event.getSummary().getValue();
                            String description = null;
                            if (event.getDescription() != null)
                                description = event.getDescription().getValue();

                            afiestas.add(new Fiesta(fecha, summary, description));
                        }
                    }
                }

                logger.info("Ok calendario de fiestas: "+fiestasFile);
            }
            catch(Exception e) {
                logger.error("Error al cargar las fiestas del fichero: "+fiestasFile,e);
            }
            finally {
                try {
                    if (fin != null) fin.close();
                }
                catch(IOException e) {
                    logger.error("Error al cerrar el fichero: "+fiestasFile,e);
                }
            }
        }
    }

    public void print() {

        for(String key : fiestas.keySet()) {

            logger.info("----------- Día de Fiesta: "+key);

            for(Fiesta fiesta : fiestas.get(key)) {
                logger.info(fiesta.getSummary() + " DESC: " + fiesta.getDescription());
            }
        }

    }

    public List<Fiesta> getFiestas(Date fecha) {
        return fiestas.get(df.format(fecha));
    }
}
