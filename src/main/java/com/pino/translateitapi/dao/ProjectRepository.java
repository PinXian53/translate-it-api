package com.pino.translateitapi.dao;

import com.pino.translateitapi.model.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {
    ProjectEntity findByOid(Integer projectOid);

    boolean existsByOid(int projectOid);

    @Transactional
    void deleteByOid(int projectOid);
}
