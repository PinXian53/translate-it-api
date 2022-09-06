package com.pino.translateitapi.dao;

import com.pino.translateitapi.model.entiry.CorpusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorpusRepository extends JpaRepository<CorpusEntity, Integer> {
}
