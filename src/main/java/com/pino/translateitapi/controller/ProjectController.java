package com.pino.translateitapi.controller;

import com.pino.translateitapi.model.dto.Pagination;
import com.pino.translateitapi.model.dto.Project;
import com.pino.translateitapi.model.dto.input.CreateProjectInput;
import com.pino.translateitapi.model.dto.input.UpdateProjectInput;
import com.pino.translateitapi.service.ProjectService;
import com.pino.translateitapi.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ProjectController {

    private final ProjectService projectService;

    @QueryMapping("project")
    public List<Project> findProject() {
        return projectService.findProject();
    }

    @QueryMapping("projectPage")
    public Pagination<Project> findProjectPage(@Argument Integer pageNum, @Argument Integer pageSize) {
        return projectService.findProjectPage(PageUtils.toPageable(pageNum, pageSize));
    }

    @MutationMapping
    public void createProject(@Argument @Valid CreateProjectInput input) {
        projectService.createProject(input);
    }

    @MutationMapping
    public void updateProject(@Argument int projectOid, @Argument @Valid UpdateProjectInput input) {
        projectService.updateProject(projectOid, input);
    }

}
