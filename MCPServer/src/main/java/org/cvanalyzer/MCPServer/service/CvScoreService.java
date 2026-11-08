package org.cvanalyzer.MCPServer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.cvanalyzer.MCPServer.record.CvScoreResponseRecord;
import org.cvanalyzer.MCPServer.record.EmbeddedCvRecord;

import java.util.List;

public interface CvScoreService {

    CvScoreResponseRecord calculateCvScore(List<EmbeddedCvRecord> embeddedCvDtos) throws JsonProcessingException;

    List<CvScoreResponseRecord> computeScore(String query) throws JsonProcessingException;
}
