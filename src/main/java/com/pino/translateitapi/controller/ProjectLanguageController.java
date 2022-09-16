package com.pino.translateitapi.controller;

import com.pino.translateitapi.model.dto.Pagination;
import com.pino.translateitapi.model.dto.Project;
import com.pino.translateitapi.model.dto.ProjectLanguage;
import com.pino.translateitapi.model.dto.input.CreateProjectLanguageInput;
import com.pino.translateitapi.service.ProjectLanguageService;
import com.pino.translateitapi.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ProjectLanguageController {

    private final ProjectLanguageService projectLanguageService;

    @QueryMapping("projectLanguage")
    public List<ProjectLanguage> findProjectLanguage(@Argument Integer projectOid) {
        return projectLanguageService.findProjectLanguage(projectOid);
    }

    @QueryMapping("projectLanguagePage")
    public Pagination<ProjectLanguage> findProjectLanguagePage(
        @Argument Integer projectOid,
        @Argument Integer pageNum,
        @Argument Integer pageSize) {
        return projectLanguageService.findProjectLanguagePage(projectOid, PageUtils.toPageable(pageNum, pageSize));
    }

    @SchemaMapping
    public List<ProjectLanguage> projectLanguage(Project project) {
        return projectLanguageService.findProjectLanguage(project.getOid());
    }

    @MutationMapping
    public void createProjectLanguage(@Argument int projectOid, @Argument CreateProjectLanguageInput input) {
        projectLanguageService.createProjectLanguage(projectOid, input);
    }

    @MutationMapping
    public void deleteProjectLanguage(@Argument int projectLanguageOid) {
        projectLanguageService.deleteProjectLanguage(projectLanguageOid);
    }
}
