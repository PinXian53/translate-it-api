package com.pino.translateitapi.model.dto.input;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UpdateTranslationInput {
    /**
     * 翻譯內容
     */
    @Size(max = 200, message = "內容超過文字上限{max}")
    private String content;
}
