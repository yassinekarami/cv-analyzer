package org.cvanalyzer.backoffice.utils;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum LangagesEnum {

    ENGLISH("english"),

    FRENCH("french")
    ;

    public String value;

    LangagesEnum(String value) {
        this.value = value;
    }

    /**
     * return all values of the enum
     * @return the values of the enums
     */
    public static List<String> getAllValues() {
        return Arrays.stream(LangagesEnum.values()).map(LangagesEnum::getValue).toList();
    }
}
