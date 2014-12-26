package com.corpme.sauron.service;

import com.corpme.sauron.domain.Status;
import com.corpme.sauron.domain.StatusKey;
import org.springframework.stereotype.Service;

/**
 * Created by ahg on 26/12/14.
 */
@Service
public class TemplateUtil {

    public String statusClass(Status status) {

        if(status.getId() == StatusKey.OPEN.getValue()
                || status.getId() == StatusKey.DETENIDA.getValue()) {
            if(status.getId() == StatusKey.DETENIDA.getValue()) {
                return "label-default glyphicon glyphicon-ban-circle";
            }
            else return "label-default";
        }

        if(status.getId() == StatusKey.DESARROLLANDO.getValue()
                || status.getId() == StatusKey.RESOLVED.getValue()) {
            return "label-primary";
        }

        if(status.getId() == StatusKey.DISPONIBLE_PARA_PRUEBAS.getValue()
                || status.getId() == StatusKey.PROBANDO.getValue()
                || status.getId() == StatusKey.DETECTADO_ERROR_PRUEBAS.getValue()
                ) {
            return "label-warning";
        }

        if(status.getId() == StatusKey.FINALIZADA.getValue()) {
            return "label-success";
        }

        if(status.getId() == StatusKey.EN_PRODUCCION.getValue()
                || status.getId() == StatusKey.CERRADA.getValue()) {
            return "glyphicon glyphicon-ok label-success";
        }

        return "label-default";
    }

}
