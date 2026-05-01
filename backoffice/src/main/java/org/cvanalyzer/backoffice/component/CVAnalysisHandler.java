package org.cvanalyzer.backoffice.component;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.cvanalyzer.backoffice.service.DocumentService;
import org.cvanalyzer.backoffice.service.StorageService;
import org.cvanalyzer.backoffice.service.VectorStoreService;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CVAnalysisHandler {

    /**
     * vectorStore service to interact with the vectorStore
     */
    private final VectorStoreService vectorStoreService;
    /**
     * documentService used to extract text from file
     */
    private final DocumentService documentService;
    /**
     * storageService used to store files
     */
    private final StorageService storageService;

    public CVAnalysisHandler(@Qualifier("PGVectorStoreSe rviceImpl") VectorStoreService vectorStoreService, DocumentService documentService, StorageService storageService) {
        this.vectorStoreService = vectorStoreService;
        this.documentService = documentService;
        this.storageService = storageService;
    }

    public String handleFileImport(MultipartFile file) throws IOException {
        String result = documentService.extractContentFromFile(file);

        Document doc = new Document(result);
        storageService.store(file);
        this.vectorStoreService.insertIntoVectorStore(doc.getText(), file.getOriginalFilename());

        return "OK";
    }
}
