package org.cvanalyzer.backoffice.component;

import lombok.AllArgsConstructor;

import org.cvanalyzer.backoffice.service.DocumentService;
import org.cvanalyzer.backoffice.service.VectorStoreService;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class CVAnalysisHandler {

    /**
     * vectorStore service to interact with the vectorStore
     */
    @Autowired
    private VectorStoreService vectorStoreService;

    /**
     * documentService used to extract text from file
     */
    @Autowired
    private DocumentService documentService;

    public String handleFileImport(MultipartFile file) throws IOException {
        String result = documentService.extractContentFromFile(file);

        Document doc = new Document(result);
        this.vectorStoreService.insertIntoVectorStore(doc.getText(), file.getOriginalFilename());

        return "OK";
    }


    public String handleFileSimilaritySearch() {
        List<Document> results = vectorStoreService.findDocumentBySimilarity();

        return results.stream().findFirst().toString();
    }
}
