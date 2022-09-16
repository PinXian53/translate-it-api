package com.pino.translateitapi.service;

import com.pino.translateitapi.dao.ProjectLanguageRepository;
import com.pino.translateitapi.dao.TranslationKeyRepository;
import com.pino.translateitapi.dao.TranslationRepository;
import com.pino.translateitapi.exception.BadRequestException;
import com.pino.translateitapi.manager.ProjectManager;
import com.pino.translateitapi.manager.TranslationManager;
import com.pino.translateitapi.model.dto.Pagination;
import com.pino.translateitapi.model.dto.ProjectLanguage;
import com.pino.translateitapi.model.dto.input.CreateProjectLanguageInput;
import com.pino.translateitapi.model.dto.input.UpdateProjectLanguageInput;
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

    private final LanguageService languageService;
    private final TranslationManager translationManager;

    private final ProjectManager projectManager;

    private final ProjectLanguageRepository projectLanguageRepository;
    private final TranslationKeyRepository translationKeyRepository;
    private final TranslationRepository translationRepository;

    @Transactional(readOnly = true)
    public List<ProjectLanguage> findProjectLanguage(Integer projectOid) {
        ProjectEntity projectEntity = projectManager.validProjectOidAndReturnEntity(projectOid);
        return toProjectLanguageList(projectOid, projectEntity.getSourceLanguageCode(),
            projectLanguageRepository.findByProjectOid(projectOid));
    }

    private List<ProjectLanguage> toProjectLanguageList(
        Integer projectOid,
        String sourceLanguageCode,
        List<ProjectLanguageEntity> entityList
    ) {
        return entityList.stream().map(entity -> {
            ProjectLanguage projectLanguage = ModelMapperUtils.map(entity, ProjectLanguage.class);
            projectLanguage.setIsSource(sourceLanguageCode.equals(projectLanguage.getLanguageCode()));
            projectLanguage.setProgressRate(calculateFinishProgressRate(projectOid, entity.getLanguageCode()));
            return projectLanguage;
        }).toList();
    }

    @Transactional(readOnly = true)
    public Pagination<ProjectLanguage> findProjectLanguagePage(Integer projectOid, Pageable pageable) {
        ProjectEntity projectEntity = projectManager.validProjectOidAndReturnEntity(projectOid);
        return toProjectLanguagePage(
            projectOid,
            projectEntity.getSourceLanguageCode(),
            projectLanguageRepository.findByProjectOid(projectOid, pageable),
            pageable
        );
    }

    private Pagination<ProjectLanguage> toProjectLanguagePage(
        Integer projectOid,
        String sourceLanguageCode,
        Page<ProjectLanguageEntity> entityPage,
        Pageable pageable
    ) {
        return PageUtils.toPagination(entityPage, pageable, entity -> {
                ProjectLanguage projectLanguage = ModelMapperUtils.map(entity, ProjectLanguage.class);
            projectLanguage.setIsSource(sourceLanguageCode.equals(projectLanguage.getLanguageCode()));
            projectLanguage.setProgressRate(calculateFinishProgressRate(projectOid, entity.getLanguageCode()));
                return projectLanguage;
            }
        );
    }

    @Transactional
    public void createProjectLanguage(final int projectOid, CreateProjectLanguageInput createProjectLanguageInput) {
        final String languageCode = createProjectLanguageInput.getLanguageCode();
        projectManager.validProjectOid(projectOid);
        languageService.validLanguageCode(languageCode);
        validProjectCanNotCreateDuplicateLanguageCode(projectOid, languageCode);
        createProjectLanguageToDb(projectOid, languageCode);
    }

    @Transactional
    public void updateProjectLanguageToDb(
        final int projectLanguageOid,
        UpdateProjectLanguageInput createProjectLanguageInput) {
        ProjectLanguageEntity projectLanguage = validProjectLanguageOidAndReturnEntity(projectLanguageOid);
        projectLanguage.setEnable(createProjectLanguageInput.isEnable());
        projectLanguageRepository.save(projectLanguage);
    }

    @Transactional
    public void createProjectLanguageToDb(final int projectOid, String languageCode) {
        ProjectLanguageEntity entity = new ProjectLanguageEntity();
        entity.setProjectOid(projectOid);
        entity.setLanguageCode(languageCode);
        entity.setEnable(true); // default true
        projectLanguageRepository.save(entity);
    }

    @Transactional
    public void deleteProjectLanguage(int projectLanguageOid) {
        ProjectLanguageEntity projectLanguage = validProjectLanguageOidAndReturnEntity(projectLanguageOid);
        ProjectEntity projectEntity = projectManager.validProjectOidAndReturnEntity(projectLanguage.getProjectOid());
        validDeleteLanguageCodeCanNotEqualSourceLanguageCode(projectEntity.getSourceLanguageCode(),
            projectLanguage.getLanguageCode());
        translationManager.deleteByProjectOidAndLanguageCode(projectLanguageOid, projectLanguage.getLanguageCode());
        projectLanguageRepository.delete(projectLanguage);
    }

    public ProjectLanguageEntity validProjectLanguageOidAndReturnEntity(int projectLanguageOid) {
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

    private long calculateFinishProgressRate(int projectOid, String languageCode) {
        long totalCount = translationKeyRepository.countByProjectOid(projectOid);
        if (totalCount == 0) {
            return 0;
        }
        long finishedCount = translationRepository.countFinishCount(projectOid, languageCode);
        return Math.round((float) (finishedCount * 100) / totalCount);
    }
}
