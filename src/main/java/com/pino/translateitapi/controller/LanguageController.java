package com.pino.translateitapi.controller;

import com.pino.translateitapi.model.dto.Language;
import com.pino.translateitapi.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class LanguageController {
    private final LanguageService languageService;

    @QueryMapping("language")
    public List<Language> getAllLanguage() {
        return languageService.getAllLanguage();
    }
}
