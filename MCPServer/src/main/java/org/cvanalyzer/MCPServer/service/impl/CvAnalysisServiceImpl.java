package org.cvanalyzer.MCPServer.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.cvanalyzer.MCPServer.record.CvRecord;
import org.cvanalyzer.MCPServer.record.CvScoreResponseRecord;
import org.cvanalyzer.MCPServer.service.AnalyseService;
import org.cvanalyzer.MCPServer.utils.CvAnalyzerUtils;
import org.cvanalyzer.MCPServer.utils.Prompts;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class CvAnalysisServiceImpl implements AnalyseService {

    /**
     * pgVectoreStore
     */
    private final PgVectorStore pgVectorStore;
    /**
     * object mapper
     */
    private final ObjectMapper objectMapper;
    /**
     * chat client used for calling the llm
     */
    private final ChatClient chatClient;

    /**
     * constructor
     * @param pgVectorStore pgvectoreStore
     * @param ojectMapper objectMapper
     */
    public CvAnalysisServiceImpl(@Qualifier("vector_store") PgVectorStore pgVectorStore,
                                 ObjectMapper ojectMapper, ChatClient chatClient) {
        this.pgVectorStore = pgVectorStore;
        this.objectMapper = ojectMapper;
        this.chatClient = chatClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CvRecord analyzeAndIndex(String text, String filename) throws JsonProcessingException {


        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("CV text cannot be empty");
        }

        CvRecord cv = chatClient
                .prompt(Prompts.CONVERT_CV_PDF_INTO_JSON)
                .call()
                .entity(CvRecord.class);

        if (cv == null) {
            throw new IllegalStateException("CV analysis returned an empty result");
        }

        pgVectorStore.add(convertCvDtoToDocument(cv, filename));

        log.info("CV successfully analyzed and indexed: {}", filename);

        return cv;
    }

    private List<Document> convertCvDtoToDocument(CvRecord cvDto, String filename) throws JsonProcessingException {
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

    @Override
    public String findSimilarCv(String query) {
        List<Document> results = this.findCvBySimilarity(query, 1);

        Set<Map<String, Object>> metadatas = CvAnalyzerUtils.extractMetaData(results);
        List<String> filesName = CvAnalyzerUtils.extractFilesNameFromMetadatas(metadatas);
        return filesName.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Document> findCvBySimilarity(String query, int topK) {
        return this.pgVectorStore
                .similaritySearch(SearchRequest.builder().query(query)
                        .topK(topK).build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Document> findCvBySimilarityAndMetadata(String query, Filter.Expression expression, int topK) {
        return this.pgVectorStore
                .similaritySearch(SearchRequest.builder().query(query)
                        .similarityThreshold(0.5)
                        .filterExpression(expression)
                        .topK(topK).build());
    }
}
