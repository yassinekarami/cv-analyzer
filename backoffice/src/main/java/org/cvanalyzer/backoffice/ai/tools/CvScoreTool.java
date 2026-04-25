package org.cvanalyzer.backoffice.ai.tools;


import lombok.AllArgsConstructor;
import org.cvanalyzer.backoffice.service.VectorStoreService;
import org.cvanalyzer.backoffice.utils.CvAnalyzerUtils;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.ai.vectorstore.filter.Filter;

/**
 * a springAi tool used to give a score to a given CV
 */
@Component
@AllArgsConstructor
public class CvScoreTool {

    /**
     * service for querying  the vector store
     */
    @Autowired
    private final VectorStoreService pgVectorStore;

    @Tool(
            name = "scoreComputeTool",
            description = "Rank CV according to the skills in the query"
    )
    public String scoreCompute(String query) {

        List<Document> results = pgVectorStore.findDocumentBySimilarityAndMetadata(query,
                new Filter.Expression(Filter.ExpressionType.EQ,
                        new Filter.Key("categorie"),
                        new Filter.Value("skills")),
                10);

        Set<Map<String, Object>> metadatas = CvAnalyzerUtils.extractMetaData(results);
        List<String> filenames = CvAnalyzerUtils.extractFilesNameFromMetadatas(metadatas);
        for (String filename: filenames) {
            retrieveCvDetailsFromFilename(filename);
        }
        return "OK";
    }

    private void retrieveCvDetailsFromFilename(String filename) {
        pgVectorStore.
        List<Document> results = pgVectorStore.findDocumentBySimilarityAndMetadata("cv-1.pdf",
                new Filter.Expression(Filter.ExpressionType.EQ,
                        new Filter.Key("filename"),
                        new Filter.Value(filename)), 1);
    }

}
