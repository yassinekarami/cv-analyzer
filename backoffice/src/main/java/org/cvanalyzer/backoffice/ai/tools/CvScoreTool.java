package org.cvanalyzer.backoffice.ai.tools;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.cvanalyzer.backoffice.model.*;
import org.cvanalyzer.backoffice.repository.CvMapper;
import org.cvanalyzer.backoffice.service.VectorStoreService;
import org.cvanalyzer.backoffice.utils.CvAnalyzerUtils;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static org.cvanalyzer.backoffice.utils.CategoriesEnum.*;

/**
 * a springAi tool used to give a score to a given CV
 */
@Component
public class CvScoreTool {

    /**
     * service for querying  the vector store
     */
    private final VectorStoreService pgVectorStore;

    /**
     * Object mapper for converting json to object and object to json
     */
    private final ObjectMapper objectMapper;
    /**
     * Cv repository
     */
    private final CvMapper cvRepository;

    /**
     * constructor
     * @param pgVectorStore pgVectorStore
     * @param objectMapper objectMapper
     * @param cvRepository cvRepository
     */
    public CvScoreTool(@Qualifier("PGVectorStoreServiceImpl") VectorStoreService pgVectorStore, ObjectMapper objectMapper, CvMapper cvRepository) {
        this.pgVectorStore = pgVectorStore;
        this.objectMapper = objectMapper;
        this.cvRepository = cvRepository;
    }

    @Tool(
            name = "scoreComputeTool",
            description = "Rank CV according to the skills in the query"
    )
    public String scoreCompute(String query) throws JsonProcessingException {

        List<Document> results = pgVectorStore.findDocumentBySimilarityAndMetadata(query,
                new Filter.Expression(Filter.ExpressionType.EQ,
                        new Filter.Key("categorie"),
                        new Filter.Value("skills")),
                10);

        if (CollectionUtils.isEmpty(results)) {
            return "noMatching CV";
        } else {
            Set<Map<String, Object>> metadatas = CvAnalyzerUtils.extractMetaData(results);
            List<String> filesname = CvAnalyzerUtils.extractFilesNameFromMetadatas(metadatas);
            retrieveCvDetailsFromFilename(filesname);
            return "OK";
        }

    }

    private void retrieveCvDetailsFromFilename(List<String> filesname) throws JsonProcessingException {
        List<EmbeddedCvDto> embeddedCvDtos = cvRepository.findByFilename(filesname);
        Map<String, List<EmbeddedCvDto>> fileNameEmbeddingMap = new HashMap<>();

        for (EmbeddedCvDto embeddedCvDto : embeddedCvDtos) {
            fileNameEmbeddingMap
                    .computeIfAbsent(embeddedCvDto.getFilename(), k -> new ArrayList<>())
                    .add(embeddedCvDto);
        }

        for (Map.Entry<String, List<EmbeddedCvDto>> entry : fileNameEmbeddingMap.entrySet()) {
            CvScoreResponseDto score  = calculateCvScore(entry.getValue());
            score.setFilename(entry.getKey());
        }


    }

    private CvScoreResponseDto calculateCvScore(List<EmbeddedCvDto> embeddedCvDtos) throws JsonProcessingException {
        List<CvSectionScoreDto> sectionScoreDtos = new ArrayList<>();
        for(EmbeddedCvDto embeddedCvDto: embeddedCvDtos) {

            CvMetadataDto metadata = objectMapper.readValue(embeddedCvDto.getMetadata(), CvMetadataDto.class);
//            switch (metadata.getCategorie()) {
//                case PROFILE -> {
//
//                }
//                case EXPERIENCE -> {
//
//                }
//                case SKILLS -> {
//
//                }
//                case PUBLICATIONS -> {
//
//                }
//                case TALKS -> {
//
//                }
//                case CERTIFICATIONS -> {
//
//                }
//                case OTHER -> {
//
//                }
//            }
        }

        return new CvScoreResponseDto(embeddedCvDtos.getFirst().getFilename(), sectionScoreDtos);
    }

}
