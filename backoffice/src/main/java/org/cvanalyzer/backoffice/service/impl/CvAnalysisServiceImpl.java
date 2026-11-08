package org.cvanalyzer.backoffice.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cvanalyzer.backoffice.utils.Prompts;
import org.cvanalyzer.backoffice.service.CvAnalysisService;
import org.cvanalyzer.backoffice.service.DocumentService;
import org.cvanalyzer.backoffice.service.StorageService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service

public class CvAnalysisServiceImpl implements CvAnalysisService {

    /**
     * documentService used to extract text from file
     */
    private DocumentService documentService;
    /**
     * storageService used to store files
     */
    private StorageService storageService;
    /**
     * chat client being used to prompt
     */
    private ChatClient chatClient;

    /**
     * Constructor for CvAnalysisServiceImpl
     * @param documentService
     * @param storageService
     * @param chatClient
     */
    public CvAnalysisServiceImpl(DocumentService documentService,
                                 StorageService storageService,
                                 @Qualifier("noMemoryChatClient") ChatClient chatClient) {
        this.documentService = documentService;
        this.storageService = storageService;
        this.chatClient = chatClient;
    }

    public String handleFileImport(MultipartFile file) throws IOException {
        try {
            String result = documentService.convertPdfIntoString(file);
            Document doc = new Document(result);
            storageService.store(file);


            chatClient.prompt()
                    .user(Prompts.EXTRACT_USEFUL_DATA_FROM_STRING.formatted(result))
                    .call()
                    .content();


        }
        catch (IOException e) {

        }
        return "OK";
    }
}
