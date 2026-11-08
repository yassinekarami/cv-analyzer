package org.cvanalyzer.MCPServer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.cvanalyzer.MCPServer.record.CvRecord;
import org.cvanalyzer.MCPServer.record.CvScoreResponseRecord;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.filter.Filter;

import java.util.List;

/**
 * VectorStore service used to insert or retrieve data
 */
public interface AnalyseService {

    /**
     * insert a text in vector store
     * @param text to analyse and insert
     * @param name the file name used for metadata
     */
    CvRecord analyzeAndIndex(String text, String name) throws JsonProcessingException;


    /**
     * perform a similarity search on the vectorStore and return result
     * @param query the query to use for similarity search
     * @param topK the number of result to be returned from vectorStore
     * @return a list of document returned by the similarity search
     */
    List<Document> findCvBySimilarity(String query, int topK);

    /**
     * search by similarity and metadata on the vectorStore and return result
     * @param query the query to use for similarity search
     * @param expression expression used to filter metadata
     * @param topK the number of result to be returned from vectorStore
     * @return a list of document returned matching the query and the filter expression
     */
    List<Document> findCvBySimilarityAndMetadata(String query, Filter.Expression expression, int topK);


    public String findSimilarCv(String query);
}
