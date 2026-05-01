package org.cvanalyzer.backoffice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class CvScoreResponseDto {

    private String filename;

    private String overallScore;

    /**
     * constructor used to calculate the overallScore
     * @param filename the cv filename
     * @param sections list of section used to calculate the cv overallScore
     */
    public CvScoreResponseDto(String filename, List<CvSectionScoreDto> sections) {
        this.filename = filename;
        overallScore = String.valueOf(sections.stream()
                .map(section -> Integer.valueOf(section.getScore()))
                .reduce(0, Integer::sum));
    }
}
