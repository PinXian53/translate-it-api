package com.pino.translateitapi.dao;

import com.pino.translateitapi.model.dto.Translation;
import com.pino.translateitapi.model.entity.TranslationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TranslationRepository extends JpaRepository<TranslationEntity, Integer> {

    @Query("select new com.pino.translateitapi.model.dto.Translation( " +
        " translation.oid, translationKey.key, sourceTranslation.content, translation.content) " +
        " from TranslationKeyEntity translationKey " +
        " left join TranslationEntity translation " +
        " on translationKey.oid = translation.translationKeyOid and translation.languageCode = :languageCode " +
        " left join TranslationEntity sourceTranslation " +
        " on translationKey.oid = sourceTranslation.translationKeyOid and sourceTranslation.languageCode = :sourceLanguageCode " +
        " where translationKey.projectOid = :projectOid ")
    List<Translation> findByLanguageCode(Integer projectOid, String languageCode, String sourceLanguageCode);
}