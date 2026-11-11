//package org.cvanalyzer.MCPServer.tools;
//
//import org.cvanalyzer.MCPServer.service.AnalyseService;
//import org.cvanalyzer.MCPServer.service.impl.CvImportServiceImpl;
//import org.cvanalyzer.MCPServer.utils.CvAnalyzerUtils;
//import org.springframework.ai.document.Document;
//import org.springframework.ai.mcp.annotation.McpTool;
//import org.springframework.ai.mcp.annotation.McpToolParam;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
///**
// * a springAi tool used to perform similarity search in vectore database
// */
//@Component
//public class CvMatcherTool {
//
//    /**
//     * service for querying  the vector store
//     */
//    private final CvImportServiceImpl pgVectorStore;
//
//    /**
//     * Constructor for pgVectorStore
//     * @param pgVectorStore pgVectorStore service
//     */
//    public CvMatcherTool(@Qualifier("PGVectorStoreServiceImpl") CvImportServiceImpl pgVectorStore) {
//        this.pgVectorStore = pgVectorStore;
//    }
//
//    /**
//     * Tool for performing a similarity search in vector store with the given query
//     * @param query the query to use for similarity search
//     * @return the filename of the similar document
//     */
//    @McpTool(name = "findBySimilarity", description = "Search CVs using similarity")
//    public String findBySimilarity(@McpToolParam(description = "the query for similarity search") String query) {
//        List<Document> results = pgVectorStore.findCvBySimilarity(query, 1);
//
//        Set<Map<String, Object>> metadatas = CvAnalyzerUtils.extractMetaData(results);
//        List<String> filesName = CvAnalyzerUtils.extractFilesNameFromMetadatas(metadatas);
//        return filesName.toString();
//
//    }
//}
