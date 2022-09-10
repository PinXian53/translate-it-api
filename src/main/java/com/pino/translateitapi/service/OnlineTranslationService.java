package com.pino.translateitapi.service;

public interface OnlineTranslationService {

    String translate(String content, String toLanguageCode);

    String translate(String content, String fromLanguageCode, String toLanguageCode);
}
