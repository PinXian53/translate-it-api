package com.pino.translateitapi.controller;

import com.pino.translateitapi.dao.LanguageRepository;
import com.pino.translateitapi.model.dto.Language;
import com.pino.translateitapi.util.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class LanguageController {
    private final LanguageRepository languageRepository;

    @QueryMapping("language")
    public List<Language> getAllLanguage() {
        return ModelMapperUtils.mapList(languageRepository.findAll(), Language.class);
    }
}
