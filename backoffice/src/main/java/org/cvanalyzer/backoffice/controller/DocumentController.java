package org.cvanalyzer.backoffice.controller;

import lombok.AllArgsConstructor;
import org.cvanalyzer.backoffice.component.CVAnalysisHandler;
import org.cvanalyzer.backoffice.service.AiService;
import org.cvanalyzer.backoffice.service.impl.OpenAIServiceImpl;
import org.cvanalyzer.backoffice.service.DocumentService;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class DocumentController {


    @Autowired
    private final CVAnalysisHandler cvAnalysisHandler;

    @GetMapping("/")
    public ResponseEntity<EmbeddingResponse> test() throws IOException, SAXException {

     //   String result = service.detect("/home/yassine/Bureau/cv-analyzer/backoffice/src/main/resources/cv-1.pdf");
        String res = cvAnalysisHandler.handleFileImport("/home/yassine/Bureau/cv-analyzer/backoffice/src/main/resources/cv-1.pdf");
        return ResponseEntity.ok().body(res);
    }
}
