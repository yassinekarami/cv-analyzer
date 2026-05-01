package org.cvanalyzer.backoffice.component.AIAgent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Component;


import static org.cvanalyzer.backoffice.ai.prompt.Prompts.AGENT_DEFAULT_SYSTEM_PROMPT;

/**
 * AiAgent for interacting with chat client only
 */
@Component
public class ChatAIAgent extends AbstractAIAgent{

    /**
     * constuctor for AIAgent
     * @param builder
     * @param memory
     */
    public ChatAIAgent(ChatClient.Builder builder, ChatMemory memory) {

        this.chatClient = builder
                .defaultSystem(AGENT_DEFAULT_SYSTEM_PROMPT) .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(memory).build()
                )
                .build();
    }

}
