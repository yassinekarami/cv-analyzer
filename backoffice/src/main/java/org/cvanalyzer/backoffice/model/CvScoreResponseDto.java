package org.cvanalyzer.backoffice.model;

import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
public class CvScoreResponseDto {

    private String filename;

    private String overallScore;
}
