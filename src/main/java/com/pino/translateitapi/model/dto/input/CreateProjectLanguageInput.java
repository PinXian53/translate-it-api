package com.pino.translateitapi.model.dto.input;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CreateProjectLanguageInput {
    /**
     * 語系代碼
     */
    @NotEmpty(message = "語系必填")
    @Size(max = 20, message = "語系超過文字上限{max}")
    private String languageCode;
}
