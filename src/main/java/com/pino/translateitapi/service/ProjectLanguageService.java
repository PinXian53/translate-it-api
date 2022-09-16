package com.pino.translateitapi.service;

import com.pino.translateitapi.dao.ProjectLanguageRepository;
import com.pino.translateitapi.model.dto.Pagination;
import com.pino.translateitapi.model.dto.ProjectLanguage;
import com.pino.translateitapi.model.dto.input.CreateProjectLanguageInput;
import com.pino.translateitapi.model.entity.ProjectEntity;
import com.pino.translateitapi.model.entity.ProjectLanguageEntity;
import com.pino.translateitapi.util.ModelMapperUtils;
import com.pino.translateitapi.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectLanguageService {

    private final ProjectService projectService;
    private final ProjectLanguageRepository projectLanguageRepository;

    @Transactional(readOnly = true)
    public List<ProjectLanguage> findProjectLanguage(Integer projectOid) {
        ProjectEntity projectEntity = projectService.validProjectOidAndReturnEntity(projectOid);
        return toProjectLanguageList(projectEntity.getSourceLanguageCode(),
            projectLanguageRepository.findByProjectOid(projectOid));
    }

    private List<ProjectLanguage> toProjectLanguageList(
        String sourceLanguageCode,
        List<ProjectLanguageEntity> entityList
    ) {
        return entityList.stream().map(entity -> {
            ProjectLanguage projectLanguage = ModelMapperUtils.map(entity, ProjectLanguage.class);
            projectLanguage.setIsSource(sourceLanguageCode.equals(projectLanguage.getLanguageCode()));
            return projectLanguage;
        }).toList();
    }

    @Transactional(readOnly = true)
    public Pagination<ProjectLanguage> findProjectLanguagePage(Integer projectOid, Pageable pageable) {
        ProjectEntity projectEntity = projectService.validProjectOidAndReturnEntity(projectOid);
        return toProjectLanguagePage(
            projectEntity.getSourceLanguageCode(),
            projectLanguageRepository.findByProjectOid(projectOid, pageable),
            pageable
        );
    }

    private Pagination<ProjectLanguage> toProjectLanguagePage(
        String sourceLanguageCode,
        Page<ProjectLanguageEntity> entityPage,
        Pageable pageable
    ) {
        return PageUtils.toPagination(entityPage, pageable, entity -> {
                ProjectLanguage projectLanguage = ModelMapperUtils.map(entity, ProjectLanguage.class);
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
