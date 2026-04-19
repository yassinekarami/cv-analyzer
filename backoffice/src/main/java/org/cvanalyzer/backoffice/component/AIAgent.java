package org.cvanalyzer.backoffice.component;

import org.cvanalyzer.backoffice.ai.tools.CvMatcherTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * AIAgent used to call tools
 */
@Component
public class AIAgent {

    private ChatClient chatClient;


    /**
     * constuctor for AIAgent
     * @param builder
     * @param memory
     * @param cvMatcherTool
     */
    private AIAgent(ChatClient.Builder builder, ChatMemory memory, CvMatcherTool cvMatcherTool) {

        this.chatClient = builder
                .defaultSystem("""
            You are an HR assistant specialized in CV matching.

            You do NOT have access to any CV data.

            To answer:
            - You MUST call the tool 'findBySimilarity'
            - The tool is the ONLY way to retrieve CVs
            - Do not answer without using the tool
        """) .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(memory).build()
                )
                .defaultTools(ToolCallbacks.from(cvMatcherTool))
                .build();
    }

    /**
     *
     * @param query
     * @return
     */
    public String askAgent(String query) {
        return chatClient.prompt()
                .user("""
                    Find the best matching CV for the following request:

                    %s

                    You must call the tool 'findBySimilarity' before answering.
                """.formatted(query))
                    .call()
                    .content();
    }
}

