package com.pino.translateitapi.model.dto.input;

import lombok.Data;

@Data
public class CreateProjectLanguageInput {
    /**
     * 語系代碼
     */
    private String languageCode;
}
