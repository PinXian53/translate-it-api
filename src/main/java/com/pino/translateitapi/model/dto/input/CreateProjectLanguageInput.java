package com.pino.translateitapi.model.dto.input;

import lombok.Data;

@Data
public class CreateProjectLanguageInput {
    /**
     * 專案Oid
     */
    private Integer projectOid;
    /**
     * 語系代碼
     */
    private String languageCode;
}
