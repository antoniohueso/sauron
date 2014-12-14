package com.corpme.sauron.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Component
public class GlobalDefaultExceptionHandler {

    Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = {Exception.class})
    public @ResponseBody
    List<String> defaultErrorHandler(HttpServletRequest req, HttpServletResponse resp, Exception e) throws Exception {

        logger.error("Exception - GlobalDefaultExceptionHandler", e);

        List<String> err = new ArrayList<String>();

        resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        err.add(e.getMessage());

        return err;
    }

    @ExceptionHandler(value = {ApplicationException.class})
    public @ResponseBody
    List<String> errorApplication(HttpServletRequest req, HttpServletResponse resp, ApplicationException e) throws Exception {

        logger.debug("ApplicationException - GlobalDefaultExceptionHandler", e);

        resp.setStatus(HttpStatus.BAD_REQUEST.value());
        return e.getErrors();
    }
}
