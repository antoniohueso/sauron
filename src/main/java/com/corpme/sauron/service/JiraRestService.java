package com.corpme.sauron.service;

import com.corpme.sauron.domain.jira.Issue;
import com.corpme.sauron.domain.jira.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JiraRestService {

    @Value("${app.jiraUrl}")
    String jiraUrl;

    @Value("${app.jiraUser}")
    String jiraUser;

    @Value("${app.jiraPwd}")
    String jiraPwd;

    static String loginsession = null;

    final RestTemplate restTemplate = new RestTemplate();

    final Logger logger = LoggerFactory.getLogger(getClass().getName());


    public <T> T get(String url,Class<T> type, Object params){
        return jiraRest(url, HttpMethod.GET, type, params);
    }

    public <T> T get(String url,Class<T> type) {
        return jiraRest(url, HttpMethod.GET, type, null);
    }

    public <T> T post(String url,Class<T> type,Object params) {
        return jiraRest(url, HttpMethod.POST, type, params);
    }

    public <T> T post(String url,Class<T> type) {
        return jiraRest(url, HttpMethod.POST, type, null);
    }

    private <T> T jiraRest(String url,HttpMethod method,Class<T> type,Object params) {

        HashMap loginInfo = null;

        if(loginsession == null) {
            loginsession = login();
        }
        else {

            try {
                return jiraRestImpl(url,method,type,loginsession,params);

            } catch (HttpClientErrorException e) {
                if (e.getStatusCode().value() == HttpStatus.UNAUTHORIZED.value()) {
                    loginsession = login();
                } else {
                    throw e;
                }
            }
        }

        return jiraRestImpl(url,method,type,loginsession,params);

    }

    private <T> T jiraRestImpl(String url,HttpMethod method,Class<T> type,String loginSession,Object params) {

        ResponseEntity<T> response;

        HttpEntity<Object> httpentity;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if(loginSession!=null) {
            headers.add("Cookie", loginSession);
        }

        if(params != null) httpentity = new HttpEntity<Object>(params,headers);
        else httpentity = new HttpEntity<Object>(headers);

        response = restTemplate.exchange(jiraUrl + url, method, httpentity, type);

        return response.getBody();
    }

    private String login() {
        final String urlSession = "/rest/auth/1/session";

        logger.info("Logandose...");

        final HashMap<String,String> credentials = new HashMap();
        credentials.put("username",jiraUser);
        credentials.put("password",jiraPwd);

        final HashMap loginInfo = jiraRestImpl(urlSession,HttpMethod.POST,HashMap.class,null,credentials);

        final HashMap<String,String> session = (HashMap<String,String>)loginInfo.get("session");

        String loginresp = new StringBuilder(session.get("name")).append("=")
                .append(session.get("value")).toString();

        logger.info("Logado, "+loginresp);

        return loginresp;
    }

    public List<Issue> search(final String jql,final int startAt) {

        Map<String,Object> params = new HashMap<>();

        params.put("jql",jql);
        params.put("maxResults","1000");
        params.put("fields",new String[] {"*all"});

        Response response = post("/rest/api/2/search",Response.class,params);

        return response.getIssues().stream().map(responseIssue -> {
            Issue issue = responseIssue.getFields();
            issue.setKey(responseIssue.getKey());
            return issue;
        }).collect(Collectors.toList());


    }
/*
    List<Issue> generateIssues(Response response) {
        response.getIssues().stream().map(r -> {

            Issue issue = new Issue();

            r.getFields().forEach(field -> {
                field.forEach((k,v)->{
                    i
                });
            });
        });
    }
*/
}
