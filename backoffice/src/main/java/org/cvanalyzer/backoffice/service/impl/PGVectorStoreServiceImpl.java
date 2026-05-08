package org.cvanalyzer.backoffice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cvanalyzer.backoffice.ai.prompt.Prompts;
import org.cvanalyzer.backoffice.component.AIAgent.ChatAIAgent;
import org.cvanalyzer.backoffice.model.CvDto;
import org.cvanalyzer.backoffice.service.VectorStoreService;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PGVectorStoreServiceImpl implements VectorStoreService {

    /**
     * pgVectoreStore
     */
    private final PgVectorStore pgVectorStore;

    /**
     * chat client
     */
    private final ChatAIAgent chatAIAgent;

    /**
     * object mapper
     */
    private final ObjectMapper objectMapper;

    /**
     * constructor
     * @param pgVectorStore pgvectoreStore
     * @param chatAIAgent aiAgent used of chat
     * @param ojectMapper objectMapper
     */
    public PGVectorStoreServiceImpl(@Qualifier("vector_store") PgVectorStore pgVectorStore,
                                    ChatAIAgent chatAIAgent,
                                    ObjectMapper ojectMapper) {
        this.pgVectorStore = pgVectorStore;
        this.chatAIAgent = chatAIAgent;
        this.objectMapper = ojectMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertIntoVectorStore(String text, String filename) throws JsonProcessingException {

        CvDto  cv = chatAIAgent.askAgent(Prompts.CONVERT_CV_DATA_INTO_JSON, text, new TypeReference<CvDto>() {});

       pgVectorStore.add(convertCvDtoToDocument(cv, filename));
    }

    private List<Document> convertCvDtoToDocument(CvDto cvDto, String filename) throws JsonProcessingException {
        List<Document> docs = new ArrayList<>();
        docs.add(new Document(objectMapper.writeValueAsString(cvDto.profile()), Map.of("filename", filename, "categorie", "profile")));
        docs.add(new Document(objectMapper.writeValueAsString(cvDto.experience()), Map.of("filename", filename, "categorie", "experience")));
        docs.add(new Document(objectMapper.writeValueAsString(cvDto.skills()), Map.of("filename", filename, "categorie", "skills")));
        docs.add(new Document(objectMapper.writeValueAsString(cvDto.publications()), Map.of("filename", filename, "categorie", "publications")));
        docs.add(new Document(objectMapper.writeValueAsString(cvDto.talks()), Map.of("filename", filename, "categorie", "talks")));
        docs.add(new Document(objectMapper.writeValueAsString(cvDto.certifications()), Map.of("filename", filename, "categorie", "certifications")));
        docs.add(new Document(objectMapper.writeValueAsString(cvDto.other()), Map.of("filename", filename, "categorie", "other")));
        return docs;

    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Document> findDocumentBySimilarity(String query, int topK) {
        return this.pgVectorStore
                .similaritySearch(SearchRequest.builder().query(query)
                        .topK(topK).build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Document> findDocumentBySimilarityAndMetadata(String query, Filter.Expression expression, int topK) {
        return this.pgVectorStore
                .similaritySearch(SearchRequest.builder().query(query)
                        .similarityThreshold(0.5)
                        .filterExpression(expression)
                        .topK(topK).build());
    }
}
