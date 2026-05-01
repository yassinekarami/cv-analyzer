package org.cvanalyzer.backoffice.ai.tools;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cvanalyzer.backoffice.ai.prompt.Prompts;
import org.cvanalyzer.backoffice.component.AIAgent.ChatAIAgent;
import org.cvanalyzer.backoffice.model.*;
import org.cvanalyzer.backoffice.repository.CvMapper;
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
     * Object mapper for converting json to object and object to json
     */
    private final ObjectMapper objectMapper;
    /**
     * Cv repository
     */
    private final CvMapper cvRepository;

    /**
     * ai agent responsible of interaction with the chat client
     */
    private final ChatAIAgent aiAgent;

    /**
     * constructor
     * @param pgVectorStore pgVectorStore
     * @param objectMapper objectMapper
     * @param cvRepository cvRepository
     */
    public CvScoreTool(@Qualifier("PGVectorStoreServiceImpl") VectorStoreService pgVectorStore,
                       ObjectMapper objectMapper,
                       CvMapper cvRepository,
                       ChatAIAgent aiAgent) {
        this.pgVectorStore = pgVectorStore;
        this.objectMapper = objectMapper;
        this.cvRepository = cvRepository;
        this.aiAgent = aiAgent;
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
            response.add(calculateCvScore(entry.getValue()));

        }
        return response;
    }

    private CvScoreResponseDto calculateCvScore(List<EmbeddedCvDto> embeddedCvDtos) throws JsonProcessingException {
        List<CvCategorieScoreDto> categorieScoreDtos = new ArrayList<>();
        for(EmbeddedCvDto embeddedCvDto: embeddedCvDtos) {

            CvMetadataDto metadata = objectMapper.readValue(embeddedCvDto.getMetadata(), CvMetadataDto.class);
            switch (CategoriesEnum.fromValue(metadata.getCategorie())) {
                case EXPERIENCE -> categorieScoreDtos.add(computeScoreForCategorie(metadata.getFilename(), EXPERIENCE, embeddedCvDto.getContent()));
                case SKILLS -> categorieScoreDtos.add(computeScoreForCategorie(metadata.getFilename(), SKILLS, embeddedCvDto.getContent()));
                case PUBLICATIONS -> categorieScoreDtos.add(computeScoreForCategorie(metadata.getFilename(), PUBLICATIONS, embeddedCvDto.getContent())) ;
                case TALKS -> categorieScoreDtos.add(computeScoreForCategorie(metadata.getFilename(), TALKS, embeddedCvDto.getContent()));
                case CERTIFICATIONS -> categorieScoreDtos.add(computeScoreForCategorie(metadata.getFilename(), CERTIFICATIONS, embeddedCvDto.getContent()));
                case OTHER -> categorieScoreDtos.add(computeScoreForCategorie(metadata.getFilename(), OTHER, embeddedCvDto.getContent()));
            }
        }

        return buildScoreReponse(embeddedCvDtos.getFirst().getFilename(), categorieScoreDtos);
    }


    /**
     * Computes a {@link CvCategorieScoreDto} for a given category based on the provided data.
     * <p>
     * If the input data is an empty JSON array (i.e. "[]"), the method returns a DTO
     * with a score of "0" for the given filename and category.
     * </p>
     *
     * @param filename   the name of the file associated with the score (must not be null)
     * @param categorie  the category for which the score is computed (must not be null)
     * @param data       the raw data used to compute the score (expected as a JSON string)
     * @return a {@link CvCategorieScoreDto} containing the filename, category, and computed score
     */
    private CvCategorieScoreDto computeScoreForCategorie(String filename, CategoriesEnum categorie, String data) {
        String score = "0";
        if (!"[]".equals(data))
            score = aiAgent.askAgent(Prompts.COMPUTE_SCORE_FOR_CATEGORIE, data, new TypeReference<String>() {});

        return buildCategorie(filename, score, categorie);
    }


    /**
     * build a CvCategorieScore
     * @param filename the filename which belong the categorie
     * @param score the computed categorie score
     * @param categorie the categorie being evaluated
     * @return a newly created CvCategorieScore
     */
    private CvCategorieScoreDto buildCategorie(String filename, String score, CategoriesEnum categorie) {
        Float finalScore = Float.parseFloat(score) * Float.parseFloat(categorie.getCoefficient());
        return CvCategorieScoreDto.builder()
                .filename(filename)
                .score(String.valueOf(finalScore))
                .categorie(categorie.getValue()).build();
    }

    /**
     * Build a CvScoreResponse
     * @param filename the cv filename
     * @param sections list of section used to calculate the cv overallScore
     */
    private CvScoreResponseDto buildScoreReponse(String filename, List<CvCategorieScoreDto> sections) {
        String overallScore = String.valueOf(sections.stream()
                .map(section -> Float.valueOf(section.getScore()))
                .reduce(0.0F, Float::sum));

        return CvScoreResponseDto.builder()
                .filename(filename)
                .overallScore(overallScore).build();
    }
}
