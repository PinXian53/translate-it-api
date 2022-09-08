package com.pino.translateitapi.model.dto.input;

import lombok.Data;

@Data
public class UpdateTranslationInput {
    /**
     * Key Oid
     */
    private Integer translationKeyOid;
    /**
     * 語系
     */
    private String languageCode;
    /**
     * 翻譯內容
     */
    private String content;
}
