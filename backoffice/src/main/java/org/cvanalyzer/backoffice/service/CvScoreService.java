package org.cvanalyzer.backoffice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.cvanalyzer.backoffice.model.CvScoreResponseDto;
import org.cvanalyzer.backoffice.model.EmbeddedCvDto;

import java.util.List;

public interface CvScoreService {

    CvScoreResponseDto calculateCvScore(List<EmbeddedCvDto> embeddedCvDtos) throws JsonProcessingException;
}
