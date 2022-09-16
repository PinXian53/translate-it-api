package com.pino.translateitapi.service;

import com.pino.translateitapi.dao.ProjectLanguageRepository;
import com.pino.translateitapi.exception.BadRequestException;
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
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectLanguageService {

    private final ProjectService projectService;
    private final ProjectLanguageRepository projectLanguageRepository;
    private final TranslationService translationService;
    private final LanguageService languageService;

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
    public void createProjectLanguage(final int projectOid, CreateProjectLanguageInput createProjectLanguageInput) {
        final String languageCode = createProjectLanguageInput.getLanguageCode();
        projectService.validProjectOid(projectOid);
        languageService.validLanguageCode(languageCode);
        validProjectCanNotCreateDuplicateLanguageCode(projectOid, languageCode);
        createProjectLanguageToDb(projectOid, languageCode);
    }

    @Transactional
    public void createProjectLanguageToDb(final int projectOid, String languageCode) {
        ProjectLanguageEntity entity = new ProjectLanguageEntity();
        entity.setProjectOid(projectOid);
        entity.setLanguageCode(languageCode);
        entity.setProgressRate(0); // default 0
        projectLanguageRepository.save(entity);
    }

    @Transactional
    public void deleteProjectLanguage(int projectLanguageOid) {
        ProjectLanguageEntity projectLanguage = validProjectOidAndReturnEntity(projectLanguageOid);
        ProjectEntity projectEntity = projectService.validProjectOidAndReturnEntity(projectLanguage.getProjectOid());
        validDeleteLanguageCodeCanNotEqualSourceLanguageCode(projectEntity.getSourceLanguageCode(),
            projectLanguage.getLanguageCode());
        translationService.deleteByProjectOidAndLanguageCode(projectLanguageOid, projectLanguage.getLanguageCode());
        projectLanguageRepository.delete(projectLanguage);
    }

    public ProjectLanguageEntity validProjectOidAndReturnEntity(int projectLanguageOid) {
        return Optional.ofNullable(projectLanguageRepository.findByOid(projectLanguageOid))
            .orElseThrow(() -> new BadRequestException("無法識別之專案語系"));
    }

    private void validDeleteLanguageCodeCanNotEqualSourceLanguageCode(
        String sourceLanguageCode,
        String deleteLanguageCode) {
        if (sourceLanguageCode.equals(deleteLanguageCode)) {
            throw new BadRequestException("主語系無法刪除");
        }
    }

    private void validProjectCanNotCreateDuplicateLanguageCode(int projectOid, String createLanguageCode) {
        if (projectLanguageRepository.existsByProjectOidAndLanguageCode(projectOid, createLanguageCode)) {
            throw new BadRequestException("語系已存在，無法重複新增");
        }
    }
}
