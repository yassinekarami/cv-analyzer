package org.cvanalyzer.backoffice.service.impl;

import org.cvanalyzer.backoffice.service.VectorStoreService;
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
public class StandardSkillsPgVectoreStoreServiceImpl implements VectorStoreService {

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

    @Override
    public void insertIntoVectorStore(String text, String name) {

        Document doc = new Document("text", Map.of("skill", name));
        pgVectorStore.add(List.of(doc));
    }

    @Override
    public List<Document> findDocumentBySimilarity(String query, int topK) {
        return List.of();
    }

    @Override
    public List<Document> findDocumentBySimilarityAndMetadata(String query, Filter.Expression expression, int topK) {
        return List.of();
    }
}
