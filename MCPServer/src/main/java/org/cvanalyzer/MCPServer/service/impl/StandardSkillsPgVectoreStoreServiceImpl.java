package org.cvanalyzer.MCPServer.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.cvanalyzer.MCPServer.record.CvRecord;
import org.cvanalyzer.MCPServer.service.AnalyseService;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * service for manipulating the standard skills vector store
 */
@Service
public class StandardSkillsPgVectoreStoreServiceImpl implements AnalyseService {

    /**
     * pg vector store
     */
    private final PgVectorStore pgVectorStore;


    /**
     * Constructor
     * @param vectorStore
     */
    public StandardSkillsPgVectoreStoreServiceImpl(@Qualifier("standardSkillsVectorStore")PgVectorStore vectorStore) {
        this.pgVectorStore = vectorStore;
    }


    public void insertIntoVectorStore(String text, String name) {

        Document doc = new Document("text", Map.of("skill", name));
        pgVectorStore.add(List.of(doc));
    }

    @Override
    public CvRecord analyzeAndIndex(String text, String name) throws JsonProcessingException {
        return null;
    }

    @Override
    public List<Document> findCvBySimilarity(String query, int topK) {
        return List.of();
    }

    @Override
    public List<Document> findCvBySimilarityAndMetadata(String query, Filter.Expression expression, int topK) {
        return List.of();
    }

    @Override
    public String findSimilarCv(String query) {
        return "";
    }
}
