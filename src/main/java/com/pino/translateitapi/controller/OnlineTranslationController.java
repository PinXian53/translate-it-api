package com.pino.translateitapi.controller;

import com.pino.translateitapi.constant.OnlineTranslateEnum;
import com.pino.translateitapi.service.OnlineTranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class OnlineTranslationController {

    private final OnlineTranslationService onlineTranslationService;

    @QueryMapping("onlineTranslate")
    public String onlineTranslate(
        @Argument String content,
        @Argument OnlineTranslateEnum onlineTranslateEnum,
        @Argument String fromLanguageCode,
        @Argument String toLanguageCode) {
        return onlineTranslationService.translate(content, onlineTranslateEnum, fromLanguageCode, toLanguageCode);
    }
}
