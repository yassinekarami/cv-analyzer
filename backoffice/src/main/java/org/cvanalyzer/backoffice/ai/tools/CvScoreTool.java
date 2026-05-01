package org.cvanalyzer.backoffice.ai.tools;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cvanalyzer.backoffice.ai.prompt.Prompts;
import org.cvanalyzer.backoffice.component.AIAgent.ChatAIAgent;
import org.cvanalyzer.backoffice.model.*;
import org.cvanalyzer.backoffice.repository.CvMapper;
import org.cvanalyzer.backoffice.service.CvScoreService;
import org.cvanalyzer.backoffice.service.VectorStoreService;
import org.cvanalyzer.backoffice.utils.CategoriesEnum;
import org.cvanalyzer.backoffice.utils.CvAnalyzerUtils;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.vectorstore.filter.Filter;
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
     * Cv repository
     */
    private final CvMapper cvRepository;

    /**
     * Cv score service
     */
    private final CvScoreService cvScoreService;

    /**
     * constructor
     * @param pgVectorStore pgVectorStore
     * @param cvRepository cvRepository
     */
    public CvScoreTool(@Qualifier("PGVectorStoreServiceImpl") VectorStoreService pgVectorStore,
                       CvMapper cvRepository,
                       CvScoreService cvScoreService) {
        this.pgVectorStore = pgVectorStore;
        this.cvRepository = cvRepository;
        this.cvScoreService = cvScoreService;
    }

    @Tool(
            name = "scoreComputeTool",
            description = "Rank CV according to the skills in the query"
    )
    public List<CvScoreResponseDto> scoreCompute(String query) throws JsonProcessingException {

        List<Document> results = pgVectorStore.findDocumentBySimilarityAndMetadata(query,
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

    private List<CvScoreResponseDto> retrieveCvDetailsFromFilename(List<String> filesname) throws JsonProcessingException {
        List<EmbeddedCvDto> embeddedCvDtos = cvRepository.findByFilename(filesname);
        Map<String, List<EmbeddedCvDto>> fileNameEmbeddingMap = new HashMap<>();
        List<CvScoreResponseDto> response = new ArrayList<>();

        for (EmbeddedCvDto embeddedCvDto : embeddedCvDtos) {
            fileNameEmbeddingMap
                    .computeIfAbsent(embeddedCvDto.getFilename(), k -> new ArrayList<>())
                    .add(embeddedCvDto);
        }

        for (Map.Entry<String, List<EmbeddedCvDto>> entry : fileNameEmbeddingMap.entrySet()) {
            response.add(cvScoreService.calculateCvScore(entry.getValue()));

        }
        return response;
    }
}
