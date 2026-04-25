package org.cvanalyzer.backoffice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.filter.Filter;

import java.util.List;

/**
 * VectorStore service used to insert or retrieve data
 */
public interface  VectorStoreService {

    /**
     * insert a text in vector store
     * @param text the text to insert
     * @param name the file name used for metadata
     */
    void insertIntoVectorStore(String text, String name) throws JsonProcessingException;


    /**
     * perform a similarity search on the vectorStore and return result
     * @param query the query to use for similarity search
     * @param topK the number of result to be returned from vectorStore
     * @return a list of document returned by the similarity search
     */
    List<Document> findDocumentBySimilarity(String query, int topK);

    /**
     * search by similarity and metadata on the vectorStore and return result
     * @param query the query to use for similarity search
     * @param expression expression used to filter metadata
     * @param topK the number of result to be returned from vectorStore
     * @return a list of document returned matching the query and the filter expression
     */
    List<Document> findDocumentBySimilarityAndMetadata(String query, Filter.Expression expression, int topK);
}
