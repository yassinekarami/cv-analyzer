package org.cvanalyzer.backoffice.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.cvanalyzer.backoffice.model.CvMatcherDto;
import org.cvanalyzer.backoffice.service.CvImportService;
import org.cvanalyzer.backoffice.utils.Prompts;
import org.cvanalyzer.backoffice.service.StorageService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class CvImportServiceImpl implements CvImportService {

    /**
     * documentService used to extract text from file
     */
    private final PdfDocumentServiceImpl documentService;
    /**
     * storageService used to store files
     */
    private final StorageService storageService;
    /**
     * chat client being used to prompt
     */
    private final ChatClient chatClient;

    /**
     * Constructor for CvImportServiceImpl
     * @param documentService
     * @param storageService
     * @param chatClient
     */
    public CvImportServiceImpl(PdfDocumentServiceImpl documentService,
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
                    .user(Prompts.EXTRACT_USEFUL_DATA_FROM_STRING.formatted(result, file.getOriginalFilename()))
                    .call()
                    .content();


        }
        catch (IOException e) {

        }
        return "OK";
    }
}
