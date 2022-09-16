package com.pino.translateitapi.service;

import com.pino.translateitapi.dao.TranslationKeyRepository;
import com.pino.translateitapi.dao.TranslationRepository;
import com.pino.translateitapi.exception.BadRequestException;
import com.pino.translateitapi.manager.ProjectManager;
import com.pino.translateitapi.model.dto.input.CreateTranslationKeyInput;
import com.pino.translateitapi.model.entity.ProjectEntity;
import com.pino.translateitapi.model.entity.TranslationEntity;
import com.pino.translateitapi.model.entity.TranslationKeyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TranslationKeyService {

    private final TranslationKeyRepository translationKeyRepository;
    private final TranslationRepository translationRepository;

    private final ProjectManager projectManager;

    @Transactional
    public void createTranslationKey(int projectOid, CreateTranslationKeyInput input) {
        ProjectEntity projectEntity = projectManager.validProjectOidAndReturnEntity(projectOid);
        validProjectCanNotExistDuplicateKey(projectEntity.getOid(), input.getKey());
        TranslationKeyEntity translationKeyEntity = createTranslationKeyToDb(projectOid, input.getKey());
        createTranslationToDb(translationKeyEntity.getOid(), projectEntity.getSourceLanguageCode(),
            input.getSourceTranslation());
    }

    public TranslationKeyEntity createTranslationKeyToDb(int projectOid, String key) {
        TranslationKeyEntity translationKeyEntity = new TranslationKeyEntity();
        translationKeyEntity.setProjectOid(projectOid);
        translationKeyEntity.setKey(key);
        return translationKeyRepository.save(translationKeyEntity);
    }

    public void createTranslationToDb(Integer translationKeyOid, String languageCode, String content) {
        TranslationEntity translationEntity = new TranslationEntity();
        translationEntity.setTranslationKeyOid(translationKeyOid);
        translationEntity.setLanguageCode(languageCode);
        translationEntity.setContent(content);
        translationRepository.save(translationEntity);
    }

    public TranslationKeyEntity validTranslationKeyOidAndReturnEntity(Integer translationKeyOid) {
        return Optional.ofNullable(translationKeyRepository.findByOid(translationKeyOid))
            .orElseThrow(() -> new BadRequestException("無法識別之 key"));
    }

    public void validProjectCanNotExistDuplicateKey(int projectOid, String key) {
        if (translationKeyRepository.existsByProjectOidAndKey(projectOid, key)) {
            throw new BadRequestException("Key 已存在，無法重複新增");
        }
    }
}
