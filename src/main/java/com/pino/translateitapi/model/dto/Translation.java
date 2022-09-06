package com.pino.translateitapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Translation {
    /**
     * Oid
     */
    private Integer oid;
    /**
     * key
     */
    private String key;
    /**
     * 來源
     */
    private String source;
    /**
     * 翻譯內容
     */
    private String content;
}
