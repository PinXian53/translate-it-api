package com.pino.translateitapi.dao;

import com.pino.translateitapi.model.entiry.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {
}
