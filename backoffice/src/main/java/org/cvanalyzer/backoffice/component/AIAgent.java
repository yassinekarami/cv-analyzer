package org.cvanalyzer.backoffice.component;

import org.cvanalyzer.backoffice.ai.tools.CvMatcherTool;
import org.cvanalyzer.backoffice.ai.tools.CvScoreTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static org.cvanalyzer.backoffice.ai.prompt.Prompts.AGENT_DEFAULT_SYSTEM_PROMPT;

/**
 * AIAgent used to call tools
 */
@Component
public class AIAgent {

    private ChatClient chatClient;


    /**
     * constuctor for AIAgent
     * @param builder
     * @param memory
     * @param cvMatcherTool
     * @param cvScoreTool
     */
    private AIAgent(ChatClient.Builder builder, ChatMemory memory, CvMatcherTool cvMatcherTool, CvScoreTool cvScoreTool) {

        this.chatClient = builder
                .defaultSystem(AGENT_DEFAULT_SYSTEM_PROMPT) .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(memory).build()
                )
                .defaultTools(ToolCallbacks.from(cvMatcherTool, cvScoreTool))
                .build();
    }

    /**
     * make a call to the chatclient with a prompt and a query
     * @param prompt the prompt used by the chat client
     * @param query the query
     * @return the prompt response
     */
    public String askAgent(String prompt, String query) {
        return chatClient.prompt()
                .user(prompt.formatted(query))
                    .call()
                    .content();
    }
}

