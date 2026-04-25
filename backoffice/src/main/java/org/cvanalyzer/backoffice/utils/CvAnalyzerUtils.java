package org.cvanalyzer.backoffice.utils;

import org.springframework.ai.document.Document;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utils class for CvAnalyzer
 */
public class CvAnalyzerUtils {

    /**
     * method to extract metadata from documents
     * @param documents list of documents to extract metadata from
     * @return set of map containing metadata
     */
    public static Set<Map<String, Object>> extractMetaData(List<Document> documents) {
        return documents.stream()
                .map(Document::getMetadata)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * extract filename for a set of metadatas
     * @param metadatas set of metadata
     * @return a list of filenames
     */
    public static List<String> extractFilesNameFromMetadatas(Set<Map<String, Object>> metadatas) {
        return metadatas.stream()
                .map(metadata -> (String) metadata.get("filename"))
                .filter(Objects::nonNull)
                .toList();
    }
}
