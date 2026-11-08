package org.cvanalyzer.backoffice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface DocumentService {

    String convertPdfIntoString(MultipartFile input) throws IOException;
}
