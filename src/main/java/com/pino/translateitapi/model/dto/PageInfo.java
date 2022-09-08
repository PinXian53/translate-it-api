package com.pino.translateitapi.model.dto;

import lombok.Data;

@Data
public class PageInfo {
    /**
     * 當前頁數，從 1 開始計數
     */
    private int pageNum;
    /**
     * 每頁資料筆數，預設為每頁 10 筆
     */
    private int pageSize;
    /**
     * 當前頁數所含資料筆數
     */
    private int size;
    /**
     * 此次查詢資料總筆數
     */
    private long total;
    /**
     * 總頁數
     */
    private int pages;
}
