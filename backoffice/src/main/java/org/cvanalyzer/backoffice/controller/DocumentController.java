package org.cvanalyzer.backoffice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import org.cvanalyzer.backoffice.ai.prompt.Prompts;
import org.cvanalyzer.backoffice.component.AIAgent.ToolAIAgent;
import org.cvanalyzer.backoffice.component.CVAnalysisHandler;
import org.cvanalyzer.backoffice.model.CvScoreResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class DocumentController {


    @Autowired
    private final CVAnalysisHandler cvAnalysisHandler;

    @Autowired
    private final ToolAIAgent agent;

    @PostMapping(path = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> importFile(@RequestParam("file") MultipartFile file)
            throws IOException {

        String res = cvAnalysisHandler.handleFileImport(file);
//        String res = cvAnalysisHandler.handleFileImport("/home/yassine/Bureau/cv-analyzer/backoffice/src/main/resources/cv-1.pdf");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<String> searchCv(@RequestParam String query) {
        String res = agent.askAgent(Prompts.CV_MATCH,query, new TypeReference<String>() {});
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/search/skills")
    public ResponseEntity<List<CvScoreResponseDto>> searchSkills(@RequestParam List<String> query) {
        //List<CvScoreResponseDto> r
        List<CvScoreResponseDto> res = agent.askAgent(Prompts.SKILL_MATCH, query.toString(), new TypeReference<List<CvScoreResponseDto>>() {});
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/init/embedding")
    public ResponseEntity<String> initEmbedding() {
        String res = agent.askAgent(Prompts.INIT_STANDARD_SKILLS_EMBEDDING, "", new TypeReference<String>() {});
        return ResponseEntity.ok().build();
    }
}
