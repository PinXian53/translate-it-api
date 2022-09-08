package com.pino.translateitapi.controller;

import com.pino.translateitapi.model.dto.ProjectLanguage;
import com.pino.translateitapi.model.dto.Translation;
import com.pino.translateitapi.model.dto.input.UpdateTranslationInput;
import com.pino.translateitapi.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class TranslationController {

    private final TranslationService translationService;

    @QueryMapping("translation")
    public List<Translation> findTranslation(Integer projectLanguageOid) {
        return translationService.findTranslation(projectLanguageOid);
    }

    @SchemaMapping
    public List<Translation> translation(ProjectLanguage projectLanguage) {
        return translationService.findTranslation(projectLanguage.getOid());
    }

    @MutationMapping
    public void updateTranslation(@Argument UpdateTranslationInput input) {
        translationService.creatOrUpdateTranslation(input);
    }
}