package org.cvanalyzer.backoffice.service;

import org.springframework.ai.document.Document;

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
    void insertIntoVectorStore(String text, String name);


    /**
     * perform a similarity search on the vectorStore and return result
     * @param query the query to use for similarity search
     * @return a list of document returned by the similarity search
     */
    List<Document> findDocumentBySimilarity(String query);
}
