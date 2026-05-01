package org.cvanalyzer.backoffice.component.AIAgent;


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
    public String askAgent(String prompt, String query) {
        return chatClient.prompt()
                .user(prompt.formatted(query))
                .call()
                .content();
    }
}
