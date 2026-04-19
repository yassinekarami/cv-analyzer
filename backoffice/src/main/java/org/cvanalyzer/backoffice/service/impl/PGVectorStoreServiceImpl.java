package org.cvanalyzer.backoffice.service.impl;

import lombok.AllArgsConstructor;
import org.cvanalyzer.backoffice.service.VectorStoreService;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PGVectorStoreServiceImpl implements VectorStoreService {

    /**
     * pgVectoreStore
     */
    @Autowired
    private final PgVectorStore pgVectorStore;

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertIntoVectorStore(String text, String input) {

        List<Document> chunks = this.buildChunksFromString(text, input);
        pgVectorStore.add(chunks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Document> findDocumentBySimilarity(String query) {
        return this.pgVectorStore
                .similaritySearch(SearchRequest.builder().query(query)
                        .topK(1).build());
    }

    /**
     * convert a text into multiple chunks and create a document foreach chunks
     * @param text the text to create chunks from
     * @param documentName the document name used for the document metadata
     * @return a list of documents
     */
    private List<Document> buildChunksFromString(String text, String documentName) {
        List<Document> docs = new ArrayList<>();
        char[] charArray = text.toCharArray();
        StringBuilder builder = new StringBuilder();
        int chunkPosition = 0;
        for(int i = 0 ; i < text.length(); i++) {

            if (builder.toString().length() < 100) {
                builder.append(charArray[i]);
            }
            else {
                chunkPosition ++;
                docs.add(new Document(builder.toString(), Map.of("fileName", documentName, "position", chunkPosition)));
                builder = new StringBuilder();
            }
        }
        return docs;
    }
}
