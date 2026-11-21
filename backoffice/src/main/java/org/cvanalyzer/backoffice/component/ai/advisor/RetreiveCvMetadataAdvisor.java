package org.cvanalyzer.backoffice.component.ai.advisor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.cvanalyzer.backoffice.model.UserMetadataRecord;
import org.cvanalyzer.backoffice.service.KnowledgeGraphService;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.Objects;

@Slf4j
public class RetreiveCvMetadataAdvisor implements BaseAdvisor
{
    private final ObjectMapper objectMapper;

    private final KnowledgeGraphService knowledgeGraphService;

    public RetreiveCvMetadataAdvisor(KnowledgeGraphService knowledgeGraphService,
                                     ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
        this.knowledgeGraphService = knowledgeGraphService;

    }
    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {

        UserMetadataRecord userMetadata = knowledgeGraphService.findUserAndEmail("userSessionId");
        String organizerEmail = "yassine_karami@hotmail.com";
        String candidateEmail = userMetadata.email();
        String userPrompt = Objects.requireNonNull(chatClientRequest.prompt().getUserMessage().getText())
                .formatted(organizerEmail, candidateEmail);
        Prompt prompt = chatClientRequest.prompt().augmentUserMessage(userPrompt);

        return chatClientRequest.mutate().prompt(prompt).build();
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        return chatClientResponse;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
