package com.pino.translateitapi.dao;

import com.pino.translateitapi.model.entity.TranslationKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslationKeyRepository extends JpaRepository<TranslationKeyEntity, Integer> {
}
