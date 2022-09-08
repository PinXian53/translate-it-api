package com.pino.translateitapi.dao;

import com.pino.translateitapi.model.entity.ProjectLanguageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectLanguageRepository extends JpaRepository<ProjectLanguageEntity, Integer> {
    List<ProjectLanguageEntity> findByProjectOid(Integer projectOid);

    Page<ProjectLanguageEntity> findByProjectOid(Integer projectOid, Pageable pageable);

    ProjectLanguageEntity findByOid(Integer oid);
}
