package org.cvanalyzer.backoffice.component.ai.advisor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.StringUtil;
import org.cvanalyzer.backoffice.model.UserMetadataRecord;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.messages.MessageType;

import java.util.HashMap;
import java.util.Map;

/**
 * advisor used to enhance the metadata to be stored in the neo4j graph
 */
@Slf4j
public class CustomMessageChatMemoryAdvisor implements BaseAdvisor {

    private final ObjectMapper objectMapper;

    public CustomMessageChatMemoryAdvisor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        return chatClientRequest;
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {

        if (chatClientResponse.chatResponse() == null || chatClientResponse.chatResponse().getResult() == null) {
            return chatClientResponse;
        }
        String output = chatClientResponse.chatResponse().getResult().getOutput().getText();
        if (StringUtil.isNotBlank(output) && MessageType.ASSISTANT.equals(chatClientResponse.chatResponse().getResult().getOutput().getMessageType())) {
            try {
                UserMetadataRecord metadata = objectMapper.readValue(output, UserMetadataRecord.class);
                Map<String, Object> map = new HashMap<>();

                map.put("details", metadata.details());
                map.put("email", metadata.email());
                map.put("filename", metadata.filename());
                map.put("name", metadata.name());
                map.put("sessionId", "userSessionId");
                chatClientResponse.chatResponse().getResults().stream().findFirst().get().getOutput().getMetadata().putAll(map);

                chatClientResponse.context().putAll(map);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return  chatClientResponse;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
