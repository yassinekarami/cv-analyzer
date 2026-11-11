package org.cvanalyzer.MCPServer.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cvanalyzer.MCPServer.record.EmbeddedCvRecord;
import org.cvanalyzer.MCPServer.repository.CvMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class Config {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ChatClient chatClient(OpenAiChatModel chatModel) {
        return ChatClient.create(chatModel);
    }

    @Bean
    public CvMapper cvMapper() {
        return new CvMapper() {
            @Override
            public List<EmbeddedCvRecord> findByFilename(List<String> filesname) {
                return List.of();
            }
        };
    }
}
