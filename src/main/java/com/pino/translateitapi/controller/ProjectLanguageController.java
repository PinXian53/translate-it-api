package com.pino.translateitapi.controller;

import com.pino.translateitapi.model.dto.Project;
import com.pino.translateitapi.model.dto.ProjectLanguage;
import com.pino.translateitapi.service.ProjectLanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ProjectLanguageController {

    private final ProjectLanguageService projectLanguageService;

    @QueryMapping("projectLanguage")
    public List<ProjectLanguage> findProjectLanguage(Integer projectOid) {
        return projectLanguageService.findProjectLanguage(projectOid);
    }

    @SchemaMapping
    public List<ProjectLanguage> projectLanguage(Project project) {
        return projectLanguageService.findProjectLanguage(project.getOid());
    }
}
