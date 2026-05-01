package org.cvanalyzer.backoffice.utils;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum CategoriesEnum {

    PROFILE("profile"),

    EXPERIENCE("experience"),

    SKILLS("skills"),

    PUBLICATIONS("publications"),

    TALKS("talks"),

    CERTIFICATIONS("certifications"),

    OTHER("other")
    ;

    /**
     * the value
     */
    public String value;

    /**
     * constructor
     * @param value the value
     */
    CategoriesEnum(String value) {
        this.value = value;
    }

    /**
     * Retrieve the categorie value in the enum
     * @param categorie the value to retrieve
     * @return the string vale
     */
    public CategoriesEnum fromValue(String categorie) {
        return Arrays.stream(CategoriesEnum.values())
                .filter(c -> c.getValue().equalsIgnoreCase(categorie))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Value '%s' not found in CategoriesEnum", categorie)
                ));
    }

    /**
     * return all values of the enum
     * @return the values of the enums
     */
    public static List<String> getAllValues() {
        return Arrays.stream(CategoriesEnum.values()).map(CategoriesEnum::getValue).toList();
    }
}
