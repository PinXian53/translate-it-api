package com.pino.translateitapi.model.dto.input;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CreateTranslationKeyInput {
    /**
     * Key
     */
    @NotEmpty(message = "key必填")
    @Size(max = 200, message = "key超過文字上限{max}")
    private String key;
    /**
     * 主語系翻譯內容
     */
    @NotEmpty(message = "內容必填")
    @Size(max = 200, message = "內容文字上限{max}")
    private String sourceTranslation;
}
