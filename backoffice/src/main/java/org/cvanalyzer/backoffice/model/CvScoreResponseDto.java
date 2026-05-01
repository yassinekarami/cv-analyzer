package org.cvanalyzer.backoffice.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CvScoreResponseDto {

    private String filename;

    private String overallScore;
}
