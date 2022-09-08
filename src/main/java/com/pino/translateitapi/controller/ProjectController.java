package com.pino.translateitapi.controller;

import com.pino.translateitapi.model.dto.Project;
import com.pino.translateitapi.model.dto.input.CreateProjectInput;
import com.pino.translateitapi.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ProjectController {

    private final ProjectService projectService;

    @QueryMapping("project")
    public List<Project> getAllProject() {
        return projectService.getAllProject();
    }

    @MutationMapping
    public void createProject(@Argument CreateProjectInput input) {
        projectService.createProject(input);
    }

}
