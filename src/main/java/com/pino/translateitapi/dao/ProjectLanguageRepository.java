package com.pino.translateitapi.dao;

import com.pino.translateitapi.model.entity.ProjectLanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectLanguageRepository extends JpaRepository<ProjectLanguageEntity, Integer> {
    List<ProjectLanguageEntity> findByProjectOid(Integer projectOid);

    ProjectLanguageEntity findByOid(Integer oid);
}
