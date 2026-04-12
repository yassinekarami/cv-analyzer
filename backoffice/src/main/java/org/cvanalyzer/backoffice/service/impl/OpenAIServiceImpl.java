package org.cvanalyzer.backoffice.service.impl;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.cvanalyzer.backoffice.service.AiService;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class OpenAIServiceImpl implements AiService {

    @Value("${spring.ai.openai.api-key}")
    private String openAiKey;

    @Value("${spring.ai.openai.chat.options.model}")
    private String model;

    @Value("${spring.ai.openai.embedding.options.model}")
    private String embedding;


    public EmbeddingResponse generateEmbeddingFromInput(String input) {

        OpenAiApi openAiApi = OpenAiApi.builder()
                .apiKey(openAiKey)
                .build();


        OpenAiEmbeddingModel embeddingModel = new OpenAiEmbeddingModel(
                openAiApi,
                MetadataMode.EMBED,
                OpenAiEmbeddingOptions.builder()
                        .model(embedding)
                        .user("user-6")
                        .build(),
                RetryUtils.DEFAULT_RETRY_TEMPLATE);

        return embeddingModel
                .embedForResponse(List.of(input));

    }
}
