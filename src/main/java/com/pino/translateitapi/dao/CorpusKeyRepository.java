package com.pino.translateitapi.dao;

import com.pino.translateitapi.model.entiry.CorpusKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorpusKeyRepository extends JpaRepository<CorpusKeyEntity, Integer> {
}
