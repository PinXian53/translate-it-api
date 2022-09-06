package com.pino.translateitapi.controller;

import com.pino.translateitapi.model.dto.Corpus;
import com.pino.translateitapi.model.dto.ProjectLanguage;
import com.pino.translateitapi.service.CorpusService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class CorpusController {

    private final CorpusService corpusService;

    @QueryMapping("corpus")
    public List<Corpus> findCorpus(Integer projectLanguageOid) {
        return corpusService.findCorpus(projectLanguageOid);
    }

    @SchemaMapping
    public List<Corpus> corpus(ProjectLanguage projectLanguage) {
        return corpusService.findCorpus(projectLanguage.getOid());
    }
}