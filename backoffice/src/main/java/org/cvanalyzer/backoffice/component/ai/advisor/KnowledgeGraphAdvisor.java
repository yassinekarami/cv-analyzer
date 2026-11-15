package org.cvanalyzer.backoffice.component.ai.advisor;

import org.cvanalyzer.backoffice.service.KnowledgeGraphService;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.prompt.Prompt;


import java.util.List;
import java.util.Objects;

public class KnowledgeGraphAdvisor implements BaseAdvisor {


    private final String userRequest;

    private final KnowledgeGraphService graphService;


    public KnowledgeGraphAdvisor(String userRequest, KnowledgeGraphService graphService) {

        this.userRequest = userRequest;
        this.graphService = graphService;
    }

    /**
     * before the advisor augment the user query with nodes from the knowledge graph
     * @param chatClientRequest
     * @param advisorChain
     * @return the new chat client request
     */
    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {

        List<String> retrievedkills = graphService.retreiveRelatedSkills("skills");

        String userPrompt = Objects.requireNonNull(chatClientRequest.prompt().getUserMessage().getText()).formatted(createSkillsString(retrievedkills, userRequest));

        Prompt prompt = chatClientRequest.prompt().augmentUserMessage(userPrompt);

        return chatClientRequest.mutate().prompt(prompt).build();

    }


    /**
     *
     * @param skills
     * @param userRequest
     * @return
     */
    private String createSkillsString(List<String> skills, String userRequest) {
        StringBuilder result = new StringBuilder();
        result.append(userRequest);
        skills.forEach(result::append);
        return result.toString();
    }
    /**
     * after the advisor add a new relation in the knowledge graph
     * @param chatClientResponse
     * @param advisorChain
     * @return the chat client response
     */
    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {

        return chatClientResponse;
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
