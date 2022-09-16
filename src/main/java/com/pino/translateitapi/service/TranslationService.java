package com.pino.translateitapi.service;

import com.pino.translateitapi.dao.ProjectLanguageRepository;
import com.pino.translateitapi.dao.ProjectRepository;
import com.pino.translateitapi.dao.TranslationKeyRepository;
import com.pino.translateitapi.dao.TranslationRepository;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TranslationService {
    private final ProjectRepository projectRepository;
    private final ProjectLanguageRepository projectLanguageRepository;
    private final TranslationRepository translationRepository;
    private final TranslationKeyRepository translationKeyRepository;

    @Transactional(readOnly = true)
    public List<Translation> findTranslation(Integer projectLanguageOid) {
        final ProjectLanguageEntity projectLanguageEntity = projectLanguageRepository.findByOid(projectLanguageOid);
        final Integer projectOid = projectLanguageEntity.getProjectOid();
        final ProjectEntity projectEntity = projectRepository.findByOid(projectOid);
        return translationRepository.findByLanguageCode(projectOid, projectLanguageEntity.getLanguageCode(),
            projectEntity.getSourceLanguageCode(), Pageable.unpaged()).getContent();
    }

    @Transactional(readOnly = true)
    public Pagination<Translation> findTranslationPage(Integer projectLanguageOid, Pageable pageable) {
        final ProjectLanguageEntity projectLanguageEntity = projectLanguageRepository.findByOid(projectLanguageOid);
        final Integer projectOid = projectLanguageEntity.getProjectOid();
        final ProjectEntity projectEntity = projectRepository.findByOid(projectOid);
        return PageUtils.toPagination(translationRepository.findByLanguageCode(projectOid,
            projectLanguageEntity.getLanguageCode(), projectEntity.getSourceLanguageCode(), pageable), pageable);
    }

    public void creatOrUpdateTranslation(int translationKeyOid, String languageCode, UpdateTranslationInput input) {
        TranslationEntity translationEntity = translationRepository.findByTranslationKeyOidAndLanguageCode(
            translationKeyOid, languageCode);
        if (translationEntity == null) {
            translationEntity = new TranslationEntity();
            translationEntity.setTranslationKeyOid(translationKeyOid);
            translationEntity.setLanguageCode(languageCode);
        }
        translationEntity.setContent(input.getContent());
        translationRepository.save(translationEntity);
    }

    @Transactional
    public void deleteByProjectOid(int projectOid) {
        List<Integer> translationKeyOidList = findTranslationKeyOidByProjectOid(projectOid);
        BatchUtils.subBatchIterator(translationKeyOidList, 500, translationRepository::deleteByTranslationKeyOidIn);
    }

    @Transactional
    public void deleteByProjectOidAndLanguageCode(int projectOid, final String languageCode) {
        List<Integer> translationKeyOidList = findTranslationKeyOidByProjectOid(projectOid);
        BatchUtils.subBatchIterator(translationKeyOidList, 500,
            subOidList -> translationRepository.deleteByTranslationKeyOidInAndLanguageCode(subOidList, languageCode));
    }

    private List<Integer> findTranslationKeyOidByProjectOid(int projectOid) {
        return translationKeyRepository.findByProjectOid(projectOid)
            .stream()
            .map(TranslationKeyEntity::getOid)
            .toList();
    }
}
