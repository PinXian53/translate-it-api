package com.pino.translateitapi.service;

import com.pino.translateitapi.constant.OnlineTranslateEnum;
import com.pino.translateitapi.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OnlineTranslationService {

    private final AzureTranslationService azureTranslationService;

    public String translate(
        String content,
        OnlineTranslateEnum onlineTranslateEnum,
        String fromLanguageCode,
        String toLanguageCode) {
        OnlineTranslation onlineTranslation;
        switch (onlineTranslateEnum) {
            case AZURE -> onlineTranslation = azureTranslationService;
            default -> throw new BadRequestException("無法識別的翻譯供應商");
        }
        return onlineTranslation.translate(content, fromLanguageCode, toLanguageCode);
    }
}
