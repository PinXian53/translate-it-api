package com.pino.translateitapi.manager;

import com.pino.translateitapi.dao.ProjectRepository;
import com.pino.translateitapi.exception.BadRequestException;
import com.pino.translateitapi.model.entity.ProjectEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ProjectManager {

    private final ProjectRepository projectRepository;

    public void validProjectOid(int projectOid) {
        if (!projectRepository.existsByOid(projectOid)) {
            throw new BadRequestException("無法識別之專案");
        }
    }

    public ProjectEntity validProjectOidAndReturnEntity(int projectOid) {
        return Optional.ofNullable(projectRepository.findByOid(projectOid))
            .orElseThrow(() -> new BadRequestException("無法識別之專案"));
    }
}
