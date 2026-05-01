package org.cvanalyzer.backoffice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class CvSectionScoreDto {

    private String filename;

    private String section;

    private String score;
}
