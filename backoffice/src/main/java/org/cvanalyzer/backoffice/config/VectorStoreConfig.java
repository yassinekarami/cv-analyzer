package org.cvanalyzer.backoffice.config;

import org.springframework.ai.autoconfigure.vectorstore.pgvector.PgVectorStoreAutoConfiguration;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class VectorStoreConfig {

    @Bean
    public VectorStore vectorStore() {
        return null;
    }
}
