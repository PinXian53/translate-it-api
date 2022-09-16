package com.pino.translateitapi.model.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class Project {
    /**
     * Oid
     */
    private Integer oid;
    /**
     * 專案名稱
     */
    private String name;
    /**
     * 內容描述
     */
    private String description;
    /**
     * 進度(%)
     */
    private long progressRate;
    /**
     * 建立日期
     */
    private OffsetDateTime createDateTime;
}
