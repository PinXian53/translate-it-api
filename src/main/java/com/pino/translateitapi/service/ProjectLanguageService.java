package com.pino.translateitapi.service;

import com.pino.translateitapi.dao.ProjectLanguageRepository;
import com.pino.translateitapi.dao.ProjectRepository;
import com.pino.translateitapi.model.dto.ProjectLanguage;
import com.pino.translateitapi.model.dto.input.CreateProjectLanguageInput;
import com.pino.translateitapi.model.entity.ProjectEntity;
import com.pino.translateitapi.model.entity.ProjectLanguageEntity;
import com.pino.translateitapi.util.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectLanguageService {

    private final ProjectRepository projectRepository;
    private final ProjectLanguageRepository projectLanguageRepository;

    @Transactional(readOnly = true)
    public List<ProjectLanguage> findProjectLanguage(Integer projectOid) {
        final ProjectEntity projectEntity = projectRepository.findByOid(projectOid);
        final String sourceLanguageCode = projectEntity.getSourceLanguageCode();
        return projectLanguageRepository.findByProjectOid(projectOid).stream().map(o -> {
            ProjectLanguage projectLanguage = ModelMapperUtils.map(o, ProjectLanguage.class);
            projectLanguage.setIsSource(sourceLanguageCode.equals(projectLanguage.getLanguageCode()));
            return projectLanguage;
        }).toList();
    }

    public void createProjectLanguage(CreateProjectLanguageInput createProjectLanguageInput) {
        projectRepository.findByOid(createProjectLanguageInput.getProjectOid());
        ProjectLanguageEntity entity = ModelMapperUtils.map(createProjectLanguageInput, ProjectLanguageEntity.class);
        entity.setProgressRate(0); // 翻譯進度預設0
        projectLanguageRepository.save(entity);
    }
}
