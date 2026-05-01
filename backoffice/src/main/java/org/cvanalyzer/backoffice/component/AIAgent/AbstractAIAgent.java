package org.cvanalyzer.backoffice.component.AIAgent;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;

/**
 * Abstraction for AIAgent
 */
public abstract  class AbstractAIAgent {

    /**
     * chat client
     */
    protected ChatClient chatClient;


    /**
     * make a call to the chatclient with a prompt and a query
     * @param prompt the prompt used by the chat client
     * @param query the query
     * @return the prompt response
     */
    public <T> T askAgent(String prompt, String query, TypeReference<T> typeRef) {
        String response = chatClient.prompt()
                .user(prompt.formatted(query))
                .call()
                .content();

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(response, typeRef);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JSON from LLM", e);
        }
    }
}
