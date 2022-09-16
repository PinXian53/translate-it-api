package com.pino.translateitapi.dao;

import com.pino.translateitapi.model.entity.ProjectLanguageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProjectLanguageRepository extends JpaRepository<ProjectLanguageEntity, Integer> {
    List<ProjectLanguageEntity> findByProjectOid(Integer projectOid);

    Page<ProjectLanguageEntity> findByProjectOid(Integer projectOid, Pageable pageable);

    ProjectLanguageEntity findByOid(Integer oid);

    @Transactional
    void deleteByProjectOid(int projectOid);

    boolean existsByProjectOidAndLanguageCode(int projectOid, String languageCode);

    long countByProjectOid(int projectOid);
}
