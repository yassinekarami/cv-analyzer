package org.cvanalyzer.backoffice.ai.tools;

import lombok.AllArgsConstructor;
import org.cvanalyzer.backoffice.service.VectorStoreService;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * a springAi tool used to perform similarity search in vectore database
 */
@Component
@AllArgsConstructor
public class CvMatcherTool {

    /**
     * pgVectoreStore
     */
    @Autowired
    private final VectorStoreService pgVectorStore;

    /**
     * Tool for performing a similarity search in vector store with the given query
     * @param query the query to use for similarity search
     * @return the filename of the similar document
     */
    @Tool(name = "findBySimilarity", description = "Search CVs using similarity")
    public String findBySimilarity(String query) {
        List<Document> results = pgVectorStore.findDocumentBySimilarity(query);

        Set<Map<String, Object>> metadatas = extractMetaData(results);
        List<String> filesName = extractFilesNameFromMetadatas(metadatas);
       // storageService.loadAsResources(filesName);
        return filesName.toString();

    }

    /**
     * method to extract metadata from documents
     * @param documents list of documents to extract metadata from
     * @return set of map containing metadata
     */
    private Set<Map<String, Object>> extractMetaData(List<Document> documents) {
        return documents.stream()
                .map(Document::getMetadata)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * extract filename for a set of metadatas
     * @param metadatas set of metadata
     * @return a list of filenames
     */
    private List<String> extractFilesNameFromMetadatas(Set<Map<String, Object>> metadatas) {
        return metadatas.stream()
                .map(metadata -> (String) metadata.get("fileName"))
                .filter(Objects::nonNull)
                .toList();
    }

}
