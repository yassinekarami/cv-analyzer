package org.cvanalyzer.backoffice.component;

import lombok.AllArgsConstructor;
import org.cvanalyzer.backoffice.service.AiService;
import org.cvanalyzer.backoffice.service.DocumentService;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class CVAnalysisHandler {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private AiService aiService;

    public String handleFileImport(String input) throws IOException {
        String result = documentService.extractContentFromFile(input);
        EmbeddingResponse res = aiService.generateEmbeddingFromInput(result);
        return EmbeddingResponse;
    }
}
