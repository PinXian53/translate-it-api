package com.pino.translateitapi.model.dto.input;

import lombok.Data;

@Data
public class CreateProjectInput {
    /**
     * 專案名稱
     */
    private String name;
    /**
     * 內容描述
     */
    private String description;
    /**
     * 主要語系
     */
    private String sourceLanguageCode;
}
