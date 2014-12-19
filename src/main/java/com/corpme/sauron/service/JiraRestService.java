package com.corpme.sauron.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Service
public class JiraRestService {

    final RestTemplate restTemplate = new RestTemplate();

    final Logger logger = Logger.getLogger(getClass().getName());


    public <T> T get(String url,Class<T> type,String loginSession, Object params){
        return jiraRest(url, HttpMethod.GET, type, loginSession, params);
    }

    public <T> T get(String url,Class<T> type, String loginSession) {
        return jiraRest(url, HttpMethod.GET, type, loginSession, null);
    }

    public <T> T post(String url,Class<T> type,String loginSession,Object params) {
        return jiraRest(url, HttpMethod.POST, type, loginSession, params);
    }

    public <T> T post(String url,Class<T> type,String loginSession) {
        return jiraRest(url, HttpMethod.POST, type, loginSession, null);
    }

    public <T> T jiraRest(String url,HttpMethod method,Class<T> type,String loginSession,Object params) {

        ResponseEntity<T> response;

        HttpEntity<Object> httpentity;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if(loginSession!=null) {
            headers.add("Cookie", loginSession);
        }

        if(params != null) httpentity = new HttpEntity<Object>(params,headers);
        else httpentity = new HttpEntity<Object>(headers);

        response = restTemplate.exchange("http://10.0.100.118:8085/jira" + url, method, httpentity, type);

        return response.getBody();
    }
}
