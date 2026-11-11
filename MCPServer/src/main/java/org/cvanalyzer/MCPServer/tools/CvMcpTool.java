package org.cvanalyzer.MCPServer.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cvanalyzer.MCPServer.record.CvRecord;
import org.cvanalyzer.MCPServer.record.CvScoreResponseRecord;
import org.cvanalyzer.MCPServer.service.impl.CvAnalysisServiceImpl;
import org.cvanalyzer.MCPServer.service.impl.CvScoreServiceImpl;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class CvMcpTool {

    /**
     * cvAnalysisService containing method for analyzing and extract data
     */
    private final CvAnalysisServiceImpl cvAnalysisService;

    /**
     * cvScoreService used to compute the cv score
     */
    private final CvScoreServiceImpl cvScoreService;

    @McpTool(name = "extractCvDataFromString",
            description = "Extract structured information from a software engineer CV. " +
                    "Identifies personal information, technical skills, " +
                    "professional experiences, education and certifications. " +
                    "Returns structured candidate information.")
    public McpSchema.CallToolResult extractDataAndGenerateEmbedding(@McpToolParam(description = "text") String text,
                                                                    @McpToolParam(description = "filename") String filename) throws JsonProcessingException {


        log.info("Starting CV extraction for file: {}", filename);

        CvRecord result =  cvAnalysisService.analyzeAndIndex(text, filename);

        return McpSchema.CallToolResult.builder().structuredContent(result).isError(false).build();
    }


    @McpTool(
            name = "scoreComputeTool",
            description = "Rank CV according to the skills in the query"
    )
    public McpSchema.CallToolResult  scoreCompute(
            @McpToolParam(description = "contains the skills used to score the cv") String query)
            throws JsonProcessingException {

        List<CvScoreResponseRecord> result = cvScoreService.computeScore(query);
        return McpSchema.CallToolResult.builder().structuredContent(result).isError(false).build();

    }

    /**
     * Tool for performing a similarity search in vector store with the given query
     * @param query the query to use for similarity search
     * @return the filename of the similar document
     */
    @McpTool(name = "findBySimilarity",
            description = "Find the most relevant CV based on the criteria passed in parameters.")
    public McpSchema.CallToolResult findBySimilarity(@McpToolParam(description = "the criteria to evaluate the cv") String query) {

        log.info("Search the cv matching the following parameters {}", query);
        String result = cvAnalysisService.findSimilarCv(query);
        return McpSchema.CallToolResult.builder().structuredContent(result).isError(false).build();

    }
}
