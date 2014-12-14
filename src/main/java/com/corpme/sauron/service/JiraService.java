package com.corpme.sauron.service;

import com.corpme.sauron.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ahg on 14/12/14.
 */
@Service
public class JiraService {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ComponentRepository componentRepository;

    public Iterable<Project> projects() {
        return projectRepository.findAll();
    }

    public Iterable<Component> components(Long projectId) {
        return componentRepository.findByProject(projectRepository.findOne(projectId));
    }
}
