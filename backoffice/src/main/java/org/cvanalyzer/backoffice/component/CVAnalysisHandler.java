package org.cvanalyzer.backoffice.component;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.cvanalyzer.backoffice.service.DocumentService;
import org.cvanalyzer.backoffice.service.StorageService;
import org.cvanalyzer.backoffice.service.VectorStoreService;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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

    @Autowired
    private StorageService storageService;

    public String handleFileImport(MultipartFile file) throws IOException {
        String result = documentService.extractContentFromFile(file);

        Document doc = new Document(result);
        storageService.store(file);
        this.vectorStoreService.insertIntoVectorStore(doc.getText(), file.getOriginalFilename());

        return "OK";
    }


//    public String handleFileSimilaritySearch() {
//        List<Document> results = vectorStoreService.findDocumentBySimilarity("Captain of the black pearl");
//
//        Set<Map<String, Object>> metadatas = extractMetaData(results);
//        List<String> filesName = extractFilesNameFromMetadatas(metadatas);
//        storageService.loadAsResources(filesName);
//        return results.stream().findFirst().toString();
//    }
//
//
//    private Set<Map<String, Object>> extractMetaData(List<Document> documents) {
//        return documents.stream()
//                .map(Document::getMetadata)
//                .collect(Collectors.toUnmodifiableSet());
//    }
//
//    private List<String> extractFilesNameFromMetadatas(Set<Map<String, Object>> metadatas) {
//        return metadatas.stream()
//                .map(metadata -> (String) metadata.get("fileName"))
//                .filter(Objects::nonNull)
//                .toList();
//    }
}
