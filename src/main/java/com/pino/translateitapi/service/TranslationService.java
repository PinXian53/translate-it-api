package com.pino.translateitapi.service;

import com.pino.translateitapi.dao.ProjectLanguageRepository;
import com.pino.translateitapi.dao.ProjectRepository;
import com.pino.translateitapi.dao.TranslationRepository;
import com.pino.translateitapi.model.dto.Translation;
import com.pino.translateitapi.model.dto.input.UpdateTranslationInput;
import com.pino.translateitapi.model.entity.ProjectEntity;
import com.pino.translateitapi.model.entity.ProjectLanguageEntity;
import com.pino.translateitapi.model.entity.TranslationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TranslationService {
    private final ProjectRepository projectRepository;
    private final ProjectLanguageRepository projectLanguageRepository;
    private final TranslationRepository translationRepository;

    @Transactional(readOnly = true)
    public List<Translation> findTranslation(Integer projectLanguageOid) {
        final ProjectLanguageEntity projectLanguageEntity = projectLanguageRepository.findByOid(projectLanguageOid);
        final Integer projectOid = projectLanguageEntity.getProjectOid();
        final ProjectEntity projectEntity = projectRepository.findByOid(projectOid);
        return translationRepository.findByLanguageCode(projectOid, projectLanguageEntity.getLanguageCode(),
            projectEntity.getSourceLanguageCode());
    }

    public void creatOrUpdateTranslation(UpdateTranslationInput input) {
        TranslationEntity translationEntity = translationRepository.findByTranslationKeyOidAndLanguageCode(
            input.getTranslationKeyOid(), input.getLanguageCode());
        if (translationEntity == null) {
            translationEntity = new TranslationEntity();
            translationEntity.setTranslationKeyOid(input.getTranslationKeyOid());
            translationEntity.setLanguageCode(input.getLanguageCode());
        }
        translationEntity.setContent(input.getContent());
        translationRepository.save(translationEntity);
    }
}
