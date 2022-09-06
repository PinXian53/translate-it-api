package com.pino.translateitapi.dao;

import com.pino.translateitapi.model.entiry.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<LanguageEntity, Integer> {
}
