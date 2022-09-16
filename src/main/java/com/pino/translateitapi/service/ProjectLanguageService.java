package com.pino.translateitapi.service;

import com.pino.translateitapi.dao.ProjectLanguageRepository;
import com.pino.translateitapi.dao.ProjectRepository;
import com.pino.translateitapi.model.dto.Pagination;
import com.pino.translateitapi.model.dto.ProjectLanguage;
import com.pino.translateitapi.model.dto.input.CreateProjectLanguageInput;
import com.pino.translateitapi.model.entity.ProjectEntity;
import com.pino.translateitapi.model.entity.ProjectLanguageEntity;
import com.pino.translateitapi.util.ModelMapperUtils;
import com.pino.translateitapi.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectLanguageService {

    private final ProjectService projectService;

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

    @Transactional(readOnly = true)
    public Pagination<ProjectLanguage> findProjectLanguagePage(Integer projectOid, Pageable pageable) {
        final ProjectEntity projectEntity = projectRepository.findByOid(projectOid);
        final String sourceLanguageCode = projectEntity.getSourceLanguageCode();
        return PageUtils.toPagination(
            projectLanguageRepository.findByProjectOid(projectOid, pageable),
            pageable,
            o -> {
                ProjectLanguage projectLanguage = ModelMapperUtils.map(o, ProjectLanguage.class);
                projectLanguage.setIsSource(sourceLanguageCode.equals(projectLanguage.getLanguageCode()));
                return projectLanguage;
            }
        );
    }

    @Transactional
    public void createProjectLanguage(CreateProjectLanguageInput createProjectLanguageInput) {
        projectService.validProjectOid(createProjectLanguageInput.getProjectOid());
        createProjectLanguageToDb(createProjectLanguageInput);
    }

    @Transactional
    public void createProjectLanguageToDb(CreateProjectLanguageInput createProjectLanguageInput) {
        ProjectLanguageEntity entity = ModelMapperUtils.map(createProjectLanguageInput, ProjectLanguageEntity.class);
        entity.setProgressRate(0); // default 0
        projectLanguageRepository.save(entity);
    }
}
