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


import java.util.Arrays;
import java.util.UUID;

import static org.cvanalyzer.backoffice.utils.Prompts.AGENT_DEFAULT_SYSTEM_PROMPT;

@Configuration
public class AIAgentConfiguration {


    /**
     * the conversation uuid
     */
    UUID uuid = UUID.randomUUID();

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
    public ChatClient noMemoryChatClient(OpenAiChatModel chatModel, SyncMcpToolCallbackProvider mcpToolProvider) {
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
    public ChatClient memoryChatClient(OpenAiChatModel chatModel,
                                 SyncMcpToolCallbackProvider mcpToolProvider,
                                 @Qualifier("neo4jchatmemory")ChatMemory memory) {

        return ChatClient.builder(chatModel)
                .defaultTools(mcpToolProvider)
                .defaultAdvisors(a -> {
                    a.param(ChatMemory.CONVERSATION_ID, uuid.toString());
                    a.advisors(MessageChatMemoryAdvisor.builder(memory).build());
                })
                .defaultSystem(AGENT_DEFAULT_SYSTEM_PROMPT)
                .build();
    }

    @Bean("sessionId")
    public String id() {
        return uuid.toString();
    }

//    @Bean
//    public ToolCallback[] knowledgeGraphToolCallbacks(SyncMcpToolCallbackProvider provider, KnowledgeGraphService knowledgeGraphService) {
//
//        return Arrays.stream(provider.getToolCallbacks())
//                .map(callback -> new KnowledgeGraphToolCallback(
//                        callback,
//                        knowledgeGraphService)).toArray(ToolCallback[]::new);
//    }

}
