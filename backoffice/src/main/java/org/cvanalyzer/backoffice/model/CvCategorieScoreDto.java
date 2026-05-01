package org.cvanalyzer.backoffice.model;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class CvCategorieScoreDto {

    private String filename;

    private String categorie;

    private String score;
}
