package com.pino.translateitapi.service;

import com.pino.translateitapi.dao.ProjectRepository;
import com.pino.translateitapi.dao.TranslationKeyRepository;
import com.pino.translateitapi.dao.TranslationRepository;
import com.pino.translateitapi.exception.BadRequestException;
import com.pino.translateitapi.model.dto.input.CreateTranslationKeyInput;
import com.pino.translateitapi.model.entity.ProjectEntity;
import com.pino.translateitapi.model.entity.TranslationEntity;
import com.pino.translateitapi.model.entity.TranslationKeyEntity;
import com.pino.translateitapi.util.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TranslationKeyService {

    private final TranslationKeyRepository translationKeyRepository;
    private final TranslationRepository translationRepository;
    private final ProjectRepository projectRepository;

    public void createTranslationKey(CreateTranslationKeyInput input) {
        TranslationKeyEntity translationKeyEntity = ModelMapperUtils.map(input, TranslationKeyEntity.class);
        translationKeyRepository.save(translationKeyEntity);
        ProjectEntity projectEntity = projectRepository.findByOid(translationKeyEntity.getProjectOid());
        createTranslation(translationKeyEntity.getOid(), projectEntity.getSourceLanguageCode(),
            input.getSourceTranslation());
    }

    public void createTranslation(Integer translationKeyOid, String languageCode, String content) {
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
}
