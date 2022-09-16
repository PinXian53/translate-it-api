package com.pino.translateitapi.service;

import com.pino.translateitapi.dao.ProjectLanguageRepository;
import com.pino.translateitapi.dao.ProjectRepository;
import com.pino.translateitapi.dao.TranslationKeyRepository;
import com.pino.translateitapi.dao.TranslationRepository;
import com.pino.translateitapi.manager.ProjectManager;
import com.pino.translateitapi.model.dto.Pagination;
import com.pino.translateitapi.model.dto.Project;
import com.pino.translateitapi.model.dto.input.CreateProjectInput;
import com.pino.translateitapi.model.dto.input.UpdateProjectInput;
import com.pino.translateitapi.model.entity.ProjectEntity;
import com.pino.translateitapi.model.entity.ProjectLanguageEntity;
import com.pino.translateitapi.util.ModelMapperUtils;
import com.pino.translateitapi.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final LanguageService languageService;
    private final TranslationService translationService;

    private final ProjectManager projectManager;

    private final ProjectRepository projectRepository;
    private final ProjectLanguageRepository projectLanguageRepository;
    private final TranslationKeyRepository translationKeyRepository;
    private final TranslationRepository translationRepository;

    public List<Project> findProject() {
        return projectRepository.findAll().stream()
            .map(this::toProject)
            .toList();
    }

    public Pagination<Project> findProjectPage(Pageable pageable) {
        return PageUtils.toPagination(
            projectRepository.findAll(pageable),
            pageable,
            this::toProject
        );
    }

    private Project toProject(ProjectEntity projectEntity) {
        Project project = ModelMapperUtils.map(projectEntity, Project.class);
        project.setProgressRate(calculateFinishProgressRate(project.getOid()));
        return project;
    }

    @Transactional
    public void createProject(CreateProjectInput createProjectInput) {
        validCreateProjectInput(createProjectInput);
        ProjectEntity projectEntity = createProjectToDb(createProjectInput);
        createProjectLanguageToDb(projectEntity.getOid(), createProjectInput.getSourceLanguageCode());
    }

    @Transactional
    public void updateProject(int projectOid, UpdateProjectInput updateProjectInput) {
        projectManager.validProjectOid(projectOid);
        updateProjectToDb(projectOid, updateProjectInput);
    }

    @Transactional
    public void deleteProject(int projectOid) {
        projectManager.validProjectOid(projectOid);
        deleteProjectToDb(projectOid);
    }

    @Transactional
    public void deleteProjectToDb(int projectOid) {
        translationService.deleteByProjectOid(projectOid);
        translationKeyRepository.deleteByProjectOid(projectOid);
        projectLanguageRepository.deleteByProjectOid(projectOid);
        projectRepository.deleteByOid(projectOid);
    }

    private void validCreateProjectInput(CreateProjectInput createProjectInput) {
        languageService.validLanguageCode(createProjectInput.getSourceLanguageCode());
    }

    private ProjectEntity createProjectToDb(CreateProjectInput createProjectInput) {
        ProjectEntity projectEntity = ModelMapperUtils.map(createProjectInput, ProjectEntity.class);
        return projectRepository.save(projectEntity);
    }

    private void createProjectLanguageToDb(int projectOid, String languageCode) {
        ProjectLanguageEntity projectLanguageEntity = new ProjectLanguageEntity();
        projectLanguageEntity.setProjectOid(projectOid);
        projectLanguageEntity.setLanguageCode(languageCode);
        projectLanguageRepository.save(projectLanguageEntity);
    }

    private void updateProjectToDb(int projectOid, UpdateProjectInput createProjectInput) {
        ProjectEntity projectEntity = projectRepository.findByOid(projectOid);
        projectEntity.setName(createProjectInput.getName());
        projectEntity.setDescription(createProjectInput.getDescription());
        projectRepository.save(projectEntity);
    }

    private long calculateFinishProgressRate(int projectOid) {
        long totalKeyCount = translationKeyRepository.countByProjectOid(projectOid);
        if (totalKeyCount == 0) {
            return 0;
        }
        long totalLanguageCount = projectLanguageRepository.countByProjectOid(projectOid);
        long finishedCount = translationRepository.countFinishCount(projectOid);
        return Math.round((float) (finishedCount * 100) / (totalKeyCount * totalLanguageCount));
    }
}
