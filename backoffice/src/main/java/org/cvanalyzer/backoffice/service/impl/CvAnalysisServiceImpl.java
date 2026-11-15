package org.cvanalyzer.backoffice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.StringUtil;
import org.cvanalyzer.backoffice.component.ai.advisor.KnowledgeGraphAdvisor;
import org.cvanalyzer.backoffice.model.CvMatcherDto;
import org.cvanalyzer.backoffice.service.CvAnalysisService;
import org.cvanalyzer.backoffice.service.KnowledgeGraphService;
import org.cvanalyzer.backoffice.utils.Prompts;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CvAnalysisServiceImpl implements CvAnalysisService {


    private final String id;

    /**
     * chat client being used to prompt
     */
    private final ChatClient chatClient;

    private final KnowledgeGraphService knowledgeGraphService;

    public CvAnalysisServiceImpl(@Qualifier("memoryChatClient") ChatClient chatClient,
                                 KnowledgeGraphService knowledgeGraphService,
                                    @Qualifier("sessionId") String id) {
        this.chatClient = chatClient;
        this.knowledgeGraphService = knowledgeGraphService;
        this.id = id;

    }

    @Override
    public CvMatcherDto searchCvBasedOnCriteria(String criteria) {


        ChatResponse result = chatClient.prompt()
                .user(Prompts.CV_MATCH)
                .advisors(new KnowledgeGraphAdvisor(criteria, knowledgeGraphService))
                .call()
                .chatResponse();



        String structuredContent = result.getResult()
                .getOutput()
                .getText();
        if (StringUtil.isNotBlank(structuredContent)) {
            knowledgeGraphService.addNodeInKnowledgeGraph("skills", criteria);
        }

        return null;
    }
}
