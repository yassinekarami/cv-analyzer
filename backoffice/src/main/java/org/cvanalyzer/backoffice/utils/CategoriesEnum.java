package org.cvanalyzer.backoffice.utils;

import lombok.Getter;

import java.util.Arrays;

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
    }

    /**
     * Retrieve the categorie value in the enum
     * @param categorie the value to retrieve
     * @return the string vale
     */
    public String fromValue(String categorie) {
        Arrays.stream(CategoriesEnum.values())
                .filter(c -> c.getValue().equalsIgnoreCase(categorie))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Value '%s' not found in CategoriesEnum", categorie)
                ));
    }
}
