package com.pino.translateitapi.service;

import com.pino.translateitapi.dao.ProjectLanguageRepository;
import com.pino.translateitapi.dao.ProjectRepository;
import com.pino.translateitapi.model.dto.Project;
import com.pino.translateitapi.model.dto.input.CreateProjectInput;
import com.pino.translateitapi.model.entity.ProjectEntity;
import com.pino.translateitapi.model.entity.ProjectLanguageEntity;
import com.pino.translateitapi.util.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectLanguageRepository projectLanguageRepository;

    public List<Project> getAllProject() {
        return ModelMapperUtils.mapList(projectRepository.findAll(), Project.class);
    }

    public void createProject(CreateProjectInput createProjectInput) {
        // 建立專案
        ProjectEntity projectEntity = ModelMapperUtils.map(createProjectInput, ProjectEntity.class);
        projectRepository.save(projectEntity);
        // 建立主語系
        ProjectLanguageEntity projectLanguageEntity = new ProjectLanguageEntity();
        projectLanguageEntity.setProjectOid(projectEntity.getOid());
        projectLanguageEntity.setLanguageCode(createProjectInput.getSourceLanguageCode());
        projectLanguageEntity.setProgressRate(0);
        projectLanguageRepository.save(projectLanguageEntity);
    }
}
