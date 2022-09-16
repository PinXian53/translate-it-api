package com.pino.translateitapi.model.dto.input;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UpdateProjectInput {
    /**
     * 專案名稱
     */
    @NotEmpty(message = "專案名稱必填")
    @Size(max = 50, message = "專案名稱超過文字上限{max}")
    private String name;
    /**
     * 內容描述
     */
    @Size(max = 200, message = "內容描述超過文字上限{max}")
    private String description;
}
