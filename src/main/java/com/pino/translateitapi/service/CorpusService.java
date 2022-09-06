package com.pino.translateitapi.service;

import com.pino.translateitapi.dao.CorpusRepository;
import com.pino.translateitapi.dao.ProjectLanguageRepository;
import com.pino.translateitapi.model.dto.Corpus;
import com.pino.translateitapi.model.entiry.ProjectLanguageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CorpusService {

    private final ProjectLanguageRepository projectLanguageRepository;
    private final CorpusRepository corpusRepository;

    @Transactional(readOnly = true)
    public List<Corpus> findCorpus(Integer projectLanguageOid) {
        final ProjectLanguageEntity projectLanguageEntity = projectLanguageRepository.findByOid(projectLanguageOid);
        final Integer projectOid = projectLanguageEntity.getProjectOid();
        final ProjectLanguageEntity sourceProjectLanguageEntity = projectLanguageRepository.findByProjectOidAndIsSource(
            projectOid, true);
        return corpusRepository.findByLanguageCode(projectOid, projectLanguageEntity.getLanguageCode(),
            sourceProjectLanguageEntity.getLanguageCode());
    }
}
