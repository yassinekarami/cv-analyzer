package org.cvanalyzer.backoffice.utils;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

//PROFILE         = 0.10,
//EXPERIENCE      = 0.35,
//SKILLS          = 0.25,
//PUBLICATIONS    = 0.08,
//TALKS           = 0.07,
//CERTIFICATIONS  = 0.10,
//OTHER           = 0.05

@Getter
public enum CategoriesEnum {

    PROFILE("profile", "0.10"),

    EXPERIENCE("experience", "0.35"),

    SKILLS("skills", "0.25"),

    PUBLICATIONS("publications", "0.08"),

    TALKS("talks", "0.07"),

    CERTIFICATIONS("certifications", "0.10"),

    OTHER("other", "0.05")
    ;

    /**
     * the value
     */
    public final String value;

    /**
     * the coefficient
     */
    public final String coefficient;

    /**
     * constructor
     * @param value the value
     */
    CategoriesEnum(String value, String coefficient) {
        this.value = value;
        this.coefficient = coefficient;
    }

    /**
     * Retrieve the categorie value in the enum
     * @param categorie the value to retrieve
     * @return the string vale
     */
    public static CategoriesEnum fromValue(String categorie) {
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
