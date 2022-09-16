package com.pino.translateitapi.controller;

import com.pino.translateitapi.model.dto.Pagination;
import com.pino.translateitapi.model.dto.ProjectLanguage;
import com.pino.translateitapi.model.dto.Translation;
import com.pino.translateitapi.model.dto.input.UpdateTranslationInput;
import com.pino.translateitapi.service.TranslationService;
import com.pino.translateitapi.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class TranslationController {

    private final TranslationService translationService;

    @QueryMapping("translation")
    public List<Translation> findTranslation(@Argument Integer projectLanguageOid) {
        return translationService.findTranslation(projectLanguageOid);
    }

    @QueryMapping("translationPage")
    public Pagination<Translation> findTranslationPage(
        @Argument Integer projectLanguageOid,
        @Argument Integer pageNum,
        @Argument Integer pageSize) {
        return translationService.findTranslationPage(projectLanguageOid, PageUtils.toPageable(pageNum, pageSize));
    }

    @SchemaMapping
    public List<Translation> translation(ProjectLanguage projectLanguage) {
        return translationService.findTranslation(projectLanguage.getOid());
    }

    @MutationMapping
    public void updateTranslation(
        @Argument int translationKeyOid,
        @Argument String languageCode,
        @Argument @Valid UpdateTranslationInput input) {
        translationService.creatOrUpdateTranslation(translationKeyOid, languageCode, input);
    }
}