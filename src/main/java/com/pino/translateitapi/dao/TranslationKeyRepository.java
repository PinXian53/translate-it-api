package com.pino.translateitapi.dao;

import com.pino.translateitapi.model.dto.KeyValue;
import com.pino.translateitapi.model.entity.TranslationKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TranslationKeyRepository extends JpaRepository<TranslationKeyEntity, Integer> {

    List<TranslationKeyEntity> findByProjectOid(int projectOid);

    void deleteByProjectOid(int projectOid);

    TranslationKeyEntity findByOid(Integer translationKeyOid);

    boolean existsByProjectOidAndKey(int projectOid, String key);

    long countByProjectOid(int projectOid);

    @Query("select new com.pino.translateitapi.model.dto.KeyValue(keyEntity.key, translationEntity.content) " +
        " from TranslationKeyEntity keyEntity " +
        " left join TranslationEntity translationEntity " +
        " on keyEntity.oid = translationEntity.translationKeyOid " +
        " and translationEntity.languageCode = :languageCode " +
        " where keyEntity.projectOid = :projectOid " +
        " order by keyEntity.key")
    List<KeyValue> getI18nKeyValue(int projectOid, String languageCode);
}
