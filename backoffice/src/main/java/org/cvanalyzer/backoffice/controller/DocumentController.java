package org.cvanalyzer.backoffice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import groovy.util.logging.Slf4j;
import lombok.AllArgsConstructor;
import org.cvanalyzer.backoffice.ai.prompt.Prompts;
import org.cvanalyzer.backoffice.component.AIAgent.ToolAIAgent;
import org.cvanalyzer.backoffice.component.CVAnalysisHandler;
import org.cvanalyzer.backoffice.model.CvMatcherDto;
import org.cvanalyzer.backoffice.model.CvScoreResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@lombok.extern.slf4j.Slf4j
@RestController
@AllArgsConstructor
@CrossOrigin("*")
@Slf4j
public class DocumentController {


    @Autowired
    private final CVAnalysisHandler cvAnalysisHandler;

    @Autowired
    private final ToolAIAgent agent;

    @PostMapping(path = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> importFile(@RequestParam("file") MultipartFile file)
            throws IOException {

        log.info("upload CV");
        String res = cvAnalysisHandler.handleFileImport(file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<CvMatcherDto> searchCv(@RequestParam String query) {
        log.info("search for CV with the query {}", query);
        CvMatcherDto res = agent.askAgent(Prompts.CV_MATCH,query, new TypeReference<CvMatcherDto>() {});
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/search/skills")
    public ResponseEntity<List<CvScoreResponseDto>> searchSkills(@RequestParam("query") List<String> query) {
        log.info("search for CV matching the following skills {}", query.toString());
        List<CvScoreResponseDto> res = agent.askAgent(Prompts.SKILL_MATCH, query.toString(), new TypeReference<List<CvScoreResponseDto>>() {});
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/init/embedding")
    public ResponseEntity<String> initEmbedding() {
        String res = agent.askAgent(Prompts.INIT_STANDARD_SKILLS_EMBEDDING, "", new TypeReference<String>() {});
        return ResponseEntity.ok().build();
    }
}
