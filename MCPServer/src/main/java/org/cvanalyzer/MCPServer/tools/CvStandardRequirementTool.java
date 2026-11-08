package org.cvanalyzer.MCPServer.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.cvanalyzer.MCPServer.constant.LangagesEnum;
import org.cvanalyzer.MCPServer.service.AnalyseService;
import org.cvanalyzer.MCPServer.service.impl.CvAnalysisServiceImpl;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * a springAi tool used to generate embedding for standard requirement
 */
@Component
public class CvStandardRequirementTool {

    private final CvAnalysisServiceImpl pgVectorStoreService;

    private final AnalyseService pgVectorStore;

    public CvStandardRequirementTool(CvAnalysisServiceImpl pgVectorStoreService, @Qualifier("standardSkillsPgVectoreStoreServiceImpl") AnalyseService pgVectorStore) {
        this.pgVectorStoreService = pgVectorStoreService;
        this.pgVectorStore = pgVectorStore;
    }

    /**
     * Tools for creating and populating the vector store with the embedding of the standard skills
     * @return  a string
     */
    @McpTool(name = "generateStandardEmbedding", description = "Generate the embedding for the standard skills")
    public String generateStandardEmbedding() throws JsonProcessingException {
        this.generateEmbeddingForLangages();
        return "ok";
    }

    private void generateEmbeddingForLangages() throws JsonProcessingException {
        List<String> values = LangagesEnum.getAllValues();
   //     pgVectorStore.insertIntoVectorStore(values.toString(), "language");
    }
}
