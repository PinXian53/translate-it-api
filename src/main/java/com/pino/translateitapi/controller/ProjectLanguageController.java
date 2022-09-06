package com.pino.translateitapi.controller;

import com.pino.translateitapi.dao.ProjectLanguageRepository;
import com.pino.translateitapi.model.dto.Project;
import com.pino.translateitapi.model.dto.ProjectLanguage;
import com.pino.translateitapi.util.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ProjectLanguageController {

    private final ProjectLanguageRepository projectLanguageRepository;

    @QueryMapping("projectLanguage")
    public List<ProjectLanguage> findProjectLanguage(Integer projectOid) {
        return ModelMapperUtils.mapList(projectLanguageRepository.findByProjectOid(projectOid), ProjectLanguage.class);
    }

    @SchemaMapping
    public List<ProjectLanguage> projectLanguage(Project project) {
        return ModelMapperUtils.mapList(projectLanguageRepository.findByProjectOid(project.getOid()),
            ProjectLanguage.class);
    }
}
