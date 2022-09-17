package com.pino.translateitapi.service;

import com.pino.translateitapi.constant.I18nTypeEnum;
import com.pino.translateitapi.dao.TranslationKeyRepository;
import com.pino.translateitapi.exception.BadRequestException;
import com.pino.translateitapi.manager.ProjectManager;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class I18nService {

    private static final int DEFAULT_INDENT_FACTOR = 0;
    private static final int PRETTY_INDENT_FACTOR = 4;

    private final ProjectManager projectManager;
    private final TranslationService translationService;

    private final TranslationKeyRepository translationKeyRepository;

    public String exportI18n(int projectOid, String languageCode, I18nTypeEnum i18nType, Boolean pretty) {
        projectManager.validProjectOid(projectOid);
        translationService.validProjectNeedExistLanguageCode(projectOid, languageCode);
        switch (i18nType) {
            case JSON -> {
                return exportJson(projectOid, languageCode, Boolean.TRUE.equals(pretty));
            }
            default -> throw new BadRequestException("尚未支援");
        }
    }

    private String exportJson(int projectOid, String languageCode, boolean pretty) {
        JSONObject jsonObject = new JSONObject();
        translationKeyRepository.getI18nKeyValue(projectOid, languageCode).forEach(keyValue -> {
            jsonObject.put(keyValue.getKey(), keyValue.getValue());
        });
        return convertToJsonString(jsonObject, pretty);
    }

    private String convertToJsonString(JSONObject jsonObject, boolean pretty) {
        return jsonObject.toString(pretty ? PRETTY_INDENT_FACTOR : DEFAULT_INDENT_FACTOR);
    }
}
