package org.cvanalyzer.backoffice.service

import org.springframework.ai.embedding.EmbeddingResponse

interface AiService {

    EmbeddingResponse generateEmbeddingFromInput(String input);
}