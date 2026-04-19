package org.cvanalyzer.backoffice.controller;

import lombok.AllArgsConstructor;
import org.cvanalyzer.backoffice.component.AIAgent;
import org.cvanalyzer.backoffice.component.CVAnalysisHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class DocumentController {


    @Autowired
    private final CVAnalysisHandler cvAnalysisHandler;

    @Autowired
    private final AIAgent agent;

    @PostMapping(path = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> importFile(@RequestParam("file") MultipartFile file)
            throws IOException {

        String res = cvAnalysisHandler.handleFileImport(file);
//        String res = cvAnalysisHandler.handleFileImport("/home/yassine/Bureau/cv-analyzer/backoffice/src/main/resources/cv-1.pdf");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<String> search(@RequestParam String query) {
      //  String res = agent.askAgent("Captain of the black pearl");
        String res = agent.askAgent(query);
       // String res = cvAnalysisHandler.handleFileSimilaritySearch();
        return ResponseEntity.ok().body(res);
    }
}
