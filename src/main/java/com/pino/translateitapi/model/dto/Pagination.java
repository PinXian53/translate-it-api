package com.pino.translateitapi.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class Pagination<T> {
    /**
     * 分頁資訊
     */
    private PageInfo pageInfo;
    /**
     * 資料
     */
    private List<T> data;
}
