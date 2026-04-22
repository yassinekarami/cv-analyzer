package org.cvanalyzer.backoffice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.cvanalyzer.backoffice.model.CvDto;
import org.cvanalyzer.backoffice.service.VectorStoreService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
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

    @Autowired
    private final ChatClient chatClient;

    private final ObjectMapper objectMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertIntoVectorStore(String text, String filename) throws JsonProcessingException {

        ChatResponse chatResponse = chatClient.prompt()
                .user("""

You are a JSON generator.

Your task is to generate a structured JSON resume that strictly matches the following schema.

RULES (MANDATORY):
- Output ONLY valid JSON. No explanations, no markdown, no comments.
- The JSON must be directly parsable by Jackson into a Java DTO.
- All field names must match exactly (case-sensitive).
- If a value is unknown or missing, use null.
- Do not omit any fields.
- Do not add extra fields.
- Use empty arrays [] instead of null for lists when no data is available.
- Use null only for scalar fields (String, Map values, etc.) when unknown.
- Ensure valid JSON syntax (quotes, commas, brackets).

SCHEMA:
{
  "profile": {
    "name": String|null,
    "email": String|null,
    "nationality": String|null,
    "links": [String],
    "title": String|null,
    "languages": { "String": "String" }
  },
  "experience": [
    {
      "role": String|null,
      "dates": String|null,
      "company": String|null,
      "location": String|null,
      "description": String|null
    }
  ],
  "education": [
    {
      "degree": String|null,
      "school": String|null,
      "year": String|null
    }
  ],
  "skills": [String],
  "publications": [
    {
      "title": String|null,
      "publisher": String|null
    }
  ],
  "talks": [
    {
      "title": String|null,
      "event": String|null,
      "location": String|null,
      "date": String|null
    }
  ],
  "certifications": [
    {
      "title": String|null,
      "description": String|null
    }
  ],
  "other": [Object]
}

CONSTRAINTS:
- "languages" must always be an object (use {} if empty).
- Arrays must always be present (never null).
- Strings must be valid JSON strings.
- Keep realistic but concise content.

Now generate the JSON resume.
""" + text)
                .call()
           //     .entity(CvDto.class);
               .chatResponse();


       String message = chatResponse.getResult().getOutput().getText();
       CvDto cv = objectMapper.readValue(message, CvDto.class);
        List<Document> chunks = this.buildChunksFromString(text, filename);
       // pgVectorStore.add( chatResponse.getResult().getOutput());
        pgVectorStore.add(convertCvDtoToDocument(cv, filename));
    }

    private List<Document> convertCvDtoToDocument(CvDto cvDto, String filename) throws JsonProcessingException {
        List<Document> docs = new ArrayList<>();
        docs.add(new Document(objectMapper.writeValueAsString(cvDto.profile()), Map.of("filename", filename, "categorie", "profile")));
        docs.add(new Document(objectMapper.writeValueAsString(cvDto.experience()), Map.of("filename", filename, "categorie", "experience")));
        docs.add(new Document(objectMapper.writeValueAsString(cvDto.skills()), Map.of("filename", filename, "categorie", "skills")));
        docs.add(new Document(objectMapper.writeValueAsString(cvDto.publications()), Map.of("filename", filename, "categorie", "publications")));
        docs.add(new Document(objectMapper.writeValueAsString(cvDto.talks()), Map.of("filename", filename, "categorie", "talks")));
        docs.add(new Document(objectMapper.writeValueAsString(cvDto.certifications()), Map.of("filename", filename, "categorie", "certifications")));
        docs.add(new Document(objectMapper.writeValueAsString(cvDto.other()), Map.of("filename", filename, "categorie", "other")));
        return docs;

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
