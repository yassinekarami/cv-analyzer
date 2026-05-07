package org.cvanalyzer.backoffice.ai.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.cvanalyzer.backoffice.service.VectorStoreService;
import org.cvanalyzer.backoffice.utils.LangagesEnum;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * a springAi tool used to generate embedding for standard requirement
 */
@Component
public class CvStandardRequirementTool {


    private final VectorStoreService pgVectorStore;

    public CvStandardRequirementTool(@Qualifier("standardSkillsPgVectoreStoreServiceImpl") VectorStoreService pgVectorStore) {
        this.pgVectorStore = pgVectorStore;
    }

    /**
     * Tools for creating and populating the vector store with the embedding of the standard skills
     * @return  a string
     */
    @Tool(name = "generateStandardEmbedding", description = "Generate the embedding for the standard skills")
    public String generateStandardEmbedding() throws JsonProcessingException {
        this.generateEmbeddingForLangages();
        return "ok";
    }

    private void generateEmbeddingForLangages() throws JsonProcessingException {
        List<String> values = LangagesEnum.getAllValues();
        pgVectorStore.insertIntoVectorStore(values.toString(), "language");
    }
}
