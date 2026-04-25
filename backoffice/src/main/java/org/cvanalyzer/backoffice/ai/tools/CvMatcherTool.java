package org.cvanalyzer.backoffice.ai.tools;

import lombok.AllArgsConstructor;
import org.cvanalyzer.backoffice.service.VectorStoreService;
import org.cvanalyzer.backoffice.utils.CvAnalyzerUtils;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * a springAi tool used to perform similarity search in vectore database
 */
@Component
@AllArgsConstructor
public class CvMatcherTool {

    /**
     * service for querying  the vector store
     */
    @Autowired
    private final VectorStoreService pgVectorStore;

    /**
     * Tool for performing a similarity search in vector store with the given query
     * @param query the query to use for similarity search
     * @return the filename of the similar document
     */
    @Tool(name = "findBySimilarity", description = "Search CVs using similarity")
    public String findBySimilarity(String query) {
        List<Document> results = pgVectorStore.findDocumentBySimilarity(query, 1);

        Set<Map<String, Object>> metadatas = CvAnalyzerUtils.extractMetaData(results);
        List<String> filesName = CvAnalyzerUtils.extractFilesNameFromMetadatas(metadatas);
       // storageService.loadAsResources(filesName);
        return filesName.toString();

    }

}
