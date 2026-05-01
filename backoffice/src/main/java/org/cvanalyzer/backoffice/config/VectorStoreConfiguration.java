package org.cvanalyzer.backoffice.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class VectorStoreConfiguration {

    @Value("${spring.ai.vectorstore.pgvector.distance-type}")
    public String distanceType;

    @Value("${spring.ai.vectorstore.pgvector.max-document-batch-size}")
    public String maxDistanceBatchSize;

    @Value("${spring.ai.vectorstore.pgvector.index-type}")
    public String indexType;

    @Bean("standardSkillsVectorStore")
    public PgVectorStore standardSkillsVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .schemaName("public")
                .vectorTableName("standardSkillsVectorStore")
                .distanceType(PgVectorStore.PgDistanceType.valueOf(distanceType))
                .removeExistingVectorStoreTable(true)
                .initializeSchema(true)
                .indexType(PgVectorStore.PgIndexType.valueOf(indexType))
                .maxDocumentBatchSize(Integer.parseInt(maxDistanceBatchSize))
                .build();
    }

    @Bean("vector_store")
    public PgVectorStore vectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .schemaName("public")
                .vectorTableName("vector_store")
                .distanceType(PgVectorStore.PgDistanceType.valueOf(distanceType))
                .removeExistingVectorStoreTable(true)
                .initializeSchema(true)
                .indexType(PgVectorStore.PgIndexType.valueOf(indexType))
                .maxDocumentBatchSize(Integer.parseInt(maxDistanceBatchSize))
                .build();
    }
}
