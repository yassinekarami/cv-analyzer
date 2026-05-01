package org.cvanalyzer.backoffice.component.AIAgent;

import org.cvanalyzer.backoffice.ai.tools.CvMatcherTool;
import org.cvanalyzer.backoffice.ai.tools.CvScoreTool;
import org.cvanalyzer.backoffice.ai.tools.CvStandardRequirementTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.stereotype.Component;

import static org.cvanalyzer.backoffice.ai.prompt.Prompts.AGENT_DEFAULT_SYSTEM_PROMPT;

/**
 * AIAgent used to call tools
 */
@Component
public class ToolAIAgent extends AbstractAIAgent{


    /**
     * constuctor for AIAgent
     * @param builder
     * @param memory
     * @param cvMatcherTool
     * @param cvScoreTool
     */
    public ToolAIAgent(ChatClient.Builder builder, ChatMemory memory,
                           CvMatcherTool cvMatcherTool,
                           CvScoreTool cvScoreTool,
                           CvStandardRequirementTool cvStandardRequirementTool) {

        this.chatClient = builder
                .defaultSystem(AGENT_DEFAULT_SYSTEM_PROMPT) .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(memory).build()
                )
                .defaultTools(ToolCallbacks.from(cvMatcherTool, cvScoreTool, cvStandardRequirementTool))
                .build();
    }

}

