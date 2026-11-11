package org.cvanalyzer.backoffice.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.cvanalyzer.backoffice.model.CvMatcherDto;
import org.cvanalyzer.backoffice.service.impl.CvAnalysisServiceImpl;
import org.cvanalyzer.backoffice.service.impl.CvImportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@lombok.extern.slf4j.Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DocumentController {


    private final CvImportServiceImpl cvImportService;

    private final CvAnalysisServiceImpl cvAnalysisService;


    /**
     * controller for uploading a cv
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importFile(@RequestParam("file") MultipartFile file)
            throws IOException {

        log.info("upload CV");
        String res = cvImportService.handleFileImport(file);
        return ResponseEntity.ok().build();
    }

    /**
     * search for a cv based on criteria
     * @param criteria the criteria
     * @return
     */
    @GetMapping("/search")
    public ResponseEntity<CvMatcherDto> searchCvBasedOnCriteria(@RequestParam String criteria) {
        log.info("search for CV with the criteria {}", criteria);

        CvMatcherDto res = cvAnalysisService.searchCvBasedOnCriteria(criteria);
        return ResponseEntity.ok().body(res);
    }
//
//    @GetMapping("/search/skills")
//    public ResponseEntity<List<CvScoreResponseDto>> searchSkills(@RequestParam("query") List<String> query) {
//        log.info("search for CV matching the following skills {}", query.toString());
//        List<CvScoreResponseDto> res = agent.askAgent(Prompts.SKILL_MATCH, query.toString(), new TypeReference<List<CvScoreResponseDto>>() {});
//        return ResponseEntity.ok().body(res);
//    }
//
//    @PostMapping("/init/embedding")
//    public ResponseEntity<String> initEmbedding() {
//        String res = agent.askAgent(Prompts.INIT_STANDARD_SKILLS_EMBEDDING, "", new TypeReference<String>() {});
//        return ResponseEntity.ok().build();
//    }
}
