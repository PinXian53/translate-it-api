package com.pino.translateitapi.controller;

import com.pino.translateitapi.model.dto.input.CreateTranslationKeyInput;
import com.pino.translateitapi.service.TranslationKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class TranslationKeyController {

    private final TranslationKeyService translationKeyService;

    @MutationMapping
    public void createTranslationKey(@Argument int projectOid, @Argument @Valid CreateTranslationKeyInput input) {
        translationKeyService.createTranslationKey(projectOid, input);
    }
}
