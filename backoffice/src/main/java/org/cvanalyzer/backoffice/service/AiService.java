package org.cvanalyzer.backoffice.service;

import org.springframework.ai.embedding.EmbeddingResponse;

public interface AiService {
    EmbeddingResponse generateEmbeddingFromInput(String input);
}
