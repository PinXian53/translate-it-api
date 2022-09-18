package com.pino.translateitapi.controller;

import com.pino.translateitapi.constant.I18nTypeEnum;
import com.pino.translateitapi.service.I18nService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class I18nController {

    private final I18nService i18nService;

    @GetMapping("i18n/{projectOid}/{languageCode}/{i18nType}")
    public String i18n(
        @PathVariable int projectOid,
        @PathVariable String languageCode,
        @PathVariable I18nTypeEnum i18nType) {
        return i18nService.exportI18n(projectOid, languageCode, i18nType);
    }

    @GetMapping("i18nFile/{projectOid}/{languageCode}/{i18nType}")
    public void i18nFile(
        @PathVariable int projectOid,
        @PathVariable String languageCode,
        @PathVariable I18nTypeEnum i18nType,
        HttpServletResponse response) {
        i18nService.exportI18nFile(projectOid, languageCode, i18nType, response);
    }

}
