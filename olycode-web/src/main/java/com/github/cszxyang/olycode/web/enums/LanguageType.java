package com.github.cszxyang.olycode.web.enums;

import lombok.Getter;

public enum LanguageType {

    JAVA("java"),
    LUA("lua"),
    PYTHON("python"),
    ;

    LanguageType(String lang) {
        this.lang = lang;
    }

    public static LanguageType langStrToEnum(String lang) {
        for (LanguageType type : LanguageType.values()) {
            if (type.getLang().equals(lang)) {
                return type;
            }
        }
        throw new IllegalArgumentException("illegal lang");
    }

    @Getter
    private String lang;
}
