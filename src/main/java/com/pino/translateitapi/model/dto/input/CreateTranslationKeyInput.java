package com.pino.translateitapi.model.dto.input;

import lombok.Data;

@Data
public class CreateTranslationKeyInput {
    /**
     * 專案Oid
     */
    private Integer projectOid;
    /**
     * Key
     */
    private String key;
    /**
     * 主語系翻譯內容
     */
    private String sourceTranslation;
}
