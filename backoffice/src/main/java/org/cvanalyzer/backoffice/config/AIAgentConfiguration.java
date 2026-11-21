package org.cvanalyzer.backoffice.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.client.transport.customizer.McpSyncHttpClientRequestCustomizer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.neo4j.Neo4jChatMemoryRepository;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.webclient.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;


import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;

import static org.cvanalyzer.backoffice.utils.Prompts.AGENT_DEFAULT_SYSTEM_PROMPT;

@Configuration
public class AIAgentConfiguration {


    @Value("${calnode.mcp.api-key}") String apiKey;
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
     * bean for objectMapper
     * @return an instance of objectMapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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




    @Bean
    public WebClientCustomizer calnodeMcpAuthCustomizer() {
        return webClientBuilder -> webClientBuilder.filter(calnodeBearerFilter());
    }

    private ExchangeFilterFunction calnodeBearerFilter() {
        return (request, next) -> {
            if (request.url().getPort() == 3000) {
                ClientRequest authorizedRequest = ClientRequest.from(request)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                        .build();
                return next.exchange(authorizedRequest);
            }
            return next.exchange(request);
        };
    }

}
