package com.pino.translateitapi.dao;

import com.pino.translateitapi.model.entiry.ProjectLanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectLanguageRepository extends JpaRepository<ProjectLanguageEntity, Integer> {
}
