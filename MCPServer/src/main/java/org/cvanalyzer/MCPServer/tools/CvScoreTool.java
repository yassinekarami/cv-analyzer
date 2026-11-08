//package org.cvanalyzer.MCPServer.tools;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import org.cvanalyzer.MCPServer.record.CvScoreResponseRecord;
//import org.cvanalyzer.MCPServer.record.EmbeddedCvRecord;
//import org.cvanalyzer.MCPServer.repository.CvMapper;
//import org.cvanalyzer.MCPServer.service.CvScoreService;
//import org.cvanalyzer.MCPServer.service.AnalyseService;
//import org.cvanalyzer.MCPServer.utils.CvAnalyzerUtils;
//import org.springframework.ai.document.Document;
//import org.springframework.ai.mcp.annotation.McpTool;
//import org.springframework.ai.vectorstore.filter.Filter;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//import java.util.*;
//
///**
// * a springAi tool used to give a score to a given CV
// */
//@Component
//public class CvScoreTool {
//
//    /**
//     * service for querying  the vector store
//     */
//    private final AnalyseService pgVectorStore;
//
//    /**
//     * Cv repository
//     */
//    private final CvMapper cvRepository;
//
//    /**
//     * Cv score service
//     */
//    private final CvScoreService cvScoreService;
//
//    /**
//     * constructor
//     * @param pgVectorStore pgVectorStore
//     * @param cvRepository cvRepository
//     */
//    public CvScoreTool(@Qualifier("PGVectorStoreServiceImpl") AnalyseService pgVectorStore,
//                       CvMapper cvRepository,
//                       CvScoreService cvScoreService) {
//        this.pgVectorStore = pgVectorStore;
//        this.cvRepository = cvRepository;
//        this.cvScoreService = cvScoreService;
//    }
//
//
//    @McpTool(
//            name = "scoreComputeTool",
//            description = "Rank CV according to the skills in the query"
//    )
//    public List<CvScoreResponseRecord> scoreCompute(String query) throws JsonProcessingException {
//
//        List<Document> results = pgVectorStore.findCvBySimilarityAndMetadata(query,
//                new Filter.Expression(Filter.ExpressionType.EQ,
//                        new Filter.Key("categorie"),
//                        new Filter.Value("skills")),
//                10);
//
//        if (CollectionUtils.isEmpty(results)) {
//            return new ArrayList<>();
//        } else {
//            Set<Map<String, Object>> metadatas = CvAnalyzerUtils.extractMetaData(results);
//            List<String> filesname = CvAnalyzerUtils.extractFilesNameFromMetadatas(metadatas);
//            return retrieveCvDetailsFromFilename(filesname);
//        }
//    }
//
//    private List<CvScoreResponseRecord> retrieveCvDetailsFromFilename(List<String> filesname) throws JsonProcessingException {
//        List<EmbeddedCvRecord> embeddedCvDtos = cvRepository.findByFilename(filesname);
//        Map<String, List<EmbeddedCvRecord>> fileNameEmbeddingMap = new HashMap<>();
//        List<CvScoreResponseRecord> response = new ArrayList<>();
//
//        for (EmbeddedCvRecord embeddedCvDto : embeddedCvDtos) {
//            fileNameEmbeddingMap
//                    .computeIfAbsent(embeddedCvDto.filename(), k -> new ArrayList<>())
//                    .add(embeddedCvDto);
//        }
//
//        for (Map.Entry<String, List<EmbeddedCvRecord>> entry : fileNameEmbeddingMap.entrySet()) {
//            response.add(cvScoreService.calculateCvScore(entry.getValue()));
//
//        }
//        return response;
//    }
//}
