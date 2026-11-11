package org.cvanalyzer.backoffice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.cvanalyzer.backoffice.model.CvMatcherDto;
import org.cvanalyzer.backoffice.service.CvAnalysisService;
import org.cvanalyzer.backoffice.utils.Prompts;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class CvAnalysisServiceImpl implements CvAnalysisService {

    /**
     * the conversation uuid
     */
    UUID uuid = UUID.randomUUID();
    /**
     * chat client being used to prompt
     */
    private final ChatClient chatClient;

    public CvAnalysisServiceImpl(@Qualifier("memoryChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public CvMatcherDto searchCvBasedOnCriteria(String criteria) {

        chatClient.prompt()
                .user(Prompts.CV_MATCH.formatted(criteria))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, uuid.toString()))
                .call()
                .content();

        return null;
    }
}
