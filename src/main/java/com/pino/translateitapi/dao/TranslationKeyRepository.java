package com.pino.translateitapi.dao;

import com.pino.translateitapi.model.entity.TranslationKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TranslationKeyRepository extends JpaRepository<TranslationKeyEntity, Integer> {

    List<TranslationKeyEntity> findByProjectOid(int projectOid);

    void deleteByProjectOid(int projectOid);
}
