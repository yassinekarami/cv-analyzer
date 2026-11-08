package org.cvanalyzer.MCPServer.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.cvanalyzer.MCPServer.record.CvScoreResponseRecord;
import org.cvanalyzer.MCPServer.record.EmbeddedCvRecord;
import org.cvanalyzer.MCPServer.repository.CvMapper;
import org.cvanalyzer.MCPServer.service.CvScoreService;
import org.cvanalyzer.MCPServer.utils.CvAnalyzerUtils;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class CvScoreServiceImpl implements CvScoreService {

    private final PgVectorStore pgVectorStore;

    /**
     * Cv repository
     */
    private final CvMapper cvMapper;

    public CvScoreServiceImpl(@Qualifier("vector_store") PgVectorStore pgVectorStore,
                              CvMapper cvMapper) {
        this.pgVectorStore = pgVectorStore;
        this.cvMapper = cvMapper;
    }


    @Override
    public CvScoreResponseRecord calculateCvScore(List<EmbeddedCvRecord> embeddedCvDtos) throws JsonProcessingException {
        return null;
    }

    @Override
    public List<CvScoreResponseRecord> computeScore(String query) throws JsonProcessingException {
        List<Document> results = this.findCvBySimilarityAndMetadata(query,
                new Filter.Expression(Filter.ExpressionType.EQ,
                        new Filter.Key("categorie"),
                        new Filter.Value("skills")),
                10);

        if (CollectionUtils.isEmpty(results)) {
            return new ArrayList<>();
        } else {
            Set<Map<String, Object>> metadatas = CvAnalyzerUtils.extractMetaData(results);
            List<String> filesname = CvAnalyzerUtils.extractFilesNameFromMetadatas(metadatas);
            return retrieveCvDetailsFromFilename(filesname);
        }
    }

    private List<CvScoreResponseRecord> retrieveCvDetailsFromFilename(List<String> filesname) throws JsonProcessingException {
        List<EmbeddedCvRecord> embeddedCvDtos = cvMapper.findByFilename(filesname);
        Map<String, List<EmbeddedCvRecord>> fileNameEmbeddingMap = new HashMap<>();
        List<CvScoreResponseRecord> response = new ArrayList<>();

        for (EmbeddedCvRecord embeddedCvDto : embeddedCvDtos) {
            fileNameEmbeddingMap
                    .computeIfAbsent(embeddedCvDto.filename(), k -> new ArrayList<>())
                    .add(embeddedCvDto);
        }

        for (Map.Entry<String, List<EmbeddedCvRecord>> entry : fileNameEmbeddingMap.entrySet()) {
        //    response.add(cvScoreService.calculateCvScore(entry.getValue()));

        }
        return response;
    }

    public List<Document> findCvBySimilarityAndMetadata(String query, Filter.Expression expression, int topK) {
        return this.pgVectorStore
                .similaritySearch(SearchRequest.builder().query(query)
                        .similarityThreshold(0.5)
                        .filterExpression(expression)
                        .topK(topK).build());
    }
}
