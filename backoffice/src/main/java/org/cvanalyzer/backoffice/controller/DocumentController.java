package org.cvanalyzer.backoffice.controller;

import lombok.AllArgsConstructor;
import org.cvanalyzer.backoffice.component.CVAnalysisHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class DocumentController {


    @Autowired
    private final CVAnalysisHandler cvAnalysisHandler;

    @PostMapping(path = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> importFile(@RequestParam("file") MultipartFile file)
            throws IOException {

        String res = cvAnalysisHandler.handleFileImport(file);
//        String res = cvAnalysisHandler.handleFileImport("/home/yassine/Bureau/cv-analyzer/backoffice/src/main/resources/cv-1.pdf");
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/search")
    public ResponseEntity<String> search() {
        String res = cvAnalysisHandler.handleFileSimilaritySearch();
        return ResponseEntity.ok(res);
    }
}
