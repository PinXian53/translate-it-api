package com.pino.translateitapi.constant;

public enum I18nTypeEnum {
    JSON(".json"),
    YAML(".yml"),
    PROPERTIES(".properties");

    private final String extension;

    I18nTypeEnum(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return this.extension;
    }
}
