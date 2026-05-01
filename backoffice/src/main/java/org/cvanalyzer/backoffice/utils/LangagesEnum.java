package org.cvanalyzer.backoffice.utils;

import lombok.Getter;

import java.util.List;

@Getter
public enum LangagesEnum {

    ENGLISH("english"),

    FRENCH("french")
    ;

    public String value;

    LangagesEnum(String value) {
    }

    public static List<String> getAllValues() {
        return List.of(ENGLISH.getValue(), FRENCH.getValue());
    }
}
