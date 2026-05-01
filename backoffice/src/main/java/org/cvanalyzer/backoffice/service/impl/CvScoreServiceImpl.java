package org.cvanalyzer.backoffice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.cvanalyzer.backoffice.ai.prompt.Prompts;
import org.cvanalyzer.backoffice.component.AIAgent.ChatAIAgent;
import org.cvanalyzer.backoffice.model.CvCategorieScoreDto;
import org.cvanalyzer.backoffice.model.CvMetadataDto;
import org.cvanalyzer.backoffice.model.CvScoreResponseDto;
import org.cvanalyzer.backoffice.model.EmbeddedCvDto;
import org.cvanalyzer.backoffice.service.CvScoreService;
import org.cvanalyzer.backoffice.utils.CategoriesEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.cvanalyzer.backoffice.utils.CategoriesEnum.*;
import static org.cvanalyzer.backoffice.utils.CategoriesEnum.CERTIFICATIONS;
import static org.cvanalyzer.backoffice.utils.CategoriesEnum.OTHER;
import static org.cvanalyzer.backoffice.utils.CategoriesEnum.TALKS;

@Service
@RequiredArgsConstructor
public class CvScoreServiceImpl implements CvScoreService {

    /**
     * ai agent responsible of interaction with the chat client
     */
    private final ChatAIAgent aiAgent;

    /**
     * Object mapper for converting json to object and object to json
     */
    private final ObjectMapper objectMapper;


    @Override
    public CvScoreResponseDto calculateCvScore(List<EmbeddedCvDto> embeddedCvDtos) throws JsonProcessingException {
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
