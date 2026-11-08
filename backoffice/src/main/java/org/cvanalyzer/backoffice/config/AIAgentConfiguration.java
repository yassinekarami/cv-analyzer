package org.cvanalyzer.backoffice.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.neo4j.Neo4jChatMemoryRepository;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.cvanalyzer.backoffice.utils.Prompts.AGENT_DEFAULT_SYSTEM_PROMPT;

@Configuration
public class AIAgentConfiguration {


    @Autowired
    Neo4jChatMemoryRepository chatMemoryRepository;

    private ChatClient chatClient;

    @Bean("neo4jchatmemory")
    public ChatMemory neo4jChatMemoryRepository() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(10)
                .build();
    }

    /**
     * bean for chat client with no memory
     * @param chatModel the LLM being used
     * @param mcpToolProvider the provider used to discover tools exposed by the mcp server
     * @return a chat client with no memory
     */
    @Bean("noMemoryChatClient")
    public ChatClient chatClient(OpenAiChatModel chatModel, SyncMcpToolCallbackProvider mcpToolProvider) {
        return ChatClient
                .builder(chatModel)
                .defaultTools(mcpToolProvider)
                .defaultSystem(AGENT_DEFAULT_SYSTEM_PROMPT)
                .build();
    }


    /**
     * bean for chat client having memory
     * @param chatModel the LLM being used
     * @param mcpToolProvider the provider used to discover tools exposed by the mcp server
     * @param memory the memory used by the client
     * @return chatClient with memory
     */
    @Bean("memoryChatClient")
    public ChatClient chatClient(OpenAiChatModel chatModel,
                                 SyncMcpToolCallbackProvider mcpToolProvider,
                                 @Qualifier("neo4jchatmemory")ChatMemory memory) {

        return ChatClient.builder(chatModel)
                .defaultTools(mcpToolProvider)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(memory).build())
                .defaultSystem(AGENT_DEFAULT_SYSTEM_PROMPT)
                .build();
    }
}
