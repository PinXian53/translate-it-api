package com.pino.translateitapi.controller;

import com.pino.translateitapi.constant.I18nTypeEnum;
import com.pino.translateitapi.service.I18nService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class I18nController {

    private final I18nService i18nService;

    @QueryMapping("i18n")
    public String i18n(
        @Argument int projectOid,
        @Argument String languageCode,
        @Argument I18nTypeEnum i18nType,
        @Argument Boolean pretty) {
        return i18nService.exportI18n(projectOid, languageCode, i18nType, pretty);
    }

}
