package com.pino.translateitapi.model.dto;

import lombok.Data;

@Data
public class ProjectLanguage {
    /**
     * Oid
     */
    private Integer oid;
    /**
     * 是否為主要語言
     */
    private Boolean isSource;
    /**
     * 語系代碼
     */
    private String languageCode;
    /**
     * 進度(%)
     */
    private Integer progressRate;
}