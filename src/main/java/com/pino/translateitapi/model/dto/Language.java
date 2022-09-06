package com.pino.translateitapi.model.dto;

import lombok.Data;

@Data
public class Language {
    /**
     * 語系代碼
     */
    private String code;
    /**
     * 描述
     */
    private String description;
}
