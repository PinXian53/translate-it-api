package com.pino.translateitapi.controller;

import com.pino.translateitapi.dao.ProjectRepository;
import com.pino.translateitapi.model.dto.Project;
import com.pino.translateitapi.util.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ProjectController {

    private final ProjectRepository projectRepository;

    @QueryMapping("project")
    public List<Project> getAllProject() {
        return ModelMapperUtils.mapList(projectRepository.findAll(), Project.class);
    }

}
