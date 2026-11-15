package org.cvanalyzer.backoffice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.StringUtil;
import org.cvanalyzer.backoffice.component.ai.advisor.KnowledgeGraphAdvisor;
import org.cvanalyzer.backoffice.model.CvMatcherRecord;
import org.cvanalyzer.backoffice.model.CvMetadataDto;
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


    /**
     * chat client being used to prompt
     */
    private final ChatClient chatClient;
    /**
     * service allowing to handle knowledge graph
     */
    private final KnowledgeGraphService knowledgeGraphService;

    public CvAnalysisServiceImpl(@Qualifier("memoryChatClient") ChatClient chatClient,
                                 KnowledgeGraphService knowledgeGraphService) {
        this.chatClient = chatClient;
        this.knowledgeGraphService = knowledgeGraphService;
    }

    @Override
    public CvMatcherRecord searchCvBasedOnCriteria(String criteria) {


        CvMatcherRecord result = chatClient.prompt()
                .user(Prompts.CV_MATCH)
                .advisors(new KnowledgeGraphAdvisor(criteria, knowledgeGraphService))
                .call()
                .entity(CvMatcherRecord.class,
                        spec -> spec.useProviderStructuredOutput().validateSchema());



//        String structuredContent = result.getResult()
//                .getOutput()
//                .getText();

        if (result  != null && result.filename() != null) {
            log.info("result : {} details  {}", result.filename(), result.details());
            knowledgeGraphService.addNodeInKnowledgeGraph("skills", criteria);
        }
        else {
            log.info("no cv is matching the following criteria {}", criteria);
        }
        return result;
    }
}
