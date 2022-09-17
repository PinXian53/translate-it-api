package com.pino.translateitapi.service;

import com.pino.translateitapi.dao.ProjectLanguageRepository;
import com.pino.translateitapi.dao.TranslationRepository;
import com.pino.translateitapi.exception.BadRequestException;
import com.pino.translateitapi.manager.ProjectManager;
import com.pino.translateitapi.manager.TranslationManager;
import com.pino.translateitapi.model.dto.Pagination;
import com.pino.translateitapi.model.dto.Translation;
import com.pino.translateitapi.model.dto.input.UpdateTranslationInput;
import com.pino.translateitapi.model.entity.ProjectEntity;
import com.pino.translateitapi.model.entity.ProjectLanguageEntity;
import com.pino.translateitapi.model.entity.TranslationEntity;
import com.pino.translateitapi.model.entity.TranslationKeyEntity;
import com.pino.translateitapi.util.BatchUtils;
import com.pino.translateitapi.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TranslationService {

    private final TranslationKeyService translationKeyService;
    private final LanguageService languageService;

    private final ProjectManager projectManager;
    private final TranslationManager translationManager;

    private final ProjectLanguageService projectLanguageService;
    private final TranslationRepository translationRepository;
    private final ProjectLanguageRepository projectLanguageRepository;

    @Transactional(readOnly = true)
    public List<Translation> findTranslation(Integer projectLanguageOid) {
        ProjectLanguageEntity projectLanguageEntity = projectLanguageService.validProjectLanguageOidAndReturnEntity(
            projectLanguageOid);
        ProjectEntity projectEntity = projectManager.validProjectOidAndReturnEntity(
            projectLanguageEntity.getProjectOid());
        return translationRepository.findByLanguageCode(projectLanguageEntity.getProjectOid(),
            projectLanguageEntity.getLanguageCode(),
            projectEntity.getSourceLanguageCode(), Pageable.unpaged()).getContent();
    }

    @Transactional(readOnly = true)
    public Pagination<Translation> findTranslationPage(Integer projectLanguageOid, Pageable pageable) {
        ProjectLanguageEntity projectLanguageEntity = projectLanguageService.validProjectLanguageOidAndReturnEntity(
            projectLanguageOid);
        ProjectEntity projectEntity = projectManager.validProjectOidAndReturnEntity(
            projectLanguageEntity.getProjectOid());
        return PageUtils.toPagination(translationRepository.findByLanguageCode(projectLanguageEntity.getProjectOid(),
            projectLanguageEntity.getLanguageCode(), projectEntity.getSourceLanguageCode(), pageable), pageable);
    }

    @Transactional
    public void creatOrUpdateTranslation(int translationKeyOid, String languageCode, UpdateTranslationInput input) {
        TranslationKeyEntity translationKeyEntity = translationKeyService.validTranslationKeyOidAndReturnEntity(
            translationKeyOid);
        validProjectNeedExistLanguageCode(translationKeyEntity.getProjectOid(), languageCode);
        languageService.validLanguageCode(languageCode);
        creatOrUpdateTranslationToDb(translationKeyOid, languageCode, input.getContent());
    }

    private void creatOrUpdateTranslationToDb(int translationKeyOid, String languageCode, String content) {
        TranslationEntity translationEntity = translationRepository.findByTranslationKeyOidAndLanguageCode(
            translationKeyOid, languageCode);
        if (translationEntity == null) {
            translationEntity = new TranslationEntity();
            translationEntity.setTranslationKeyOid(translationKeyOid);
            translationEntity.setLanguageCode(languageCode);
        }
        translationEntity.setContent(StringUtils.isEmpty(content) ? null : content.trim());
        translationRepository.save(translationEntity);
    }

    @Transactional
    public void deleteByProjectOid(int projectOid) {
        List<Integer> translationKeyOidList = translationManager.findTranslationKeyOidByProjectOid(projectOid);
        BatchUtils.subBatchIterator(translationKeyOidList, 500, translationRepository::deleteByTranslationKeyOidIn);
    }

    public void validProjectNeedExistLanguageCode(int projectOid, String createLanguageCode) {
        if (!projectLanguageRepository.existsByProjectOidAndLanguageCode(projectOid, createLanguageCode)) {
            throw new BadRequestException("語系資料錯誤");
        }
    }
}
