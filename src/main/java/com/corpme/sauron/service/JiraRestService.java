package com.corpme.sauron.service;

import com.corpme.sauron.domain.jira.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;
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

    final Logger logger = Logger.getLogger(getClass().getName());


    public <T> T get(String url,Class<T> type, Object params) {
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

    public <T> T put(String url,Class<T> type,Object params) {
        return jiraRest(url, HttpMethod.PUT, type, params);
    }

    public <T> T put(String url,Class<T> type) {
        return jiraRest(url, HttpMethod.PUT, type, null);
    }

    private <T> T jiraRest(String url, HttpMethod method, Class<T> type, Object params) {

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

    private <T> T jiraRestImpl(String url, HttpMethod method, Class<T> type, String loginSession, Object params) {

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

        final HashMap loginInfo = jiraRestImpl(urlSession, HttpMethod.POST,HashMap.class,null,credentials);

        final HashMap<String,String> session = (HashMap<String,String>)loginInfo.get("session");

        String loginresp = new StringBuilder(session.get("name")).append("=")
                .append(session.get("value")).toString();

        logger.info("Logado, "+loginresp);

        return loginresp;
    }

    public Issue findIssueByKey(final String key) {
        return parseIssue(get("/rest/api/2/issue/" + key, ResponseIssue.class));
    }


    public List<Issue> search(final String jql) throws ExecutionException, InterruptedException {

        Response response = asyncSearch(jql,0).get();

        final List<Issue> listResponse = parseIssues(response);

        final List<Issue> result = new ArrayList<>();

        if(response.getTotal() > response.getMaxResults()) {

            final List<Future<Response>> asyncList = new ArrayList<>();

            int pages = response.getTotal() / response.getMaxResults();
            if ((response.getTotal() % response.getMaxResults()) > 0) pages++;

            logger.info("Results: "+response.getTotal() + " Max results: "+response.getMaxResults() + " Páginas: "+pages );

            for(int i = 1; i <= pages; i++ ) {
                asyncList.add(asyncSearch(jql, response.getMaxResults() * i));
            }

            asyncList.forEach(f -> {
                try {
                    listResponse.addAll(parseIssues(f.get()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }


        return listResponse;

    }

    @Async
    public Future<Response> asyncSearch(final String jql,int startAt) throws ExecutionException, InterruptedException {

        final Map<String,Object> params = new HashMap<>();

        params.put("jql",jql);
        params.put("fields",new String[] {"*all"});
        params.put("startAt",startAt);
        params.put("maxResults",200);

        return new AsyncResult<>(post("/rest/api/2/search",Response.class,params));
    }

    private List<Issue> parseIssues(Response response) throws ExecutionException, InterruptedException {

        final List<Issue> listResponse = response.getIssues().stream().map(responseIssue -> parseIssue(responseIssue)).collect(Collectors.toList());

        final Map<String,Project> projectsKeys = new HashMap<>();

        final Map <String,Component> idxComponents = new HashMap<>();

        listResponse.forEach(issue -> {
            projectsKeys.put(issue.getProject().getKey(),issue.getProject());
        });

        projectsKeys.forEach((key,project) -> {
            getProjectComponents(key).forEach(c ->{
                idxComponents.put(c.getId(),c);
            });
        });

        final Map<String,Issue> epicas = new HashMap<>();

        final StringBuilder jqlEpica = new StringBuilder();

        final List<Issue> result = new ArrayList<>();
        listResponse.forEach(issue ->{

            //System.out.println(issue.getKey());

            if (issue.getEpicaKey() != null) {
                epicas.put(issue.getEpicaKey(), null);
            }

            if(issue.getComponents() == null || issue.getComponents().size() == 0) {
                result.add(issue);
            }
            else {
                boolean bFirst = true;

                for(Component c : issue.getComponents()) {
                    final Issue newIssue = new Issue();
                    BeanUtils.copyProperties(issue, newIssue);
                    if(bFirst) {
                        bFirst = false;
                    }
                    else {
                        newIssue.setPuntosHistoria(0);
                    }
                    newIssue.setComponent(idxComponents.get(c.getId()));
                    result.add(newIssue);
                };
            }
        });

        //--- Crea el filtro para buscar las épicas
        epicas.forEach((epicaKey,epica) -> {
            if(jqlEpica.length() == 0) {
                jqlEpica.append(epicaKey);
            }
            else {
                jqlEpica.append(",").append(epicaKey);
            }
        });

        //---- Crea el índice de épicas
        if(jqlEpica.length() > 0) {
            search("key in (" + jqlEpica + ")").forEach(epica -> {
                epicas.put(epica.getKey(), epica);
            });

            //--- Añade la épica a los datos de las issues
            result.forEach(issue -> {
                if (issue.getEpicaKey() != null) {
                    issue.setEpica(epicas.get(issue.getEpicaKey()));
                }
            });
        }

        return result;

    }

    private Issue parseIssue(ResponseIssue rissue) {
        Issue issue = rissue.getFields();
        issue.setKey(rissue.getKey());
        issue.setId(rissue.getId());

        if(issue.getIssuelinks() != null) {
            issue.getIssuelinks().stream().forEach(ln ->{
                if (ln.getOutwardIssue() != null) {
                    issue.getIssues().add(parseIssue(ln.getOutwardIssue()));
                }
            });
            issue.getIssuelinks().clear();
        }

        if(issue.getSubtasks() != null) {
            issue.getSubtasks().stream().forEach(st ->{
                issue.getIssues().add(parseIssue(st));
            });
            issue.getSubtasks().clear();
        }


        if(issue.getStatus().getId() == StatusKey.OPEN.getValue()
                || issue.getStatus().getId() == StatusKey.DETENIDA.getValue()
                || issue.getStatus().getId() == StatusKey.DETECTADO_ERROR_PRUEBAS.getValue()) {
            issue.setSituacion("Pendiente");
        }
        else if(issue.getStatus().getId() == StatusKey.DESARROLLANDO.getValue()) {
            issue.setSituacion("En curso");
        }
        else if(issue.getStatus().getId() == StatusKey.RESOLVED.getValue()) {
            issue.setSituacion("Pte. pruebas");
        }
        else if(issue.getStatus().getId() == StatusKey.PROBANDO.getValue()) {
            issue.setSituacion("Probando");
        }
        else {
            issue.setSituacion("Finalizada");
        }

        if(issue.getStatus().getId() == StatusKey.OPEN.getValue()
                || issue.getStatus().getId() == StatusKey.DETENIDA.getValue()
                || issue.getStatus().getId() == StatusKey.DESARROLLANDO.getValue()) {
            issue.setCompletadaSprint("No completado");
            issue.setCompletada(false);
        }
        else {
            issue.setCompletadaSprint("Completado");
            issue.setCompletada(true);
        }

        return issue;
    }

    static class ComponentList extends ArrayList<Component> { }

    public List<Component> getProjectComponents(String projectKey) {
        return get("/rest/api/2/project/"+projectKey+"/components",ComponentList.class);
    }

    public Sprint getSprint(String sprintId) {
        return  get("/rest/agile/1.0/sprint/"+sprintId, Sprint.class);
    }

    public List<Sprint> getProjectSprints(String boardId) {

        ResponseSprints response = get("/rest/agile/1.0/board/"+boardId+"/sprint", ResponseSprints.class);

        final List<Sprint> listResponse = new ArrayList<>(response.getSprints());

        while(!Boolean.parseBoolean(response.getIsLast())) {
            int startAt = response.getStartAt() + response.getMaxResults();
            response = get("/rest/agile/1.0/board/"+boardId+"/sprint?startAt="+startAt, ResponseSprints.class);
            listResponse.addAll(response.getSprints());
        }

        return listResponse;

    }


}
